package com.bok.krypto.util;

import com.bok.krypto.integration.internal.dto.TransactionDTO;
import com.bok.krypto.model.Transaction;

public class DTOUtils {

    public static TransactionDTO toDTO(Transaction t) {
        TransactionDTO dto = new TransactionDTO();
        dto.accountId = t.getAccount().getId();
        dto.amount = t.getAmount();
        dto.status = t.getStatus().toString();
        dto.publicId = t.getPublicId();
        dto.type = t.getType().toString();
        return dto;
    }
}
