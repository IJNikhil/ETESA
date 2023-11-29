package com.etesa.app.viewreport;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class FirebaseViewUtil {
    private static DocumentReference getStudentsAttendanceDataFromFirestoreData(String dName) {
        return FirebaseFirestore.getInstance().collection("attendance").document(dName);
    }
}
