package com.etesa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.etesa.app.faculty.FacultyDashboardActivity;
import com.etesa.app.utilMain.AndroidUtil;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.List;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {
    TextInputEditText userCreateEmail, userCreatePass, userCnfCreatePass, userNameInput;
    MaterialAutoCompleteTextView addUserRole;
    MaterialButton createUserBtn;
    String selectedUserRole, userEmail, userPass, userCnfPass, userName, role;
    String uid = "";
    MaterialTextView loginTextBtn;
    View sView;
    UserModelMain model, modelMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        initView();
        sView = new View(getApplicationContext());

        String[] userRoles = {"HOD", "Faculty"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, userRoles);
        addUserRole.setAdapter(adapter);

        addUserRole.setOnItemClickListener((parent, view, position, id) -> {
            selectedUserRole = parent.getItemAtPosition(position).toString();
        });

        createUserBtn.setOnClickListener(view -> createUser());

        loginTextBtn.setOnClickListener(view -> loginUserScreen());
    }
    private void createUser() {
        // Validate user input
        userEmail = userCreateEmail.getText().toString().trim();
        userPass = userCreatePass.getText().toString().trim();
        userCnfPass = userCnfCreatePass.getText().toString().trim();
        userName = userNameInput.getText().toString().trim();
        role = selectedUserRole;

        if (userEmail.isEmpty()) {
            AndroidUtil.showSnack(sView, "Enter all details");
        } else if (userPass.isEmpty()) {
            userCreatePass.setError("Enter password");
        } else if (userPass.length() < 6) {
            userCreatePass.setError("Password must be more than 6 characters");
        } else if (!userCnfPass.equals(userPass)) {
            userCnfCreatePass.setError("Enter correct password");
        } else if (role.isEmpty()) {
            addUserRole.setError("select role");
        } else if (userName.isEmpty() || userName.length() < 3) {
            userNameInput.setError("Enter username");
        } else {
            firebaseFetchUser();
        }
    }

    private void firebaseFetchUser() {
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(userEmail).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (signInMethods.isEmpty()) {
                    createUserOnFirebase(); // User does not exist, proceed with creating a new user
                } else {
                    Toast.makeText(this, "User already registered", Toast.LENGTH_SHORT).show();
                    loginUserScreen();
                }
            } else { // Error fetching sign-in methods
                Log.e("AddUserActivity", "Failed to fetch sign-in methods", task.getException());
            }
        });
    }

    private void createUserOnFirebase() {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userCnfPass).addOnSuccessListener(authResult -> {
            uid = Objects.requireNonNull(authResult.getUser()).getUid();

            authResult.getUser().updateProfile(new UserProfileChangeRequest
                    .Builder().setDisplayName(selectedUserRole)
                    .build()).addOnSuccessListener(runnable -> {
                // User profile updated successfully, proceed with data storage
                setData(userName, role, userEmail, userPass, uid);

                Intent intent = new Intent(getApplicationContext(), FacultyDashboardActivity.class);
                AndroidUtil.passUserModelAsIntent(intent, model);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }).addOnFailureListener(e -> {
                Log.e("AddUserActivity", "Failed to add user", e);
            });
        }).addOnFailureListener(e -> { // Error creating user
            Log.e("AddUserActivity", "Failed to create user", e);
        });
    }


    private void setData(String userName, String role, String userEmail, String userPass, String uid) {
        model = new UserModelMain(userName, role, userEmail, userPass, uid, Timestamp.now());
        modelMain.setUserName(userName);
        modelMain.setUserRole(role);
        modelMain.setUserEmail(userEmail);
        modelMain.setUserPassword(userPass);
        modelMain.setUserId(uid);
        modelMain.setTimestamp(Timestamp.now());

        FirebaseUtilMain.getCurrentUserDetails().set(model).addOnCompleteListener(task1 -> {
            if (task1.isSuccessful()) {
                Toast.makeText(this, "Added data Successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Error saving user data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUserScreen() {
    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
    startActivity(intent);
}
    private void initView() {
        userCreateEmail = findViewById(R.id.userCreateEmail);
        userCreatePass = findViewById(R.id.userCreatePass);
        userCnfCreatePass = findViewById(R.id.userCnfCreatePass);
        userNameInput = findViewById(R.id.userName);
        addUserRole = findViewById(R.id.addUserRole);
        createUserBtn = findViewById(R.id.createUserBtn);
        loginTextBtn = findViewById(R.id.loginTextBtn);
    }
}