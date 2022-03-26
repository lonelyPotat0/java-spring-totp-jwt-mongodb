package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.repositories.UserRepository;
import com.example.demo.ultils.TOTPTool;
import com.example.demo.ultils.TokenTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TotpService {
    
    @Autowired
    UserRepository userRepository;

    TokenTool tokenTool = new TokenTool();
    TOTPTool totpTool = new TOTPTool();

    public Boolean enableTFA(String bearer, String totp) {
        System.out.println(this.tokenTool.getUsernameFromToken(bearer));
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) return false;
        User user = userRepository.findByUsername(username);
        Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), totp);
        System.out.println( "is success" + success);
        if (success) {
            user.setTfa(true);
            userRepository.save(user);
        }
        return success;
        // return false;
    }

    public Boolean disableTFA(String bearer, String totp) {
        System.out.println(this.tokenTool.getUsernameFromToken(bearer));
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) return false;
        User user = userRepository.findByUsername(username);
        Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), totp);
        System.out.println( "is success" + success);
        if (success) {
            user.setTfa(false);
            userRepository.save(user);
        }
        return success;
        // return false;
    }

    private Boolean usernameIsExist(String username) {
        try {
            Boolean user = userRepository.existsByUsername(username);
            if (user) {
                return true;
            }
        } catch (Exception err) {
        }
        return false;
    }
}
