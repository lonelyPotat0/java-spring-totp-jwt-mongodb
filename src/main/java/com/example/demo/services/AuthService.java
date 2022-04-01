package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.payload.Requests.LoginRequest;
import com.example.demo.payload.Requests.SignupRequest;
import com.example.demo.payload.Responses.JwtResponse;
import com.example.demo.repositories.UserRepository;
import com.example.demo.security.config.JwtTokenUtil;
import com.example.demo.security.services.JwtUserDetailService;
import com.example.demo.ultils.TOTPTool;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

// import dev.samstevens.totp.code.CodeGenerator;
// import dev.samstevens.totp.code.CodeVerifier;
// import dev.samstevens.totp.code.DefaultCodeGenerator;
// import dev.samstevens.totp.code.DefaultCodeVerifier;
// import dev.samstevens.totp.secret.DefaultSecretGenerator;
// import dev.samstevens.totp.secret.SecretGenerator;
// import dev.samstevens.totp.time.SystemTimeProvider;
// import dev.samstevens.totp.time.TimeProvider;


@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtUserDetailService userDetailsService;

    @Autowired
	private JwtTokenUtil jwtTokenUtil;

    TOTPTool totpTool = new TOTPTool();

    public ResponseEntity<?> createUser(SignupRequest signupRequest) throws Exception {
        if (!signupRequest.getUsername().matches("\\s*\\S+\\s*")) {
            return ResponseEntity.badRequest().body("Username must not include space");
        }
        if (this.usernameIsExist(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("username already exist");
        }
        User user = new User();
        user.setAuthKey("");
        user.setRole("ROLE_USER");
        user.setUsername(signupRequest.getUsername().toLowerCase());
        String hashed;
        System.out.println("password =====> " + signupRequest.getPassword());
        hashed = BCrypt.hashpw(signupRequest.getPassword(), BCrypt.gensalt(10));
        try {
            user.setAuthKey(this.totpTool.SecretGenerator());
        } catch (Exception error) {
            System.out.println("Error =====> " + error);
            return ResponseEntity.badRequest().body(error);
        }
        user.setPassword(hashed);
        user.setTfa(false);
        try {
            // System.out.println("User =====> " + user);
            // return user;
            try {
                userRepository.save(user);
                return ResponseEntity.ok("user created");
            } catch (Exception error) {
                return ResponseEntity.badRequest().body(error);
            }
        } catch (Exception error) {
            System.out.println("Error =====> " + error);
            throw new Exception(error);
        }
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) throws Exception {
        if (!this.usernameIsExist(loginRequest.getUsername())) {
            return ResponseEntity.badRequest().body("username doesn't exist");
        }
        User user = new User();
        try {
            user = userRepository.findByUsername(loginRequest.getUsername());
        } catch (Exception err) {
            return ResponseEntity.badRequest().body(err);
        }
        System.out.println(user);
        Boolean verified = false;
        if (user.isTfa()) {
            if (loginRequest.getTOTP() == null || loginRequest.getTOTP() == "") {
                System.out.println("=================> TOTP required");
                return ResponseEntity.badRequest().body("TOTP required");
            }
            Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), loginRequest.getTOTP());
            if (success) {
                verified = success;
            } else { 
                return ResponseEntity.badRequest().body("TOTP verify fail");
            }
        }
        if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("It matches");
            verified = true;
        } else {
            System.out.println("It does not match");
            verified = false;
        }
        if (verified) {
            final UserDetails userDetails = userDetailsService
                    .loadUserByUsername(loginRequest.getUsername());
            final String token = jwtTokenUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(token));
            // return ResponseEntity.ok("Hello world");

        }
        return ResponseEntity.badRequest().body("unauthorized");
        // return false;
    }

    // public Boolean verifyTOTP(String secret, String code) {
    //     TimeProvider timeProvider = new SystemTimeProvider();
    //     CodeGenerator codeGenerator = new DefaultCodeGenerator();
    //     CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    //     return verifier.isValidCode(secret, code);
    // }
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
