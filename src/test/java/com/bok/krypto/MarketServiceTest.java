package com.bok.krypto;

import com.bok.krypto.utils.ModelTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContextConfiguration.class)
public class MarketServiceTest {

    @Autowired
    ModelTestUtils modelTestUtils;
}
