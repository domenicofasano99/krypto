package com.bok.krypto.messaging.consumer;

import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.helper.WalletHelper;
import com.bok.parent.integration.message.AccountClosureMessage;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AccountConsumer {

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    WalletHelper walletHelper;

    @JmsListener(destination = "${queue.account.creation}")
    public void marketListener(AccountCreationMessage message) {
        log.info("Received Account Creation Message: " + message.toString());
        accountHelper.createAccount(message);
    }

    @JmsListener(destination = "${queue.account.deletion}")
    public void accountDeletionListener(AccountClosureMessage accountClosureMessage) {
        log.info("Received an account closure message");
        accountHelper.closeAccount(accountClosureMessage);
        walletHelper.closeWallets(accountClosureMessage);
    }
}
