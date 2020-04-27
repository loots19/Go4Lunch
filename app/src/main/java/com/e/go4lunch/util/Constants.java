package com.e.go4lunch.util;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.e.go4lunch.R;
import com.e.go4lunch.models.myPlace.Result;
import com.e.go4lunch.models.placeDetail.ResultDetail;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.ButterKnife;

public class Constants extends AppCompatActivity {

    public static final String BASE_URL = "https://maps.googleapis.com/maps/";
    public static final String API_KEY ="AIzaSyCbD-Ektsu_fCIS7YIU0G5BWic30ZXpDiA";
    public static final String BASE_URL_PHOTO = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
    public static final int MAX_WIDTH = 400;
    public static final int MAX_HEIGHT = 400;
    public static Result currentResult;


}
