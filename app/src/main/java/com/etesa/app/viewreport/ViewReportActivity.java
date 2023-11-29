package com.etesa.app.viewreport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.etesa.app.R;
import com.etesa.app.faculty.MarkAbsentActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class ViewReportActivity extends AppCompatActivity {
    MaterialAutoCompleteTextView selectClass, selectSubject, selectSemester, selectDate;
    MaterialButton next;
    ArrayAdapter<String> classAdapter, semesterAdapter, subjectAdapter;
    String[] classList, semesterList, subjectList;
    String selectedClass, selectedSemester, selectedSubject, selectedDate;
    String[] sem3Subject = {"EM-3", "EDC", "DE", "EMI", "Aptitude", "Soft Skill"};
    String[] sem4Subject = {"NT", "SS", "BHR", "PTRP", "Python"};
    String[] sem5Subject = {"ACM", "DSP", "ESD", "EFT", "CSE", "Mini Project"};
    String[] sem6Subject = {"AWP", "DC", "MPMC", "Computer network", "ESD"};
    String[] sem7Subject = {"ME", "EWM", "MC", "TNP", "Foreign language ", "FOC", "FM", "Mini Project"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        initViews();
        initAdapter();

        if (next == null) {
            // Handle the null reference
        } else {
            next.setOnClickListener(view -> {
                // Implement button click logic
                if (selectedClass.isEmpty() || selectedSemester.isEmpty() || selectedSubject.isEmpty() || selectedDate.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please select all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Navigate to MarkAbsentActivity
                    Intent intent = new Intent(getApplicationContext(), ViewReportDetailsActivity.class);
                    intent.putExtra("selectedClass", selectedClass);
                    intent.putExtra("selectedSemester", selectedSemester);
                    intent.putExtra("selectedSubject", selectedSubject);
                    intent.putExtra("selectedDate", selectedDate);
                    startActivity(intent);
                }
            });
        }
    }

    private void initAdapter() {
        classList = new String[]{"SY", "TY", "BTech"};
        classAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, classList);
        selectClass.setAdapter(classAdapter);

        selectClass.setOnItemClickListener((parent, view, position, id) -> {
            selectedClass = parent.getItemAtPosition(position).toString();

            switch (selectedClass) {
                case "SY":
                    semesterList = new String[]{"Semester 3", "Semester 4"};
                    break;
                case "TY":
                    semesterList = new String[]{"Semester 5", "Semester 6"};
                    break;
                case "BTech":
                    semesterList = new String[]{"Semester 7"};
                    break;
            }

            semesterAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, semesterList);
            selectSemester.setAdapter(semesterAdapter);
            semesterAdapter.notifyDataSetChanged();
        });

        selectSemester.setOnItemClickListener((parent, view, position, id) -> {
            selectedSemester = parent.getItemAtPosition(position).toString();

            switch (selectedSemester) {
                case "Semester 3":
                    subjectList = sem3Subject;
                    break;
                case "Semester 4":
                    subjectList = sem4Subject;
                    break;
                case "Semester 5":
                    subjectList = sem5Subject;
                    break;
                case "Semester 6":
                    subjectList = sem6Subject;
                    break;
                case "Semester 7":
                    subjectList = sem7Subject;
                    break;
            }

            subjectAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, subjectList);
            selectSubject.setAdapter(subjectAdapter);
            subjectAdapter.notifyDataSetChanged();
        });

        selectSubject.setOnItemClickListener((parent, view, position, id) -> {
            selectedSubject = parent.getItemAtPosition(position).toString();
            selectSubject.setText(selectedSubject);
        });
    }

    private void initViews() {

        selectClass = findViewById(R.id.selectClass);
        selectSubject = findViewById(R.id.selectSubject);
        selectSemester = findViewById(R.id.selectSemester);
        selectDate = findViewById(R.id.selectDate);
        next = findViewById(R.id.next);

        selectDate.setOnClickListener(v -> {
            // Create a new instance of DatePickerDialog
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    ViewReportActivity.this,
                    (view, year, monthOfYear, dayOfMonth) -> {
                        selectedDate  = DateFormat.getDateInstance().format(new Date(year - 1900, monthOfYear, dayOfMonth));
                        selectDate.setText(selectedDate);
                    },
                    Calendar.getInstance().get(Calendar.YEAR), // Set the initial year
                    Calendar.getInstance().get(Calendar.MONTH), // Set the initial month
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH) // Set the initial day
            );
            // Show the DatePickerDialog
            datePickerDialog.show();
        });
    }
}