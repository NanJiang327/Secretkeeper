package com.example.lovej.secretkeeper;

/**
 * Created by lovej on 2016/9/9 0009.
 * Description: This class to setup user attribute
 */
public class User {
    private String username;
    private String password;
    private String gender;
    private String email;

    /**
     * @param username The username string
     * @param password The password string
     * @description: <This is constructor for User>
     */
    public User(String username,String password){
        this.username = username;
        this.password = password;
    }

    /**
     * @param username The username string
     * @param password The password string
     * @param gender   The gender string
     * @param email    The email string
     * @description: <This is constructor for User>
     */
    public User(String username,String password,String gender,String email){
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
