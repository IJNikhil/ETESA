package com.etesa.app.model.faculty.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.etesa.app.R;

public class FacultyMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_main);
        TextView txt = findViewById(R.id.textView);
        txt.setText("This is Faculty Screen");
    }
}