package com.example.demo.payload.Requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class SignupRequest {

    @NotBlank(message = "Username can't be empty")
    @Size(min = 3, max = 25, message = "About Me must be between 6 and 25 characters")
    public String username;

    @NotBlank(message = "Password can't be empty")
    public String password;

    public String getUsername() {
        return this.username;
    }
    public String getPassword() {
        return this.password;
    }

    @Override
    public String toString() {
        return "username : " + this.username + " password : " + this.password;
    }
}
