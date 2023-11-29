package com.etesa.app.student;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.etesa.app.R;
import com.etesa.app.faculty.MarkAbsentActivity;

import java.util.HashMap;
import java.util.List;


public class StudentAdapter extends RecyclerView.Adapter<StudentViewHolder> {
    private List<Student> students;
    private MarkAbsentActivity markAbsentActivity;
    private HashMap<String, Boolean> checkedRollNoMap;
    public StudentAdapter(List<Student> students, HashMap<String, Boolean> checkedRollNoMap, MarkAbsentActivity markAbsentActivity) {
        this.students = students;
        this.checkedRollNoMap = checkedRollNoMap; // Initialize checkedRollNoMap in the constructor
        this.markAbsentActivity = markAbsentActivity;

        if (checkedRollNoMap == null) {
            checkedRollNoMap = new HashMap<>();
        }
    }

    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int position = viewType;

        Student student = students.get(position);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_student_recycler_row, parent, false);
        return new StudentViewHolder(view, student, markAbsentActivity, checkedRollNoMap);
    }

    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {

            Student student = students.get(position);
            holder.rollNumberTextView.setText(student.getRollNumber());

            boolean isChecked = checkedRollNoMap.get(student.getRollNumber());
            holder.checkBox.setChecked(isChecked);
            holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked1) -> {
                student.setChecked(isChecked1); // Update the isChecked value in the Student object
                markAbsentActivity.markAbsent(student.getRollNumber(), isChecked1);
            });
    }

    @Override
    public int getItemCount() {
        return students.size();
    }
}
