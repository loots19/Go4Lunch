package com.e.go4lunch.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.go4lunch.R;
import com.e.go4lunch.injection.Injection;
import com.e.go4lunch.injection.ViewModelFactory;
import com.e.go4lunch.models.Workmates;
import com.e.go4lunch.ui.BaseActivity;
import com.e.go4lunch.ui.MainActivity;
import com.e.go4lunch.workmates.WorkmateViewModel;
import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends BaseActivity {

    //FOR DESIGN
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

    FirebaseAuth mFirebaseAuth;
    private WorkmateViewModel mWorkmateViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
        configureViewModel();

        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewWorkmate();
                createUserInFirestore();
            }
        });
    }

    private void registerNewWorkmate() {
        mProgressBar.setVisibility(View.VISIBLE);

        String name, email, password;
        name = mNameRegister.getText().toString().trim();
        email = mEmailRegister.getText().toString();
        password = mPasswordRegister.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mEmailRegister.setError(getString(R.string.input_error_email_invalid));
            mEmailRegister.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Please enter name!", Toast.LENGTH_LONG).show();
            return;
        }

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                            createUserInFirestore();

                            startMapsActivity();
                        } else {
                            Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }



    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    // --------------------
    // REST REQUEST
    // --------------------

    //  Http request that create user in firestore
    private void createUserInFirestore() {

        if (this.getCurrentUser() != null) {

            String urlPicture = (this.getCurrentUser().getPhotoUrl() != null) ? this.getCurrentUser().getPhotoUrl().toString() : null;
            String workmateName = this.getCurrentUser().getDisplayName();
            String workmateEmail = this.getCurrentUser().getEmail();
            String uid = this.getCurrentUser().getUid();
            mWorkmateViewModel.createWorkmate(uid, workmateEmail, workmateName, urlPicture);

            Log.e("name", workmateName);
            Log.e("email", workmateEmail);
            Log.e("uid", uid);


        }
    }

    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkmateViewModel.class);
    }

}