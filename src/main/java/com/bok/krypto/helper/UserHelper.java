package com.bok.krypto.helper;

import com.bok.integration.UserDTO;
import com.bok.krypto.exception.UserNotFoundException;
import com.bok.krypto.model.User;
import com.bok.krypto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    @Autowired
    UserRepository userRepository;

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("user does not exist"));
    }

    public String findEmailByUserId(Long id){
        return userRepository.findEmailById(id);
    }

    public Boolean existsById(Long id) {
        return userRepository.existsById(id);
    }

    public User save(Long userId) {
        return userRepository.save(new User(userId));
    }

    public void createInternalUser(UserDTO userDTO) {

    }
}
