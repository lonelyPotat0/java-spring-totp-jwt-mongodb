package com.example.demo.services;

import com.example.demo.models.User;
import com.example.demo.payload.Requests.LoginRequest;
import com.example.demo.payload.Requests.SignupRequest;
import com.example.demo.ultils.TOTPTool;

import org.mindrot.jbcrypt.BCrypt;
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

    TOTPTool totpTool = new TOTPTool();

    public User createUser(SignupRequest signupRequest) throws Exception {
        User user = new User();
        user.setAuthKey("");
        user.setRole("ROLE_USER");
        user.setUsername(signupRequest.getUsername().toLowerCase());
        String hashed;
        System.out.println("password =====> " + signupRequest.getPassword());
        hashed = BCrypt.hashpw(signupRequest.getPassword(), BCrypt.gensalt(10));
        try {
            // SecretGenerator secretGenerator = new DefaultSecretGenerator();
            // String secret = secretGenerator.generate();
            // user.setAuthKey(secret);
            user.setAuthKey(this.totpTool.SecretGenerator());
        } catch (Exception error) {
            System.out.println("Error =====> " + error);
            throw new Exception(error);
        }
        user.setPassword(hashed);
        user.setTfa(false);
        try {
            // System.out.println("User =====> " + user);
            return user;// user;
        } catch (Exception error) {
            System.out.println("Error =====> " + error);
            throw new Exception(error);
        }
    }

    public Boolean authenticateUser(User user, LoginRequest loginRequest) throws Exception {
        System.out.println(user);
        if (user.isTfa()) {
            if (loginRequest.getTOTP() == null || loginRequest.getTOTP() == "") {
                System.out.println("=================> TOTP required");
                throw new Exception("TOTP required");
            }
            Boolean success = this.totpTool.verifyTOTP(user.getAuthKey(), loginRequest.getTOTP());
            return success;
        }
        if (BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            System.out.println("It matches");
            return true;
        } else {
            System.out.println("It does not match");
            return false;
        }
        // return false;
    }

    // public Boolean verifyTOTP(String secret, String code) {
    //     TimeProvider timeProvider = new SystemTimeProvider();
    //     CodeGenerator codeGenerator = new DefaultCodeGenerator();
    //     CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);
    //     return verifier.isValidCode(secret, code);
    // }

}
