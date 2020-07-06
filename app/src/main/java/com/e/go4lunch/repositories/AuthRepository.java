package com.e.go4lunch.repositories;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AuthRepository {

    private static final String COLLECTION_NAME = "workmates";
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkmatesCollection() {
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }


}
