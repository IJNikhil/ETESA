package com.etesa.app.utilMain;

import android.content.Intent;

public class AndroidUtilMain {
    public static void passUserModelAsIntent(Intent intent, UserModelMain userModel) {
        intent.putExtra("email", userModel.getUserEmail());
        intent.putExtra("password", userModel.getUserPassword());
        intent.putExtra("userId", userModel.getUserId());
        intent.putExtra("fcmToken", userModel.getFcmToken());
        intent.putExtra("Timestamp", userModel.getCreatedTimestamp());
    }
}
