package com.e.go4lunch.auth;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class RegisterActivity extends AppCompatActivity {

    // ----------------- FOR DESIGN -----------------
    @BindView(R.id.et_name_register)
    EditText mNameRegister;
    @BindView(R.id.et_email_register)
    EditText mEmailRegister;
    @BindView(R.id.et_password_register)
    EditText mPasswordRegister;
    @BindView(R.id.button_register)
    Button mButtonRegister;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.textViewLogin)
    TextView mTextViewLogin;

    // ----------------- FOR DATA -----------------
    private WorkmateViewModel mWorkmateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);



        configureViewModel();
        registerWorkmate();

        mButtonRegister.setOnClickListener(v -> RegisterActivity.this.registerNewWorkmate());
        mTextViewLogin.setOnClickListener(v -> startLoginActivity());
    }

    // --------------------------------------------------------------------
    // --- get all input text check if is good and register in fireBase ---
    // --------------------------------------------------------------------
    private void registerNewWorkmate() {
        mProgressBar.setVisibility(View.VISIBLE);

        String name, email, password;
        name = mNameRegister.getText().toString().trim();
        email = mEmailRegister.getText().toString();
        password = mPasswordRegister.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_enter_email), Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailRegister.setError(getString(R.string.input_error_email_invalid));
            mEmailRegister.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_Successfully), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.Please_enter_name), Toast.LENGTH_LONG).show();
            return;
        }

        mWorkmateViewModel.register(email, password);


    }

    // -------------------------------------------------------
    // ----------------- Launch map activity -----------------
    // -------------------------------------------------------
    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // ---------------------------------------------------------
    // ----------------- Launch LogIn activity -----------------
    // ---------------------------------------------------------
    private void startLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // ---------------------------------------------------------
    // ----------------- Configuring ViewModel -----------------
    // ---------------------------------------------------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = new ViewModelProvider(this, mViewModelFactory).get(WorkmateViewModel.class);

    }

    // ---------------------------------
    // ----- Configuring Observers -----
    // ---------------------------------
    private void registerWorkmate() {
        mWorkmateViewModel.getUserLiveData().observe(this, fireBaseUser -> {
            if (fireBaseUser != null) {
                createWorkmates();
                startMapsActivity();
            }
        });
    }

    // ---------------------------------------
    // ----- Create workmate in fireBase -----
    // ---------------------------------------
    private void createWorkmates() {
        mWorkmateViewModel.createWorkmate(mEmailRegister.getText().toString(), mNameRegister.getText().toString().trim(),
                null);
    }

}

