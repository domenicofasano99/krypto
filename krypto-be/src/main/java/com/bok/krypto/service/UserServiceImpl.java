package com.bok.krypto.service;

import com.bok.integration.UserDTO;
import com.bok.krypto.helper.UserHelper;
import com.bok.krypto.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserHelper userHelper;

    @Override
    public void createInternalUser(UserDTO userDTO) {

    }
}
