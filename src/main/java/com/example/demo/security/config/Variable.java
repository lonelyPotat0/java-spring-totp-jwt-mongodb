package com.example.demo.security.config;

public class Variable {
    private static final String JwtSecret = "SuperSecret";

    public String getJwtSecret() {
        return Variable.JwtSecret;
    }
}
