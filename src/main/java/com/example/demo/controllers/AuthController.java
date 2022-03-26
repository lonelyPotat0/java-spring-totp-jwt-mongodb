package com.example.demo.controllers;

import javax.validation.Valid;

import com.example.demo.models.User;
import com.example.demo.payload.Requests.LoginRequest;
import com.example.demo.payload.Requests.SignupRequest;
import com.example.demo.payload.Responses.JwtResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.config.JwtTokenUtil;
import com.example.demo.security.services.JwtUserDetailService;
import com.example.demo.services.AuthService;
import com.example.demo.ultils.TOTPTool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUserDetailService userDetailsService;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    AuthService authService = new AuthService();
    TOTPTool totpTool = new TOTPTool();


    @PostMapping("signup")
    public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) throws Exception {
        if (!signupRequest.getUsername().matches("\\s*\\S+\\s*")) {
            return ResponseEntity.badRequest().body("Username must not include space");
        }
        if (this.usernameIsExist(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("username already exist");
        }
        User user = new User();
        user = this.authService.createUser(signupRequest);
        try {
            userRepository.save(user);
            return ResponseEntity.ok("user created");
        } catch (Exception error) {
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {
        System.out.println(loginRequest);
        if (!this.usernameIsExist(loginRequest.getUsername())) {
            return ResponseEntity.badRequest().body("username doesn't exist");
        }
        User user = new User();
        try {
            user = userRepository.findByUsername(loginRequest.getUsername());
        } catch (Exception err) {
        }

        Boolean success = this.authService.authenticateUser(user, loginRequest);
        if (success) {
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(loginRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
            // return ResponseEntity.ok("Hello world");

        }
        return ResponseEntity.badRequest().body("unauthorized");
    }

    @PostMapping("test-verify")
    public ResponseEntity<?> testVerify(@Valid @RequestBody LoginRequest loginRequest) {
        User user = new User();
        try {
            user = userRepository.findByUsername(loginRequest.getUsername());
        } catch (Exception err) {
        }

        Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), loginRequest.getTOTP());

        if (success) {
            return ResponseEntity.ok("hello");
        }
        return ResponseEntity.badRequest().body("fail");
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
