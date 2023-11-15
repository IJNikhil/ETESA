package com.etesa.app.utilMain;

import com.google.firebase.Timestamp;

public class UserModelMain {
    private String userRole;
    private String userEmail;
    private String userPassword;
    private Timestamp createdTimestamp;
    private String fcmToken;
    private String userId;

    public UserModelMain() {}

    public UserModelMain(String userRole, String userEmail, String userPassword, Timestamp createdTimestamp, String userId) {
        this.userRole = userRole;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
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

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
