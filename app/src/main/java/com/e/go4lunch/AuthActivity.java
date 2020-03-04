package com.e.go4lunch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

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
                                        new AuthUI.IdpConfig.Builder(AuthUI.GOOGLE_PROVIDER).build())) //GOOGLE
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
                                        new AuthUI.IdpConfig.Builder(AuthUI.FACEBOOK_PROVIDER).build())) //FACEBOOK
                        .build(),
                RC_SIGN_IN);
    }

}
