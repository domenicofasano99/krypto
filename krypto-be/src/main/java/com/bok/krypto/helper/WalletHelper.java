package com.bok.krypto.helper;

import com.bok.krypto.core.AddressGenerator;
import com.bok.krypto.exception.InvalidRequestException;
import com.bok.krypto.exception.KryptoNotFoundException;
import com.bok.krypto.exception.TransactionException;
import com.bok.krypto.exception.WalletAlreadyExistsException;
import com.bok.krypto.exception.WalletNotFoundException;
import com.bok.krypto.integration.internal.dto.ValidationRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletDeleteResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletInfoDTO;
import com.bok.krypto.integration.internal.dto.WalletRequestDTO;
import com.bok.krypto.integration.internal.dto.WalletResponseDTO;
import com.bok.krypto.integration.internal.dto.WalletsDTO;
import com.bok.krypto.messaging.messages.WalletCreationMessage;
import com.bok.krypto.messaging.messages.WalletDeleteMessage;
import com.bok.krypto.model.Account;
import com.bok.krypto.model.BalanceSnapshot;
import com.bok.krypto.model.Krypto;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.WalletRepository;
import com.bok.krypto.service.bank.BankService;
import com.bok.krypto.service.interfaces.MessageService;
import com.bok.krypto.util.DTOUtils;
import com.bok.parent.integration.message.EmailMessage;
import com.google.common.base.Preconditions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.bok.krypto.util.Constants.mathContext;

@Slf4j
@Component
public class WalletHelper {

    @Autowired
    WalletRepository walletRepository;

    @Autowired
    TransactionHelper transactionHelper;

    @Autowired
    TransferHelper transferHelper;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    KryptoHelper kryptoHelper;

    @Autowired
    MessageService messageService;

    @Autowired
    MarketHelper marketHelper;

    @Autowired
    BankService bankService;

    @Autowired
    AddressGenerator addressGenerator;

    @Autowired
    BalanceSnapshotHelper balanceSnapshotHelper;


    public Wallet findByPublicId(String publicId) {
        return walletRepository.findByAddressAndDeletedIsFalse(publicId).orElseThrow(() -> new WalletNotFoundException("wallet not found"));
    }

    public Wallet findByAccountIdAndSymbol(Long userId, String symbol) throws WalletNotFoundException {
        return walletRepository.findByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(userId, symbol).orElseThrow(() -> new WalletNotFoundException("wallet not found"));

    }

    @Transactional
    public BigDecimal withdraw(Wallet wallet, BigDecimal amount) {
        Wallet w = findByPublicId(wallet.getAddress());
        if (w.getAvailableAmount().compareTo(amount) < 0) {
            throw new TransactionException("not enough funds to perform the withdrawal");
        }
        log.info("withdrawing {} from wallet {}", amount, w.getAddress());
        BigDecimal newBalance = w.getAvailableAmount().subtract(amount, mathContext);
        w.setAvailableAmount(newBalance);
        balanceSnapshotHelper.save(w.createSnapshot());
        walletRepository.saveAndFlush(w);
        log.info("wallet {} balance: {}", w.getAddress(), newBalance);
        return amount;
    }

    @Transactional
    public BigDecimal deposit(Wallet wallet, BigDecimal amount) {
        Wallet w = findByPublicId(wallet.getAddress());
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new InvalidRequestException("Cannot deposit negative amounts");
        }
        log.info("depositing {} {} to wallet {}", amount, w.getKrypto().getSymbol(), wallet.getAddress());
        BigDecimal newBalance = w.getAvailableAmount().add(amount, mathContext);
        w.setAvailableAmount(newBalance);
        balanceSnapshotHelper.save(w.createSnapshot());
        walletRepository.saveAndFlush(w);
        log.info("wallet {} balance: {}", wallet.getAddress(), newBalance);
        return amount;
    }

    public Boolean existsByAccountIdAndSymbol(Long accountId, String symbol) {
        return walletRepository.existsByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(accountId, symbol);
    }

    public WalletResponseDTO createWallet(Long accountId, WalletRequestDTO requestDTO) {
        if (!kryptoHelper.existsBySymbol(requestDTO.symbol)) {
            throw new KryptoNotFoundException("This krypto doesn't exists");
        }
        if (walletRepository.existsByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(accountId, requestDTO.symbol)) {
            throw new WalletAlreadyExistsException("A wallet with the same Krypto exists for this user");
        }
        log.info("Creating {} wallet for account {}", requestDTO.symbol, accountId);

        Wallet w = new Wallet();
        w.setAccount(accountHelper.findById(accountId));
        w = walletRepository.saveAndFlush(w);
        WalletCreationMessage walletCreationMessage = new WalletCreationMessage();
        walletCreationMessage.id = w.getId();
        walletCreationMessage.symbol = requestDTO.symbol;
        messageService.sendWallet(walletCreationMessage);

        return new WalletResponseDTO(WalletResponseDTO.Status.ACCEPTED);
    }

    @Transactional
    public void handleMessage(WalletCreationMessage walletCreationMessage) {
        Wallet w = walletRepository.findById(walletCreationMessage.id)
                .orElseThrow(() -> new RuntimeException("This wallet should have been pre-persisted."));

        w.setAddress(addressGenerator.generateWalletAddress());
        w.setKrypto(kryptoHelper.findBySymbol(walletCreationMessage.symbol));
        w.setAvailableAmount(BigDecimal.ZERO);
        w.setStatus(Wallet.Status.CREATED);
        save(w);
        balanceSnapshotHelper.save(w.createSnapshot());
        messageService.sendEmail(emailWalletCreation(w, w.getAccount()));
    }

    public Wallet createWallet(Account account, Krypto k) {
        Wallet w = new Wallet();
        w.setAccount(account);
        w.setAddress(addressGenerator.generateWalletAddress());
        w.setKrypto(k);
        w.setAvailableAmount(BigDecimal.ZERO);
        w = save(w);
        w.setStatus(Wallet.Status.CREATED);
        w = save(w);
        balanceSnapshotHelper.save(w.createSnapshot());
        messageService.sendEmail(emailWalletCreation(w, w.getAccount()));
        return w;
    }

    private EmailMessage emailWalletCreation(Wallet wallet, Account account) {
        EmailMessage email = new EmailMessage();
        email.subject = "BOK - Wallet creation";
        email.body = "Your wallet for Krypto " + wallet.getKrypto().getSymbol() + " has been created.";
        email.to = accountHelper.getEmailByAccountId(account.getId());
        return email;
    }

    public WalletDeleteResponseDTO delete(Long accountId, WalletDeleteRequestDTO deleteRequestDTO) {
        Preconditions.checkArgument(accountHelper.existsById(accountId));
        Preconditions.checkArgument(walletRepository.existsByAccount_IdAndKrypto_SymbolAndDeletedIsFalse(accountId, deleteRequestDTO.symbol));

        WalletDeleteMessage message = new WalletDeleteMessage();
        message.accountId = accountId;
        message.symbol = deleteRequestDTO.symbol;
        messageService.sendWalletDeletion(message);

        Account account = accountHelper.findById(accountId);
        Wallet wallet = findByAccountIdAndSymbol(accountId, deleteRequestDTO.symbol);
        marketHelper.emptyWallet(account, wallet);

        delete(wallet);
        String to, subject, text;
        to = accountHelper.getEmailByAccountId(account.getId());
        subject = "BOK - Wallet deletion";
        text = "Your wallet " + wallet.getKrypto().getSymbol() + " has been deleted.";
        sendMarketEmail(subject, to, text);
        return new WalletDeleteResponseDTO(wallet.getAddress());
    }

    private void delete(Wallet wallet) {
        wallet.setDeleted(true);
        walletRepository.saveAndFlush(wallet);
    }

    public void handleWalletDeletion(WalletDeleteMessage walletDeleteMessage) {

        Account account = accountHelper.findById(walletDeleteMessage.accountId);
        Wallet wallet = findByAccountIdAndSymbol(walletDeleteMessage.accountId, walletDeleteMessage.symbol);
        marketHelper.emptyWallet(account, wallet);
        walletRepository.delete(wallet);
        String to, subject, text;
        to = accountHelper.getEmailByAccountId(account.getId());
        subject = "BOK - Wallet deletion";
        text = "Your wallet " + wallet.getKrypto().getSymbol() + " has been deleted.";
        sendMarketEmail(subject, to, text);
    }

    public void sendMarketEmail(String subject, String email, String text) {
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.subject = subject;
        emailMessage.to = email;
        emailMessage.body = text;
        messageService.sendEmail(emailMessage);
    }

    @Transactional
    public WalletsDTO getWallets(Long accountId) {
        Preconditions.checkArgument(accountHelper.existsById(accountId));
        List<Wallet> wallets = walletRepository.findByAccount_IdAndDeletedIsFalse(accountId);
        WalletsDTO walletsDTO = new WalletsDTO();
        walletsDTO.wallets = new ArrayList<>();

        for (Wallet w : wallets) {
            WalletInfoDTO walletInfoDTO = new WalletInfoDTO();
            if (w.getStatus().equals(Wallet.Status.PENDING)) {
                walletInfoDTO.status = WalletInfoDTO.Status.valueOf(w.getStatus().name());
            } else {
                walletInfoDTO = getInfoFromWallet(w);
            }
            walletsDTO.wallets.add(walletInfoDTO);
        }

        return walletsDTO;
    }

    private WalletInfoDTO getInfoFromWallet(Wallet wallet) {
        WalletInfoDTO info = new WalletInfoDTO();
        info.address = wallet.getAddress();
        info.availableAmount = wallet.getAvailableAmount();
        info.symbol = wallet.getKrypto().getSymbol();
        info.creationTimestamp = wallet.getCreationTime();
        info.updateTimestamp = wallet.getUpdateTime();
        info.status = WalletInfoDTO.Status.valueOf(wallet.getStatus().name());
        return info;
    }


    public WalletInfoDTO info(Long accountId, String symbol, Instant startDate, Instant endDate) {
        Preconditions.checkArgument(accountHelper.existsById(accountId));

        Wallet wallet = findByAccountIdAndSymbol(accountId, symbol);
        WalletInfoDTO info = getInfoFromWallet(wallet);

        info.activities = transactionHelper.findByWalletIdAndDateBetween(wallet, startDate, endDate)
                .stream()
                .map(DTOUtils::toDTO)
                .collect(Collectors.toList());

        info.activities.addAll(transferHelper.findByWalletIdAndDateBetween(wallet, startDate, endDate)
                .stream()
                .map(DTOUtils::toDTO)
                .collect(Collectors.toList()));

        info.balanceHistory = findBalanceSnapshotByWalletAndDateBetween(wallet, startDate, endDate)
                .stream()
                .map(DTOUtils::toDTO)
                .collect(Collectors.toList());
        return info;
    }

    public List<BalanceSnapshot> findBalanceSnapshotByWalletAndDateBetween(Wallet wallet, Instant from, Instant until) {
        return balanceSnapshotHelper.findHistory(wallet)
                .stream()
                .filter(t -> t.getTimestamp().isAfter(from)
                        && t.getTimestamp().isBefore(until))
                .collect(Collectors.toList());

    }

    public Boolean validateAddress(ValidationRequestDTO validationRequestDTO) {
        return walletRepository.existsByAddressAndKrypto_SymbolAndDeletedIsFalse(validationRequestDTO.address, validationRequestDTO.symbol);
    }

    public Wallet save(Wallet wallet) {
        return walletRepository.save(wallet);
    }
}
