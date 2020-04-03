package com.e.go4lunch.auth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import android.widget.Button;

import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.R;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends AppCompatActivity  {

    //FOR DESIGN
    @BindView(R.id.btnGG)
    Button buttonloginGg;
    @BindView(R.id.btnFb)
    Button buttonFb;
    @BindView(R.id.btnTw)
    Button buttonTw;

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

    //FOR DATA
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);
        ButterKnife.bind(this); //Configure Butterknife
        loginGg();
    }
    // --------------------
    // ACTIONS
    // --------------------
    @OnClick(R.id.btnGG)
    public void loginGg() {
        // Launch Sign-In Activity when user clicked on Login Button
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        } else {
            this.startSignInActivity();
        }
    }

    @OnClick(R.id.btnFb)
    public void loginFb() {
        // Launch Sign-In Activity when user clicked on Login Button
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        } else {
            this.startSignInActivityFb();
        }

    }
    @OnClick(R.id.btnTw)
    public void loginTw(){
        if (this.isCurrentUserLogged()) {
            this.startMapsActivity();
        }else{
            this.startSignInActivityTw();
        }
    }

    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
                                Arrays.asList(
                                        new AuthUI.IdpConfig.GoogleBuilder().build()))//GOOGLE
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

}
