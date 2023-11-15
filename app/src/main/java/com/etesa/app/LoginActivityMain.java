package com.etesa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.etesa.app.model.admin.activity.AdminMainActivity;
import com.etesa.app.model.faculty.activity.FacultyMainActivity;
import com.etesa.app.model.hodorlabassistant.activity.HodMainActivity;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivityMain extends AppCompatActivity {
    EditText userEmail, usePassword;
    Button loginButton;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();

        userEmail = findViewById(R.id.userEmail);
        usePassword = findViewById(R.id.userpassword);
        loginButton = findViewById(R.id.clickButton);

        loginButton.setOnClickListener(view -> {
            String email, password;
            email = userEmail.getText().toString();
            password = usePassword.getText().toString();

            if (email.isEmpty() || password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Enter correct values", Toast.LENGTH_SHORT).show();
            } else {
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        // Retrieve the user role from Firestore.
                        DocumentReference userRef = FirebaseUtilMain.getCurrentUserDetails();

                        userRef.get().addOnCompleteListener(task2 -> {
                            String role;
                            if (task2.isSuccessful()) {
                                // Get the user role from the document.
                                setData();

                                DocumentSnapshot documentSnapshot = task2.getResult();
                                role = documentSnapshot.getString("role");
                                startRoleActivity(role);
                            } else {
                                // Error getting user document from Firestore.
                                Toast.makeText(getApplicationContext(), "Error retrieving role from Firestore.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Toast.makeText(getApplicationContext(), "Enter correct details", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

//    private void setData() {
//
//        FirebaseUtilMain.getCurrentUserDetails().get().addOnCompleteListener(task -> {
//            if (task.isSuccessful()) {
//                // Get the user role from the document.
//                DocumentSnapshot documentSnapshot = task.getResult();
//                String userRole = documentSnapshot.getString("role");
//                String userEmail = documentSnapshot.getString("email");
//                String userPassword = documentSnapshot.getString("password");
//                Timestamp userTimestamp = documentSnapshot.getTimestamp("timestamp");
//                String userId = documentSnapshot.getString("userId");
//
////                DocumentReference urf = FirebaseUtilMain.getCurrentUserDetails().get().getResult().getReference();
//                // Create a new UserModel object.
//                UserModelMain userModel = new UserModelMain(userRole, userEmail, userPassword, userTimestamp, userId);
//
//                // Set the user details in the UserModel object.
//                userModel.setUserRole(userRole);
//                userModel.setUserEmail(userEmail);
//                userModel.setUserId(userId);
//                userModel.setUserPassword(userPassword);
//                userModel.setCreatedTimestamp(userTimestamp);
//
//                // Save the UserModel object in the database.
//                FirebaseUtilMain.getCurrentUserDetails().set(userModel).addOnCompleteListener(task4 -> {
//                    if (task4.isSuccessful()) {
//                        // The UserModel object has been saved successfully.
////                        startRoleActivity(userRole);
//                        Toast.makeText(getApplicationContext(), "saved user data.", Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        // Error saving the UserModel object to the database.
//                        Toast.makeText(getApplicationContext(), "Error saving user data.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            } else {
//                // Error getting user document from Firestore.
//                Toast.makeText(getApplicationContext(), "Error retrieving role from Firestore.", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }



    private void setData() {
        DocumentReference userRef = FirebaseUtilMain.getCurrentUserDetails();

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Get the user data from the document.
                DocumentSnapshot documentSnapshot = task.getResult();
                String userRole = documentSnapshot.getString("role");
                String userEmail = documentSnapshot.getString("email");
                String userPassword = documentSnapshot.getString("password");
                Timestamp userTimestamp = documentSnapshot.getTimestamp("timestamp");
                String userId = documentSnapshot.getString("userId");

                // Update user data in a separate step
                updateUser(userRole, userEmail, userPassword, userTimestamp, userId);
            } else {
                // Error getting user document from Firestore.
                Toast.makeText(getApplicationContext(), "Error retrieving role from Firestore.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUser(String userRole, String userEmail, String userPassword, Timestamp userTimestamp, String userId) {
        DocumentReference userDocRef = FirebaseFirestore.getInstance().collection("users").document(userId);

        UserModelMain userModel = new UserModelMain(userRole, userEmail, userPassword, userTimestamp, userId);
        userDocRef.set(userModel).addOnCompleteListener(task2 -> {
            if (task2.isSuccessful()) {
                // The UserModel object has been saved successfully.
                Toast.makeText(getApplicationContext(), "Saved user data.", Toast.LENGTH_SHORT).show();

                // Start the appropriate activity based on the user's role
                startRoleActivity(userRole);
            } else {
                // Error saving the UserModel object to the database.
                Toast.makeText(getApplicationContext(), "Error saving user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void startRoleActivity(String userRole) {
        Intent intent;
        // Add code to navigate to the appropriate activity based on the user's role
        switch (userRole) {
            case "HOD":
                intent = new Intent(getApplicationContext(), HodMainActivity.class);
                break;
            case "Faculty":
                intent = new Intent(getApplicationContext(), FacultyMainActivity.class);
                break;
            case "Admin":
                intent = new Intent(getApplicationContext(), AdminMainActivity.class);
                break;
            default:
                Toast.makeText(getApplicationContext(), "Unknown role: " + userRole, Toast.LENGTH_SHORT).show();
                return;
        }

        // If the user is logged in, directly start the appropriate activity
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        // Finish the current activity after starting the new activity
        finish();
    }
}
