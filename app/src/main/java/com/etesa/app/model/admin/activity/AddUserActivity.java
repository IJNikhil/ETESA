package com.etesa.app.model.admin.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.etesa.app.R;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.etesa.app.utilMain.UserModelMain;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Objects;

public class AddUserActivity extends AppCompatActivity {
    private EditText addUserEmail, addUserPassword;
    private AutoCompleteTextView addUserRole;
    private String selectedUserRole;
    private  String uid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        addUserEmail = findViewById(R.id.addUserEmail);
        addUserPassword = findViewById(R.id.addUserpassword);
        TextInputLayout dropdown = findViewById(R.id.dropdown);
        addUserRole = findViewById(R.id.addUserRole);
        MaterialButton addUserButton = findViewById(R.id.addUserButton);

        // Populate the dropdown menu with user roles
        String[] userRoles = {"HOD", "Faculty"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, userRoles);
        addUserRole.setAdapter(adapter);

        // Set a listener to handle user selection
        addUserRole.setOnItemClickListener((parent, view, position, id) -> {
            // Get the selected user role
            selectedUserRole = parent.getItemAtPosition(position).toString();
        });

        addUserButton.setOnClickListener(view -> {
            // Get the user email and password from the text fields
            String userEmail = addUserEmail.getText().toString();
            String userPassword = addUserPassword.getText().toString();


            // Create a new user in Firebase Authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword)
                    .addOnSuccessListener(authResult -> {
                        uid = Objects.requireNonNull(authResult.getUser()).getUid();
                        // The user was created successfully

                        // Add the user role to the user's profile
                        authResult.getUser().updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(selectedUserRole)
                                        .build())
                                .addOnSuccessListener(task -> {
                                    // The user role was added successfully


                                    setData();
                                    // Clear the addUserEmail, addUserPassword, and addUserRole fields
                                    addUserEmail.setText("");
                                    addUserPassword.setText("");
                                    addUserRole.setText("");
                                    // Show a Snackbar to the user
                                    Snackbar.make(view, "User added successfully", Snackbar.LENGTH_SHORT).show();
                                })
                                .addOnFailureListener(e -> {
                                    // There was an error adding the user role
                                    Log.e("AddUserActivity", "Failed to add user role", e);
                                });
                    })
                    .addOnFailureListener(e -> {
                        // There was an error creating the user
                        Log.e("AddUserActivity", "Failed to create user", e);
                    });
        });

    }


    private void setData() {
        String addEmail = addUserEmail.getText().toString();
        String password = addUserPassword.getText().toString();
        String role = selectedUserRole;
        String userId = uid;

        FirebaseUtilMain.getCurrentUserDetails().get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Create a new UserModel object.
                UserModelMain userModel = new UserModelMain(role, addEmail, password, Timestamp.now(), userId);

                // Set the user details in the UserModel object.
                userModel.setUserRole(role);
                userModel.setUserEmail(addEmail);
                userModel.setUserId(userId);
                userModel.setUserPassword(password);
                userModel.setCreatedTimestamp(Timestamp.now());

                // Save the UserModel object in the database.
                FirebaseUtilMain.getCurrentUserDetails().set(userModel).addOnCompleteListener(task4 -> {
                    if (task4.isSuccessful()) {
                        Toast.makeText(this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Error saving the UserModel object to the database.
                        Toast.makeText(getApplicationContext(), "Error saving user data.", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                // Error getting user document from Firestore.
                Toast.makeText(getApplicationContext(), "Error retrieving role from Firestore.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
