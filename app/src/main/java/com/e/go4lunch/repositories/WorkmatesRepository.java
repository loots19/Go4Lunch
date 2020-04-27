package com.e.go4lunch.repositories;

import com.e.go4lunch.models.Workmates;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class WorkmatesRepository {


    private static final String COLLECTION_NAME = "workmates";
    private static  WorkmatesRepository instance;
    private CollectionReference workmateCollection;
    private Workmates mWorkmates;

    public static WorkmatesRepository getInstance(){
        if(instance == null){
            instance = new WorkmatesRepository();
        }
        return instance;
    }
    public WorkmatesRepository(){
        this.workmateCollection = getWorkmatesCollection();
    }


    // --- COLLECTION REFERENCE ---

    public static CollectionReference getWorkmatesCollection(){
        return FirebaseFirestore.getInstance().collection(COLLECTION_NAME);
    }

    // --- CREATE ---

    public static Task<Void> createWorkmates(String uid, String workmateName, String workmateMail, String urlPicture){
        Workmates workmatesToCreate = new Workmates(uid,workmateName,workmateMail,urlPicture);
        return WorkmatesRepository.getWorkmatesCollection().document(uid).set(workmatesToCreate);
    }

    // --- GET ---

    public static Task<DocumentSnapshot> getWorkmate(String uid){
        return WorkmatesRepository.getWorkmatesCollection().document(uid).get();
    }
    public static Task<QuerySnapshot> getAllWormateFromFirebase(){
        return WorkmatesRepository.getWorkmatesCollection().orderBy("username").get();
    }

    // --- UPDATE ---

    public static Task<Void> updateWorkmateName(String workmateName,String uid){
        return WorkmatesRepository.getWorkmatesCollection().document(uid).update("workmateName",workmateName);
    }

    public static Task<Void> updateUrlPicture(String urlPicture,String uid){
        return WorkmatesRepository.getWorkmatesCollection().document(uid).update("urlPicture",urlPicture);
    }

    // --- DELETE ---

    public static Task<Void> deleteWorkmateName(String uid){
        return WorkmatesRepository.getWorkmatesCollection().document(uid).delete();
    }




}
