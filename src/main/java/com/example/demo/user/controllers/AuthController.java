package com.example.demo.user.controllers;

import com.example.demo.ultils.TOTPTool;
import com.example.demo.user.payload.Requests.LoginRequest;
import com.example.demo.user.payload.Requests.SignupRequest;
import com.example.demo.user.payload.Requests.TOTP;
import com.example.demo.user.services.AuthService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import com.example.demo.models.User;
// import com.example.demo.payload.Responses.JwtResponse;
// import com.example.demo.repositories.UserRepository;
// import com.example.demo.security.config.JwtTokenUtil;
// import com.example.demo.security.services.JwtUserDetailService;
// import com.example.demo.services.AuthService;

// import org.springframework.security.core.userdetails.UserDetails;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    // AuthService authService = new AuthService();
    TOTPTool totpTool = new TOTPTool();

    @PostMapping("signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) throws Exception {
        return this.authService.createUser(signupRequest);
    }

    @PostMapping("signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        return this.authService.authenticateUser(loginRequest);
    }


    @PostMapping("enable-totp")
    public ResponseEntity<?> enableTOTP(@RequestHeader("Authorization") String bearer, @RequestBody TOTP totp) throws Exception {
        return this.authService.enableTFA(bearer, totp.getTOTP());
    }

    @PostMapping("disable-totp")
    public ResponseEntity<?> disableTOTP(@RequestHeader("Authorization") String bearer, @RequestBody TOTP totp) throws Exception {
        return this.authService.disableTFA(bearer, totp.getTOTP());
    }

    @PostMapping("request-secretkey")
    public ResponseEntity<?> requestSecret(@RequestHeader("Authorization") String bearer) throws Exception {
        return ResponseEntity.ok(this.authService.getSecret(bearer));
    }

    @PostMapping("check-2fa-status")
    public ResponseEntity<?> requestStatus(@RequestHeader("Authorization") String bearer) throws Exception {
        return ResponseEntity.ok(this.authService.getEnableStatus(bearer));
    }

}
