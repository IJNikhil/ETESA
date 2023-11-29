package com.etesa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.etesa.app.faculty.FacultyDashboardActivity;
import com.etesa.app.utilMain.AndroidUtil;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (FirebaseUtilMain.isLoggedIn()) {
            FirebaseUtilMain.getCurrentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModelMain model = task.getResult().toObject(UserModelMain.class);
                    facultyDashboardScreen(model);
                } else {
                    // Handle error
                    Log.e("MainActivity", "Error fetching user data", task.getException());
                    Toast.makeText(getApplicationContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // If the user is not logged in, proceed with the login activity
            new Handler().postDelayed(() -> loginUserScreen(), 1000);
        }
    }
    private void loginUserScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }
    private void facultyDashboardScreen(UserModelMain model) {
        Intent intent = new Intent(getApplicationContext(), FacultyDashboardActivity.class);
//        intent.putExtra(userName, "userName");
        AndroidUtil.passUserModelAsIntent(intent, model);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}