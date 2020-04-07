package com.e.go4lunch.Retrofit;

import com.e.go4lunch.models.Workmates;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserHelper {

    private static final String COLLECTION_NAME = "users";

    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkmatesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createWorkmates(String uid, String workmateName, String workmateMail, String urlPicture){
        Workmates workmatesToCreate = new Workmates(uid,workmateName,workmateMail,urlPicture);
        return UserHelper.getWorkmatesCollection().document(uid).set(workmatesToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getWorkmate(String uid){
        return UserHelper.getWorkmatesCollection().document(uid).get();
    }

    // --- UPDATE ---

    public static Task<Void> updateWorkmateName(String workmateName,String uid){
        return UserHelper.getWorkmatesCollection().document(uid).update("workmateName",workmateName);
    }

    public static Task<Void> updateUrlPicture(String urlPicture,String uid){
        return UserHelper.getWorkmatesCollection().document(uid).update("urlPicture",urlPicture);
    }

    // --- DELETE ---

    public static Task<Void> deleteWorkmateName(String uid){
        return UserHelper.getWorkmatesCollection().document(uid).delete();
    }

}