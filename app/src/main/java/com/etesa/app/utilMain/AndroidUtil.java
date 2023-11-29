package com.etesa.app.utilMain;

import android.content.Intent;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;

public class AndroidUtil {
    public static void showSnack(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
    }

    public static void passUserModelAsIntent(Intent intent, UserModelMain userModel) {
        intent.putExtra("userName", userModel.getUserName());
        intent.putExtra("userPass", userModel.getUserPassword());
        intent.putExtra("userId", userModel.getUserId());
        intent.putExtra("userEmail", userModel.getUserEmail());
        intent.putExtra("userRole", userModel.getUserRole());
    }
    public static UserModelMain getUserModelFromIntent(Intent intent) {
        UserModelMain userModel = new UserModelMain();
        userModel.setUserName(intent.getStringExtra("userName"));
        userModel.setUserPassword(intent.getStringExtra("userPass"));
        userModel.setUserId(intent.getStringExtra("userId"));
        userModel.setUserRole(intent.getStringExtra("userRole"));
        userModel.setUserEmail(intent.getStringExtra("userEmail"));
        return userModel;
    }
}
