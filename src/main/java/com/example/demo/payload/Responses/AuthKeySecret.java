package com.example.demo.payload.Responses;

public class AuthKeySecret {
    private String authkey;
    private String qrCodeFormat;

    public String getAuthKey() {
        return this.authkey;
    }
    public void setAuthKey(String authkey) {
        this.authkey = authkey;
    }

    public void setQrCodeFormat(String username) {
        this.qrCodeFormat = "otpauth://totp/Potato:"+ username + "?secret=" + this.authkey +"&issuer=Potato";
    }
    public String getQrCodeFormat(String username) {
        return this.qrCodeFormat;
    }
}
