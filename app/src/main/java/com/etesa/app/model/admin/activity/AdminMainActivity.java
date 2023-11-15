package com.etesa.app.model.admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.etesa.app.R;
import com.etesa.app.utilMain.FirebaseUtilMain;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.messaging.FirebaseMessaging;

public class AdminMainActivity extends AppCompatActivity {
    TextView addUserText;
    ImageView addUserImg;
    TextView viewUserText;
    ImageView viewUserImg;
    CircularRevealCardView addUserCardView;
    CircularRevealCardView viewUserCardView;
    DocumentReference userRef;
    String userRefPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_admin_main);
        addUserCardView = findViewById(R.id.addUserCard);
        addUserText = findViewById(R.id.addUserText);
        addUserImg = findViewById(R.id.addUserImg);
        viewUserCardView = findViewById(R.id.viewUserCard);
        viewUserText = findViewById(R.id.viewUserText);
        addUserImg = findViewById(R.id.viewUserImg);


        getFCMToken();

        addUserCardView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), AddUserActivity.class);
            startActivity(intent);
        });

        viewUserCardView.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), ViewUserActivity.class);
            startActivity(intent);
        });

    }



    private void getFCMToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                FirebaseUtilMain.getCurrentUserDetails().update("fcmToken", token);
            }
        } );
    }

}