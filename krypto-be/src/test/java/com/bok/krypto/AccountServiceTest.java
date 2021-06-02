package com.bok.krypto;

import com.bok.krypto.helper.AccountHelper;
import com.bok.krypto.model.Account;
import com.bok.krypto.repository.AccountRepository;
import com.bok.krypto.utils.ModelTestUtils;
import com.bok.parent.integration.message.AccountCreationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@Slf4j
@ActiveProfiles("test")
public class AccountServiceTest {

    @Autowired
    ModelTestUtils modelTestUtils;

    @Autowired
    AccountHelper accountHelper;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    public void clearAll() {
        modelTestUtils.clearAll();
    }

    @Test
    public void accountCreationMessageReceivedTest() {
        Long accountId = modelTestUtils.randomID();
        String email = ModelTestUtils.faker.internet().emailAddress();
        AccountCreationMessage accountCreationMessage = new AccountCreationMessage();
        accountCreationMessage.accountId = accountId;
        accountCreationMessage.email = email;

        //accountHelper.handle(accountCreationMessage);

        Account account = accountRepository.findById(accountCreationMessage.accountId).orElseThrow(RuntimeException::new);
        Assertions.assertNotNull(account);
    }
}
