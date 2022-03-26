package com.example.demo.payload.Requests;

public class TOTP {
    public String TOTP;
    
    public String getTOTP() {
        return this.TOTP;
    }

    public void setTOTP(String TOTP) {
        this.TOTP = TOTP;
    }

    @Override
    public String toString() {
        return "TOTP : " + this.toString();
    }
}
