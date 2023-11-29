package com.etesa.app.faculty;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.etesa.app.R;
import com.etesa.app.student.Student;
import com.etesa.app.student.StudentAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarkAbsentActivity extends AppCompatActivity {
    private MarkAbsentActivity markAbsentActivity = this;
    private String selectedClass, selectedSemester, selectedSubject, selectedDate;
    private static final String TAG = "MarkAbsentActivity";
    MaterialButton saveAttendanceButton;
    LinearProgressIndicator progressBar;
    private RecyclerView recyclerView;
    private StudentAdapter adapter;
    private List<Student> studentList = new ArrayList<>();
    private HashMap<String, Boolean> checkedRollNoMap = new HashMap<>(); // Initialize checkedRollNoMap in the MarkAbsentActivity constructor

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_absent);

        initIntent();

        initializeUIComponents();
        setInProgress(false);

        initializeStudentData();

        saveAttendanceButton.setOnClickListener(view -> submitAttendance(view));
    }
    private void initIntent() {
        // Extract intent data
        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");
        selectedSemester = intent.getStringExtra("selectedSemester");
        selectedSubject = intent.getStringExtra("selectedSubject");
        selectedDate = intent.getStringExtra("selectedDate");
    }

    void setInProgress(boolean inProgress) {
        if (inProgress) {
            progressBar.setVisibility(View.VISIBLE);
            saveAttendanceButton.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            saveAttendanceButton.setVisibility(View.VISIBLE);
        }
    }
    private void initializeUIComponents() {

        MaterialTextView mainText = findViewById(R.id.mainText);
        MaterialDivider divider = findViewById(R.id.divider);
        LinearLayout title = findViewById(R.id.title);
        MaterialTextView studentRollNoTitle = findViewById(R.id.studentRollNoTitle);
        MaterialTextView checkStudentTitle = findViewById(R.id.checkStudentTitle);
        saveAttendanceButton = findViewById(R.id.saveAttendance);
        progressBar = findViewById(R.id.progressBar);

        setupRecycler();
    }
    private void setupRecycler() {
        recyclerView = findViewById(R.id.rollNo_recycler_view);
        markAbsentActivity = this;
        // Set up recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new StudentAdapter(studentList, checkedRollNoMap, MarkAbsentActivity.this);
        recyclerView.setAdapter(adapter);
    }
    private void initializeStudentData() {
    // Set roll number range based on selected class
    int rollNoStart, rollNoEnd;
    switch (selectedClass) {
        case "SY" -> {
            rollNoStart = 1;
            rollNoEnd = 50;
        }
        case "TY" -> {
            rollNoStart = 51;
            rollNoEnd = 80;
        }
        case "BTech" -> {
            rollNoStart = 81;
            rollNoEnd = 131;
        }
        default -> {
            rollNoStart = 1;
            rollNoEnd = 50;
            break;
        }
    }

//     Generate student list
    for (int i = rollNoStart; i <= rollNoEnd; i++) {
        String rollNo = String.valueOf(i);
        Student student = new Student(rollNo, false);
        studentList.add(student);
        checkedRollNoMap.put(rollNo, false); // Update checkedRollNoMap for each student
        checkedRollNoMap.putIfAbsent(rollNo, false); // Update checkedRollNoMap for each student
    }
    runOnUiThread(() -> adapter.notifyDataSetChanged());
}
    public void markAbsent(String rollNumber, boolean isChecked) {
        synchronized (checkedRollNoMap) {
            if (checkedRollNoMap.containsKey(rollNumber)) {
                adapter.notifyItemChanged(studentList.indexOf(new Student(rollNumber, isChecked)));
            }
        }
    }
    public void submitAttendance(View view) {
        setInProgress(true);
        // Create custom document name with class + semester + subject + date
        String documentName = selectedClass + "_" + selectedSemester + "_" + selectedSubject + "_" + selectedDate;
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("attendance").document(documentName);
        // Create a HashMap to store checked roll numbers
        HashMap<String, Boolean> checkedRollNoMap = new HashMap<>();

        for (Student student : studentList) {
            checkedRollNoMap.put(student.getRollNumber(), student.isChecked());
        }
        // Create data to be stored
        HashMap<String, Object> data = new HashMap<>();
        data.put("checkedRollNoMap", checkedRollNoMap);

        // Add data to the document
        documentReference.set(data, SetOptions.merge()).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.d(TAG, "Attendance data stored successfully for document: " + documentName);
                Toast.makeText(MarkAbsentActivity.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();



                Intent intent = new Intent(getApplicationContext(), FacultyDashboardActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.e(TAG, "Failed to store attendance data: ", task.getException());
                Toast.makeText(MarkAbsentActivity.this, "Failed to mark attendance", Toast.LENGTH_SHORT).show();
            }
        });
    }
}