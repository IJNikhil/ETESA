package com.etesa.app.utilMain;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;

public class FirebaseUtilMain {
    public static String currentUserId() {
        return FirebaseAuth.getInstance().getUid();
    }
    public static DocumentReference getCurrentUserDetails() {
        return FirebaseFirestore.getInstance().collection("users").document(currentUserId());
    }
    public static boolean isLoggedIn() {
        if (currentUserId() != null) {
            return true;
        }
        return false;
    }

//    public static CollectionReference allUserCollectionReference() {
//        return FirebaseFirestore.getInstance().collection("users");
//    }

//    public static String timestampToString(Timestamp timestamp) {
//        return new SimpleDateFormat("hh :mm").format(timestamp.toDate());
//    }
}
