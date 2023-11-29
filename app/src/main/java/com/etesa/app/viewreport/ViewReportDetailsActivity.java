package com.etesa.app.viewreport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.etesa.app.R;

import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewReportDetailsActivity extends AppCompatActivity {

    private List<ViewReport> allStudentList = new ArrayList<>();
    private List<ViewReport> presentStudentList = new ArrayList<>();
    private List<ViewReport> absentStudentList = new ArrayList<>();
    private ViewAdapter adapter;
    private String selectedClass, selectedSemester, selectedSubject, selectedDate, selectedView;
    private Map<String, Boolean> checkedRollNoMap;


    ViewReportDetailsActivity viewReportDetailsActivity = this;
    TextInputLayout viewSelectionLayout;
    MaterialAutoCompleteTextView selectView;
    private static final String TAG = "ViewReportDetailsActivity";
    String[] viewList = {"All", "All Present", "All Absent"};
    ArrayAdapter<String> setViewAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report_details);

        initIntent();
        initializeUIComponents();
        initAdapter();
        setupRecycler();
        getData();
    }

    private void initIntent() {
        Intent intent = getIntent();
        selectedClass = intent.getStringExtra("selectedClass");
        selectedSemester = intent.getStringExtra("selectedSemester");
        selectedSubject = intent.getStringExtra("selectedSubject");
        selectedDate = intent.getStringExtra("selectedDate");
    }

    private void getData() {
        String documentName = selectedClass + "_" + selectedSemester + "_" + selectedSubject + "_" + selectedDate;

        // Get the document reference for the attendance document
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("attendance").document(documentName);

        documentReference.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if (documentSnapshot.exists()) {
                    // Get the checkedRollNoMap from the document
                    checkedRollNoMap = (Map<String, Boolean>) documentSnapshot.get("checkedRollNoMap");

                    // Process attendance data
                    processAttendanceData();
                } else {
                    // Handle no-document scenario
                    Toast.makeText(getApplicationContext(), "Attendance data not found for selected class, semester, subject, and date", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle error
                Log.e(TAG, "Failed to retrieve attendance data", task.getException());
                Toast.makeText(getApplicationContext(), "Error retrieving attendance data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void processAttendanceData() {
        allStudentList.clear();
        presentStudentList.clear();
        absentStudentList.clear();

        if (checkedRollNoMap != null) {
            for (String rollNumber : checkedRollNoMap.keySet()) {
                boolean isPresent = checkedRollNoMap.get(rollNumber);
                ViewReport viewReport = new ViewReport(rollNumber, isPresent);

                allStudentList.add(viewReport);

                if (isPresent) {
                    presentStudentList.add(viewReport);
                } else {
                    absentStudentList.add(viewReport);
                }
            }
        }

        // Sort allStudentList based on roll number
        Collections.sort(allStudentList, (viewReport1, viewReport2) -> {
            String rollNumber1 = viewReport1.getRollNumber();
            String rollNumber2 = viewReport2.getRollNumber();

            if (rollNumber1 == null && rollNumber2 == null) {
                return 0; // Both roll numbers are null, so they are equal
            } else if (rollNumber1 == null) {
                return 1; // Roll number2 is not null, so it comes after roll number1
            } else if (rollNumber2 == null) {
                return -1; // Roll number1 is not null, so it comes before roll number2
            } else {
                try {
                    int rollNumber1Int = Integer.parseInt(rollNumber1);
                    int rollNumber2Int = Integer.parseInt(rollNumber2);
                    return rollNumber1Int - rollNumber2Int;
                } catch (NumberFormatException e) {
                    // Invalid roll number format, consider it after valid roll numbers
                    return 1;
                }
            }
        });

        // Sort presentStudentList based on roll number
        Collections.sort(presentStudentList, (viewReport1, viewReport2) -> {
            String rollNumber1 = viewReport1.getRollNumber();
            String rollNumber2 = viewReport2.getRollNumber();

            if (rollNumber1 == null && rollNumber2 == null) {
                return 0; // Both roll numbers are null, so they are equal
            } else if (rollNumber1 == null) {
                return 1; // Roll number2 is not null, so it comes after roll number1
            } else if (rollNumber2 == null) {
                return -1; // Roll number1 is not null, so it comes before roll number2
            } else {
                try {
                    int rollNumber1Int = Integer.parseInt(rollNumber1);
                    int rollNumber2Int = Integer.parseInt(rollNumber2);
                    return rollNumber1Int - rollNumber2Int;
                } catch (NumberFormatException e) {
                    // Invalid roll number format, consider it after valid roll numbers
                    return 1;
                }
            }
        });

        // Sort absentStudentList based on roll number
        Collections.sort(absentStudentList, new Comparator<ViewReport>() {
            @Override
            public int compare(ViewReport viewReport1, ViewReport viewReport2) {
                int rollNumber1 = Integer.parseInt(viewReport1.getRollNumber());
                int rollNumber2 = Integer.parseInt(viewReport2.getRollNumber());
                return rollNumber1 - rollNumber2;
            }
        });
    }



    private void initializeUIComponents() {
        MaterialTextView mainText = findViewById(R.id.mainText);
        MaterialDivider divider = findViewById(R.id.divider);
        LinearLayout title = findViewById(R.id.title);
        MaterialTextView studentRollNoTitle = findViewById(R.id.studentRollNoTitle);
        MaterialTextView viewStudentAttendanceTitle = findViewById(R.id.viewStudentAttendanceTitle);
        viewSelectionLayout = findViewById(R.id.viewSelectionLayout);
        selectView = findViewById(R.id.selectView);
    }

    private void initAdapter() {
        setViewAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, viewList);
        selectView.setAdapter(setViewAdapter);

        selectView.setOnItemClickListener((parent, view, position, id) -> {
            selectedView = parent.getItemAtPosition(position).toString();
            selectView.setText(selectedView);

            // Update data based on selected view
            if (selectedView.equals("All")) {
                adapter.setData(allStudentList);
            } else if (selectedView.equals("All Present")) {
                adapter.setData(presentStudentList);
            } else if (selectedView.equals("All Absent")) {
                adapter.setData(absentStudentList);
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.rollNo_recycler_view_data);
        adapter = new ViewAdapter(); // Create an instance of the ViewAdapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter); // Set the adapter for the RecyclerView
    }

}
