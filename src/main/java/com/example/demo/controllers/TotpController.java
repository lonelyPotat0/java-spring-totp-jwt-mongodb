package com.example.demo.controllers;

import com.example.demo.payload.Requests.TOTP;
// import com.example.demo.repositories.UserRepository;
import com.example.demo.services.TotpService;
import com.example.demo.ultils.TokenTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/totp")
public class TotpController {
    
    // @Autowired
    // UserRepository userRepository;

    @Autowired
    TotpService totpService;

    TokenTool tokenTool = new TokenTool();

    @PostMapping("enable")
    public ResponseEntity<?> enableTOTP(@RequestHeader("Authorization") String bearer, @RequestBody TOTP totp) {
        Boolean success = this.totpService.enableTFA(bearer, totp.getTOTP());
        if (success) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("unsuccess");
    }

    @PostMapping("disable")
    public ResponseEntity<?> disableTOTP(@RequestHeader("Authorization") String bearer, @RequestBody TOTP totp) {
        Boolean success = this.totpService.disableTFA(bearer, totp.getTOTP());
        if (success) {
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("unsuccess");
    }

    @PostMapping("request-authkey")
    public ResponseEntity<?> requestSecret(@RequestHeader("Authorization") String bearer) throws Exception {
        return ResponseEntity.ok(this.totpService.getAuthKey(bearer));
    }

    @PostMapping("check-2fa-status")
    public ResponseEntity<?> requestStatus(@RequestHeader("Authorization") String bearer) throws Exception {
        return ResponseEntity.ok(this.totpService.getEnableStatus(bearer));
    }


}
