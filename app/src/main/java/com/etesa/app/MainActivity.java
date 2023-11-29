package com.etesa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.etesa.app.faculty.FacultyDashboardActivity;
import com.etesa.app.utilMain.AndroidUtil;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;
import com.google.android.material.textview.MaterialTextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ImageView imgLogo;
    MaterialTextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgLogo = findViewById(R.id.imgLogo);
        welcomeText = findViewById(R.id.welcomeText);

        if (FirebaseUtilMain.isLoggedIn()) {
            FirebaseUtilMain.getCurrentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    UserModelMain model = task.getResult().toObject(UserModelMain.class);
                    String rRole = model.getUserRole();
                    facultyDashboardScreen(model, rRole);
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
    private void facultyDashboardScreen(UserModelMain model, String role) {

        if (role == "HOD") {
            Intent intent = new Intent(getApplicationContext(), HodDashboard.class);
//        intent.putExtra(userName, "userName");
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else if (role == "Faculty") {
            Intent intent = new Intent(getApplicationContext(), FacultyDashboardActivity.class);
//        intent.putExtra(userName, "userName");
            AndroidUtil.passUserModelAsIntent(intent, model);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }

    }
}