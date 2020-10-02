package com.e.go4lunch.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.e.go4lunch.R;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.workmates.WorkmateViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.et_email_login)
    EditText mEmail;
    @BindView(R.id.et_password_login)
    EditText mPassword;
    @BindView(R.id.button_login)
    Button mButton;
    @BindView(R.id.progressBarLogin)
    ProgressBar mProgressBar;

    // ----------------- FOR DATA -----------------
    private WorkmateViewModel mWorkmateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        configureViewModel();
        LoginWorkmate();

        mButton.setOnClickListener(v -> LogIn());
    }

    // -------------------------------------
    // ----- authenticate the workmate -----
    // -------------------------------------
    private void LogIn() {
        mProgressBar.setVisibility(View.VISIBLE);
        String email, password;
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_enter_email), Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmail.setError(getString(R.string.input_error_email_invalid));
            mEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_Successfully), Toast.LENGTH_LONG).show();
        }
        mWorkmateViewModel.logIn(email, password);

    }

    // ---------------------------------
    // ----- Configuring ViewModel -----
    // ---------------------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = new ViewModelProvider(this, mViewModelFactory).get(WorkmateViewModel.class);

    }

    // ---------------------------------
    // ----- Configuring Observers -----
    // ---------------------------------
    private void LoginWorkmate() {
        mWorkmateViewModel.getUserLiveData().observe(this, fireBaseUser -> {
            if (fireBaseUser != null) {
                startMapsActivity();
            }
        });
    }

    // -------------------------------
    // ----- Launch map activity -----
    // -------------------------------
    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }


}
