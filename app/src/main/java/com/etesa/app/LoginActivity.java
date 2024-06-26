package com.etesa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.etesa.app.faculty.FacultyDashboardActivity;
import com.etesa.app.utilMain.AndroidUtil;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText userEmailInput, userPassInput;
    MaterialButton loginBtn;
    MaterialTextView createAcTextBtn;
    private String email, pass;
    LinearProgressIndicator progressBar;
    private FirebaseAuth mAuth;
    UserModelMain model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        initView();

        setInProgress(false);
        createAcTextBtn.setOnClickListener(view -> createUserScreen());

        loginBtn.setOnClickListener(view -> checkCredential());
    }
    private void checkCredential() {
        email = userEmailInput.getText().toString().trim();
        pass = userPassInput.getText().toString().trim();

        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Enter correct details", Toast.LENGTH_SHORT).show();
        } else {
            fetchUserOnFirebase(email);
        }
    }
    private void fetchUserOnFirebase(String email) {
        setInProgress(true);
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> signInMethods = task.getResult().getSignInMethods();
                if (!signInMethods.isEmpty()) {
                    firebaseSignInProcess(); // User exists, proceed with login
                } else {
                    Toast.makeText(this, "User not registered. Please create an account.", Toast.LENGTH_SHORT).show();
                    createUserScreen();
                }
            } else {
                Toast.makeText(this, "Error checking user existence", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void firebaseSignInProcess() {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(authResultTask -> {
            if (authResultTask.isSuccessful()) {
                retrieveUserData(); // User logged in successfully, proceed with retrieving user data
            } else {
                Toast.makeText(this, "Incorrect email or password. Please try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void retrieveUserData() {
        DocumentReference userDocRef = FirebaseUtilMain.getCurrentUserDetails();

        userDocRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();

                String userName = documentSnapshot.getString("userName");
                String userRole = documentSnapshot.getString("userRole");
                String userEmail = documentSnapshot.getString("userEmail");
                String userPass = documentSnapshot.getString("userPass");
                String userId = documentSnapshot.getString("userId");

                updateUser(userName, userRole, userEmail, userPass, userId);
            } else {
                Toast.makeText(this, "Error retrieving user data from Firestore.", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateUser(String userName, String userRole, String userEmail, String userPass, String userId) {
        UserModelMain userModel = new UserModelMain(userName, userRole, userEmail, userPass, userId, Timestamp.now());
        userModel.setUserName(userName);
        userModel.setUserRole(userRole);
        userModel.setUserEmail(userEmail);
        userModel.setUserPassword(userPass);
        userModel.setUserId(userId);
        userModel.setTimestamp(Timestamp.now());

        DocumentReference userDocRef = FirebaseUtilMain.getCurrentUserDetails();

        userDocRef.set(userModel).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                UserModelMain model = task.getResult().toObject(UserModelMain.class);
                String rRole = userModel.getUserRole();
                if (Objects.equals(rRole, "HOD")) {
                    facultyDashboardScreen(userModel);
                } else if (Objects.equals(rRole, "Faculty")) {
                    hodDashboardScreen(model);
                }
                facultyDashboardScreen(userModel); // User data updated successfully, proceed to faculty dashboard
            } else {
                Toast.makeText(this, "Error saving user data to Firestore.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void facultyDashboardScreen(UserModelMain model) {
        Intent intent = new Intent(getApplicationContext(), FacultyDashboardActivity.class);
//        intent.putExtra(userName, "userName");
        AndroidUtil.passUserModelAsIntent(intent, model);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

    }
    private void hodDashboardScreen(UserModelMain model) {
        Intent intent = new Intent(getApplicationContext(), HodDashboard.class);
//        intent.putExtra(userName, "userName");
        AndroidUtil.passUserModelAsIntent(intent, model);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
    private void createUserScreen() {
        Intent intent = new Intent(getApplicationContext(), CreateAccountActivity.class);
        startActivity(intent);
    }
    private void initView() {
        userEmailInput = findViewById(R.id.userEmail);
        userPassInput = findViewById(R.id.userPass);
        loginBtn = findViewById(R.id.loginBtn);
        createAcTextBtn = findViewById(R.id.createAcTextBtn);
        progressBar = findViewById(R.id.progressBar);
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            loginBtn.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginBtn.setVisibility(View.VISIBLE);
        }
    }
}