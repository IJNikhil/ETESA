package com.etesa.app.student;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.etesa.app.R;
import com.etesa.app.faculty.MarkAbsentActivity;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.android.material.textview.MaterialTextView;

import java.util.HashMap;
public class StudentViewHolder extends RecyclerView.ViewHolder {
    private Student student;
    private MarkAbsentActivity markAbsentActivity;
    private HashMap<String, Boolean> checkedRollNoMap;
    public MaterialTextView rollNumberTextView;
    public MaterialCheckBox checkBox;
    public StudentViewHolder(View itemView, Student student, MarkAbsentActivity markAbsentActivity, HashMap<String, Boolean> checkedRollNoMap) {
        super(itemView);

        this.student = student;
        this.markAbsentActivity = markAbsentActivity;
        this.checkedRollNoMap = checkedRollNoMap;

        // Initialize rollNumberTextView and checkBox
        rollNumberTextView = itemView.findViewById(R.id.studentRollNo);
        checkBox = itemView.findViewById(R.id.checkStudent);
        
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> markAbsentActivity.markAbsent(student.getRollNumber(), isChecked));
    }

}

