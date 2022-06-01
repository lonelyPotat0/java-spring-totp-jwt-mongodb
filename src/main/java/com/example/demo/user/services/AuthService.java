package com.example.demo.user.services;

import com.example.demo.security.config.JwtTokenUtil;
import com.example.demo.security.services.JwtUserDetailService;
import com.example.demo.ultils.TOTPTool;
import com.example.demo.ultils.TokenTool;
import com.example.demo.user.models.User;
import com.example.demo.user.payload.Requests.LoginRequest;
import com.example.demo.user.payload.Requests.SignupRequest;
import com.example.demo.user.payload.Responses.JwtResponse;
import com.example.demo.user.payload.Responses.SecretKey;
import com.example.demo.user.payload.Responses.TfaStatus;
import com.example.demo.user.repositories.UserRepository;
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
    TokenTool tokenTool = new TokenTool();

    public ResponseEntity<?> createUser(SignupRequest signupRequest) throws Exception {
        if (!signupRequest.getUsername().matches("\\s*\\S+\\s*")) {
            return ResponseEntity.badRequest().body("Username must not include space");
        }
        if (this.usernameIsExist(signupRequest.getUsername())) {
            return ResponseEntity.badRequest().body("username already exist");
        }
        User user = new User();
        user.setSecret("");
        user.setRole("ROLE_USER");
        user.setUsername(signupRequest.getUsername().toLowerCase());
        String hashed;
        System.out.println("password =====> " + signupRequest.getPassword());
        hashed = BCrypt.hashpw(signupRequest.getPassword(), BCrypt.gensalt(10));
        try {
            user.setSecret(this.totpTool.SecretGenerator());
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
            Boolean success = this.totpTool.verifyTOTP(user.getSecret(), loginRequest.getTOTP());
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

    public ResponseEntity<?> enableTFA(String bearer, String totp) throws Exception {
        System.out.println(this.tokenTool.getUsernameFromToken(bearer));
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) {
            return ResponseEntity.badRequest().body("username does not exist");
        }
        // return false;
        User user = userRepository.findByUsername(username);
        Boolean success = this.totpTool.verifyTOTP(user.getSecret(), totp);
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
            return ResponseEntity.badRequest().body("username does not exist");
        } // return false;
        User user = userRepository.findByUsername(username);
        Boolean success = this.totpTool.verifyTOTP(user.getSecret(), totp);
        System.out.println( "is success" + success);
        if (success) {
            user.setTfa(false);
            user.setSecret(this.totpTool.SecretGenerator());
            userRepository.save(user);
            return ResponseEntity.ok("success");
        }
        return ResponseEntity.badRequest().body("unsuccess");
        // return false;
    }

    public SecretKey getSecret(String bearer) throws Exception {
        String username = this.tokenTool.getUsernameFromToken(bearer);
        if(!this.usernameIsExist(username)) throw new Exception("username not found");
        User user = userRepository.findByUsername(username);
        SecretKey secret = new SecretKey(user.getSecret(), user.getUsername());
        // secret.setSecret(user.getSecret());
        // secret.setQrCodeFormat(username);
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
