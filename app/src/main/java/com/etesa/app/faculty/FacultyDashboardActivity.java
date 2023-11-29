package com.etesa.app.faculty;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.etesa.app.LoginActivity;
import com.etesa.app.R;
import com.etesa.app.viewreport.ViewReportActivity;
import com.etesa.app.utilMain.AndroidUtil;
import com.etesa.app.utilMain.UserModelMain;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.circularreveal.cardview.CircularRevealCardView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;

public class FacultyDashboardActivity extends AppCompatActivity {
    UserModelMain modelMain;
    View snackView;
    MaterialTextView viewReportTxt, markReportTxt, text;
    ImageView viewReportImg, markReportImg;
    MaterialButton logoutBtn;
    CircularRevealCardView markRecordCard, viewRecordCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        modelMain = AndroidUtil.getUserModelFromIntent(getIntent());

        setContentView(R.layout.activity_faculty_dashboard);
        initView();

        text.setText("Welcome, " + modelMain.getUserName());

        logoutBtn.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            loginScreen();
        });

        markRecordCard.setOnClickListener(view -> markReportScreen());
        viewRecordCard.setOnClickListener(view -> viewReportScreen());
    }

    private void viewReportScreen() {
        Intent intent = new Intent(getApplicationContext(), ViewReportActivity.class);
        startActivity(intent);
    }

    private void markReportScreen() {
        Intent intent = new Intent(getApplicationContext(), FacultyMarkReportActivity.class);
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
        markReportTxt = findViewById(R.id.markReportTxt);
        viewReportTxt = findViewById(R.id.viewReportTxt);
        markRecordCard = findViewById(R.id.markReport);
        viewRecordCard = findViewById(R.id.viewReport);
        markReportImg = findViewById(R.id.markReportImg);
        viewReportImg = findViewById(R.id.viewReportImg);
    }
}