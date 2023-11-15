package com.etesa.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.etesa.app.utilMain.AndroidUtilMain;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;
import com.etesa.app.model.admin.activity.AdminMainActivity;
import com.etesa.app.model.faculty.activity.FacultyMainActivity;
import com.etesa.app.model.hodorlabassistant.activity.HodMainActivity;

public class MainActivity extends AppCompatActivity {

    private TextView splashTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        if (FirebaseUtilMain.isLoggedIn()) {
            // Get the current user's details
            FirebaseUtilMain.getCurrentUserDetails().get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Handle successful retrieval of user data
                    UserModelMain model = task.getResult().toObject(UserModelMain.class);
                    String userRole = model.getUserRole();

                    startRoleActivity(userRole, model);
                } else {
                    // Handle error
                    Log.e("MainActivity", "Error fetching user data", task.getException());
                    Toast.makeText(getApplicationContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            // If the user is not logged in, proceed with the login activity
            new Handler().postDelayed(() -> {
                startActivity(new Intent(MainActivity.this, LoginActivityMain.class));
                finish();
            }, 1000);
        }
    }

    private void startRoleActivity(String userRole, UserModelMain model) {
        Intent intent;
            if (userRole == null) {
                // Handle the error
                Log.e("MainActivity", "User role is null");
                intent = new Intent(getApplicationContext(), LoginActivityMain.class);
                AndroidUtilMain.passUserModelAsIntent(intent, model);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                // Finish the current activity after starting the new activity
                finish();
                Toast.makeText(getApplicationContext(), "Error fetching user data", Toast.LENGTH_SHORT).show();

                return;
            } else {
                switch (userRole) {
                    case "HOD":
                        intent = new Intent(getApplicationContext(), HodMainActivity.class);
                        break;
                    case "Faculty":
                        intent = new Intent(getApplicationContext(), FacultyMainActivity.class);
                        break;
                    case "Admin":
                        intent = new Intent(getApplicationContext(), AdminMainActivity.class);
//                        AdminAndroidUtil.passUserModelAsIntent(intent, model);
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Unknown role: " + userRole, Toast.LENGTH_SHORT).show();
                        return;
                }

                AndroidUtilMain.passUserModelAsIntent(intent, model);
                // If the user is logged in, directly start the appropriate activity
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                // Finish the current activity after starting the new activity
                finish();
            }// ...
        }

    private void initUi() {
        splashTag = findViewById(R.id.splashtag);
    }
}
