package com.example.demo.user.payload.Responses;

public class SecretKey {
    private String secret;
    private String qrCodeFormat;

    public SecretKey() {}
    
    public SecretKey(String secret, String username) {
        this.secret = secret;
        this.qrCodeFormat = "otpauth://totp/Potato:"+ username + "?secret=" + secret +"&issuer=Potato";
    }

    public String getSecret() {
        return this.secret;
    }
    public void setSecret(String secret) {
        this.secret = secret;
    }

    public void setQrCodeFormat(String username) {
        this.qrCodeFormat = "otpauth://totp/Potato:"+ username + "?secret=" + this.secret +"&issuer=Potato";
    }
    public String getQrCodeFormat() {
        return this.qrCodeFormat;
    }
    
}
