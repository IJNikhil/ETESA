package com.etesa.app.student;

import java.util.HashMap;
import java.util.Map;

public class Student {
    private static Map<String, Boolean> checkedRollNoMap = new HashMap<>();
    private String rollNumber;
    private boolean isChecked;
    private StudentAdapter adapter; // Reference to the adapter
    public Student(String rollNumber, boolean isChecked) {
        this.rollNumber = rollNumber;
        this.isChecked = isChecked;
    }
    // Getter methods for roll number, name, and isChecked
    public String getRollNumber() {
        return rollNumber;
    }
    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }
    public boolean isChecked() {
        return isChecked;
    }
    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        // Notify the adapter about the change in the dataset
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    // Setter method for the adapter
    public void setAdapter(StudentAdapter adapter) {
        this.adapter = adapter;
    }
}
