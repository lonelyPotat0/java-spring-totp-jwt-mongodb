package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.payload.Responses.AuthKeySecret;
import com.example.demo.payload.Responses.TfaStatus;
import com.example.demo.repositories.UserRepository;
import com.example.demo.ultils.TOTPTool;
import com.example.demo.ultils.TokenTool;
// import com.google.zxing.NotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TotpService {
    
    @Autowired
    UserRepository userRepository;

    TokenTool tokenTool = new TokenTool();
    TOTPTool totpTool = new TOTPTool();

    public ResponseEntity<?> enableTFA(String bearer, String totp) throws Exception {
        System.out.println(this.tokenTool.getUsernameFromToken(bearer));
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) {
            throw new Exception("username does not exist");
        }
        // return false;
        User user = userRepository.findByUsername(username);
        Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), totp);
        System.out.println( "is success" + success);
        if (success) {
            user.setTfa(true);
            userRepository.save(user);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("unsuccess");
        // return false;
    }

    public ResponseEntity<?> disableTFA(String bearer, String totp) throws Exception {
        System.out.println(this.tokenTool.getUsernameFromToken(bearer));
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) {
            throw new Exception("username does not exist");
        } // return false;
        User user = userRepository.findByUsername(username);
        Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), totp);
        System.out.println( "is success" + success);
        if (success) {
            user.setTfa(false);
            user.setAuthKey(this.totpTool.SecretGenerator());
            userRepository.save(user);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("unsuccess");
        // return false;
    }

    public AuthKeySecret getAuthKey(String bearer) throws Exception {
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) throw new Exception("username not found");
        User user = userRepository.findByUsername(username);
        AuthKeySecret secret = new AuthKeySecret();
        secret.setAuthKey(user.getAuthKey());
        secret.setQrCodeFormat(username);
        return secret;
    }

    public TfaStatus getEnableStatus(String bearer) throws Exception {
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) throw new Exception("username not found");
        User user = userRepository.findByUsername(username);
        TfaStatus status = new TfaStatus();
        status.setEnabled(user.isTfa());
        return status;
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
