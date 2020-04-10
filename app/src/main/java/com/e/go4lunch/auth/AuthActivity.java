package com.e.go4lunch.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.e.go4lunch.Retrofit.UserHelper;
import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity {

    //FOR DESIGN
    @BindView(R.id.btnGG)
    Button buttonloginGg;
    @BindView(R.id.btnFb)
    Button buttonFb;
    @BindView(R.id.btnTw)
    Button buttonTw;
    @BindView(R.id.btnLog)
    Button buttonLog;


    //FOR DATA
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);
        ButterKnife.bind(this); //Configure Butterknife
        alreadySigned();

    }

    // --------------------
    // ACTIONS
    // --------------------
    @OnClick(R.id.btnGG)
    public void loginGg() {
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        } else {
            this.startSignInActivity();
        }

    }

    @OnClick(R.id.btnFb)
    public void loginFb() {
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        } else {
            this.startSignInActivityFb();

        }

    }

    @OnClick(R.id.btnTw)
    public void loginTw() {
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        } else {
            this.startSignInActivityTw();
        }

    }

    @OnClick(R.id.btnLog)
    public void loginEmail() {
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        } else {
            this.startSignInActivityForMailPassword();
        }

    }


    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                this.createUserInFirestore();
                Toast.makeText(this, "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                startMapsActivity();
            } else { // ERRORS
                if (response == null) {
                    Toast.makeText(this, "error_authentication_canceled", Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "no_network", Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(this, "unknown_error", Toast.LENGTH_SHORT).show();


                }
            }
        }
    }
    // ------------------
    // NAVIGATION
    // ------------------

    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Arrays.asList(new AuthUI.IdpConfig.GoogleBuilder().build()))//GOOGLE
                        .build(),
                RC_SIGN_IN);

    }


    private void startSignInActivityFb() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.FacebookBuilder().build())) //FACEBOOK
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityTw() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.TwitterBuilder().build())) //FACEBOOK
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityForMailPassword() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Arrays.asList(
                                        new AuthUI.IdpConfig.EmailBuilder().build()))
                        .build(), RC_SIGN_IN);
    }

    // --------------------
    // REST REQUEST
    // --------------------

    // 1 - Http request that create user in firestore
    private void createUserInFirestore() {

        if (this.getCurrentUser() != null) {

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String workmateName = this.getCurrentUser().getDisplayName();
            String workmateEmail = this.getCurrentUser().getEmail();
            String uid = this.getCurrentUser().getUid();

            UserHelper.createWorkmates(uid, workmateName, workmateEmail, urlPicture).addOnFailureListener(this.onFailureListener());
        }
    }
    // --------------------
    // UTILS
    // --------------------

    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }

    protected OnFailureListener onFailureListener() {
        return new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "unknown_error", Toast.LENGTH_LONG).show();
            }
        };


    }
    public void alreadySigned(){
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        }
    }
}






