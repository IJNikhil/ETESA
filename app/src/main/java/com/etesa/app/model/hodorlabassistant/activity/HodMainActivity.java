package com.etesa.app.model.hodorlabassistant.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.etesa.app.R;

public class HodMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hod_main);
        TextView txt = findViewById(R.id.textView);
        txt.setText("This is Hod Screen");
    }
}