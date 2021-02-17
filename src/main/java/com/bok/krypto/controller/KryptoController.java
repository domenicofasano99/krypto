package com.bok.krypto.controller;

import com.bok.krypto.service.interfaces.KryptoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/krypto")
public class KryptoController {

    @Autowired
    KryptoService kryptoService;
}
