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

import androidx.lifecycle.ViewModelProviders;

import com.e.go4lunch.R;
import com.e.go4lunch.repositories.injection.Injection;
import com.e.go4lunch.repositories.injection.ViewModelFactory;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.ui.BaseActivity;
import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {

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

    // ----------------- FOR DATA -----------------
    FirebaseAuth mFireBaseAuth;
    private WorkmateViewModel mWorkmateViewModel;
    private List<Workmates> mWorkmatesList;
    private Boolean workmatesExists = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mFireBaseAuth = FirebaseAuth.getInstance();
        configureViewModel();
        subscribeObservers();

        mButtonRegister.setOnClickListener(v -> registerNewWorkmate());
    }

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

           mFireBaseAuth.createUserWithEmailAndPassword(email, password)
                   .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           subscribeObservers();
                           Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_Successfully), Toast.LENGTH_LONG).show();
                           mProgressBar.setVisibility(View.GONE);
                           startMapsActivity();
                       } else {
                           Toast.makeText(getApplicationContext(), Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                           mProgressBar.setVisibility(View.GONE);
                       }
                   });

    }
    // ----------------- Launch map activity -----------------
    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // ----------------- Configuring ViewModel -----------------
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkmateViewModel.class);

    }

    // ----------------- Configuring Observers -----------------
    private void subscribeObservers() {
        mWorkmateViewModel.getWorkmatesList().observe(this, workmates -> {
            mWorkmatesList = workmates;
            checkWorkmateExist();
        });

    }

    private void createWorkmates() {
        mWorkmateViewModel.createWorkmate(FirebaseAuth.getInstance().getCurrentUser().getUid(), mEmailRegister.getText().toString(), mNameRegister.getText().toString().trim(),
                null);
    }

    // ----------------- Check if workmates exists -----------------
    private void checkWorkmateExist() {
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
                    createWorkmates();
                    startMapsActivity();
                }
            }
        }

    }


}

