package com.e.go4lunch.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.e.go4lunch.R;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.ui.BaseActivity;
import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.util.Constants;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthActivity extends BaseActivity {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.btnGG)
    Button buttonLoginGg;
    @BindView(R.id.btnFb)
    Button buttonFb;
    @BindView(R.id.btnTw)
    Button buttonTw;
    @BindView(R.id.btnLog)
    Button buttonLog;


    // ----------------- FOR DATA -----------------
    public static final int RC_SIGN_IN = 123;
    private WorkmateViewModel mWorkmateViewModel;
    private List<Workmates> mWorkmatesList;
    private Boolean workmatesExists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.auth_main);
        ButterKnife.bind(this);
        configureViewModel();
        subscribeObservers();
        initTwitter();

    }

    // -------------------
    // ----- ACTIONS -----
    // -------------------
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

    // -------------------------------
    // ----- Launch map activity -----
    // -------------------------------
    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


    // ----------------------
    // ----- NAVIGATION -----
    // ----------------------
    private void startSignInActivity() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Collections.singletonList(new AuthUI.IdpConfig.GoogleBuilder().build()))//GOOGLE
                        .build(),
                RC_SIGN_IN);

    }


    private void startSignInActivityFb() {
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setTheme(R.style.AppTheme)
                        .setAvailableProviders(
                                Collections.singletonList(
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
                                Collections.singletonList(
                                        new AuthUI.IdpConfig.TwitterBuilder().build())) //FACEBOOK
                        .build(),
                RC_SIGN_IN);
    }

    private void startSignInActivityForMailPassword() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    //------------------------
    //----- init twitter -----
    //------------------------
    public void initTwitter() {
        TwitterConfig config = new TwitterConfig.Builder(this)
                .logger(new DefaultLogger(Log.DEBUG))
                .twitterAuthConfig(new TwitterAuthConfig(Constants.CONSUMER_KEY, Constants.CONSUMER_SECRET))
                .debug(false)
                .build();
        Twitter.initialize(config);


    }

    // ---------------------------------
    // ----- Configuring ViewModel -----
    // ---------------------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = new ViewModelProvider(this, mViewModelFactory).get(WorkmateViewModel.class);

    }

    // --------------------------------
    // -----Configuring Observers -----
    // --------------------------------
    private void subscribeObservers() {
        mWorkmateViewModel.getAllWorkmates().observe(this, workmates -> {
            mWorkmatesList = workmates;
            checkWorkmateExists();
        });

    }


    // -------------------------------------
    // ----- Check if workmates exists -----
    // -------------------------------------
    private void checkWorkmateExists() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (mWorkmatesList != null) {
                int size = mWorkmatesList.size();
                for (int i = 0; i < size; i++) {
                    if (mWorkmatesList.get(i).getWorkmateEmail().equals(FirebaseAuth.getInstance().getCurrentUser().getEmail())) {
                        workmatesExists = true;
                        break;
                    }
                }
                if (workmatesExists) {
                    startMapsActivity();
                } else {
                    String email = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                    String name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
                    String urlPicture = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).toString();
                    mWorkmateViewModel.createWorkmate(email, name, urlPicture);
                }
            }

        }
    }


    // ------------------------------
    // ----- response of signIn -----
    // ------------------------------
    private void responseSignIn(int requestCode, int resultCode, Intent data) {
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "" + user.getEmail(), Toast.LENGTH_SHORT).show();
                    this.subscribeObservers();
                }
            } else {
                if (response == null) {
                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                } else if (Objects.requireNonNull(response.getError()).getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(getApplicationContext(), R.string.no_network, Toast.LENGTH_SHORT).show();
                } else if (response.getError().getErrorCode() == ErrorCodes.UNKNOWN_ERROR) {
                    Toast.makeText(getApplicationContext(), R.string.unknown_error, Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        this.responseSignIn(requestCode, resultCode, data);
    }


}








