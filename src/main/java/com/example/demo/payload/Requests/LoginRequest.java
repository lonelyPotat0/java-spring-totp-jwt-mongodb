package com.example.demo.payload.Requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotBlank(message = "Username can't be empty")
    @Size(min = 6, max = 25, message = "About Me must be between 6 and 25 characters")
    public String username;

    @NotBlank(message = "Password can't be empty")
    public String password;

    public String TOTP;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassowrd(String password) {
        this.password = password;
    }

    public String getTOTP() {
        return this.TOTP;
    }

    public void setTOTP(String totp) {
        this.TOTP = totp;
    }

    @Override
    public String toString() {
        return "username : " + this.username + " password : " + this.password + " TOTP : " + this.TOTP;
    }

}
