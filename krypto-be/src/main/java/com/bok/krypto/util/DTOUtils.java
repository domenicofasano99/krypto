package com.bok.krypto.util;

import com.bok.krypto.integration.internal.dto.ActivityDTO;
import com.bok.krypto.integration.internal.dto.BalanceSnapshotDTO;
import com.bok.krypto.model.BalanceSnapshot;
import com.bok.krypto.model.Transaction;
import com.bok.krypto.model.Transfer;

public class DTOUtils {

    public static ActivityDTO toDTO(Transaction t) {
        ActivityDTO dto = new ActivityDTO();
        dto.accountId = t.getAccount().getId();
        dto.amount = t.getAmount();
        dto.status = t.getStatus().toString();
        dto.publicId = t.getPublicId();
        dto.type = t.type.equals(Transaction.Type.PURCHASE) ? ActivityDTO.Type.PURCHASE : ActivityDTO.Type.SELL;
        return dto;
    }

    public static ActivityDTO toDTO(Transfer t) {
        ActivityDTO dto = new ActivityDTO();
        dto.accountId = t.getAccount().getId();
        dto.amount = t.getAmount();
        dto.status = t.getStatus().toString();
        dto.publicId = t.getPublicId();
        dto.type = ActivityDTO.Type.TRANSFER;
        return dto;
    }

    public static BalanceSnapshotDTO toDTO(BalanceSnapshot balanceSnapshot) {
        BalanceSnapshotDTO wbi = new BalanceSnapshotDTO();
        wbi.amount = balanceSnapshot.getAmount();
        wbi.value = balanceSnapshot.getValue();
        wbi.instant = balanceSnapshot.getTimestamp();
        return wbi;
    }
}
