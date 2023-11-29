package com.etesa.app.utilMain;

import com.google.firebase.Timestamp;

import java.sql.Time;

public class UserModelMain {
    private String userRole;
    private String userEmail;
    private String userPassword;
    private Timestamp timestamp;
    private String userId;
    private String userName;
    public UserModelMain() {
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Timestamp getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
    public String getUserRole() {
        return userRole;
    }
    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
    public String getUserEmail() {
        return userEmail;
    }
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
    public String getUserPassword() {
        return userPassword;
    }
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public UserModelMain(String userName, String userRole, String userEmail, String userPassword, String userId, Timestamp timestamp) {
        this.timestamp = timestamp;
        this.userName = userName;
        this.userRole = userRole;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userId = userId;
    }
}

