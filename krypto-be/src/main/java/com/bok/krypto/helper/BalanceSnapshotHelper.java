package com.bok.krypto.helper;

import com.bok.krypto.model.BalanceSnapshot;
import com.bok.krypto.model.Wallet;
import com.bok.krypto.repository.BalanceSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Component
public class BalanceSnapshotHelper {

    @Autowired
    BalanceSnapshotRepository balanceSnapshotRepository;

    @Transactional
    public BalanceSnapshot save(BalanceSnapshot bh) {
        return balanceSnapshotRepository.saveAndFlush(bh);
    }

    public List<BalanceSnapshot> findHistory(Wallet wallet) {
        return balanceSnapshotRepository.findByWallet_Id(wallet.getId());
    }
}
