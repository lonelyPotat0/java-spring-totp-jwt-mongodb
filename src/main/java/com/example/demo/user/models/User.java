package com.example.demo.user.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private String id;
    
    @Indexed(unique = true)
    private String username;
    
    private String password;
    private String secret;
    private Boolean tfa;
    private String role;
    

    /**
     * @return String return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return String return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the authKey
     */
    public String getSecret() {
        return secret;
    }

    /**
     * @param authKey the authKey to set
     */
    public void setSecret(String authKey) {
        this.secret = authKey;
    }

    /**
     * @return Boolean return the tfa
     */
    public Boolean isTfa() {
        return tfa;
    }

    /**
     * @param tfa the tfa to set
     */
    public void setTfa(Boolean tfa) {
        this.tfa = tfa;
    }

    /**
     * @return String return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString(){
        return "id : " + this.id + " username : " + this.username + " TFA : " + this.tfa + " secret :" + this.secret + " role : " + this.role + " passsword : " + this.password;
    } 

}
