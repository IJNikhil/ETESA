package com.etesa.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.etesa.app.faculty.FacultyMarkReportActivity;
import com.etesa.app.utilMain.AndroidUtil;
import com.etesa.app.utilMain.UserModelMain;
import com.etesa.app.viewreport.ViewReportActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class HodDashboard extends AppCompatActivity {
    UserModelMain modelMain;
    View snackView;
    MaterialTextView viewReportTxt, text;
    ImageView viewReportImg;
    MaterialButton logoutBtn;
    CircularRevealCardView viewRecordCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modelMain = AndroidUtil.getUserModelFromIntent(getIntent());

        setContentView(R.layout.activity_hod_dashboard);
        initView();

        text.setText("Welcome, " + modelMain.getUserName());

        logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            loginScreen();
        });

        viewRecordCard.setOnClickListener(view -> viewReportScreen());
    }

    private void viewReportScreen() {
        Intent intent = new Intent(getApplicationContext(), ViewReportActivity.class);
        startActivity(intent);
    }
    private void loginScreen() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();
    }
    private void initView() {
        text = findViewById(R.id.welcomeText);
        logoutBtn = findViewById(R.id.logoutBtn);
        viewReportTxt = findViewById(R.id.viewReportTxt);
        viewRecordCard = findViewById(R.id.viewReport);
        viewReportImg = findViewById(R.id.viewReportImg);
    }
}