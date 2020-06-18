package com.e.go4lunch.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
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

import java.util.List;
import java.util.Objects;

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
    private List<Workmates> mWorkmatesList;


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
            }
        });
    }

    private void registerNewWorkmate() {
        mProgressBar.setVisibility(View.VISIBLE);

        String name, email, password;
        name = mNameRegister.getText().toString().trim();
        Log.e("testname", name);
        email = mEmailRegister.getText().toString();
        password = mPasswordRegister.getText().toString();


        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),  getResources().getString(R.string.Please_enter_email), Toast.LENGTH_LONG).show();
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

        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        createUser();
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Registered_Successfully), Toast.LENGTH_LONG).show();
                        mProgressBar.setVisibility(View.GONE);
                        startMapsActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        mProgressBar.setVisibility(View.GONE);
                    }
                });

    }


    private void startMapsActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    // Configuring ViewModel
    private void configureViewModel() {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(this);
        this.mWorkmateViewModel = ViewModelProviders.of(this, mViewModelFactory).get(WorkmateViewModel.class);

    }

    private void createUser() {
        String uid = FirebaseAuth.getInstance().getUid();
        String email = mEmailRegister.getText().toString();
        String name = mNameRegister.getText().toString();
        String urlPicture = null;
        mWorkmateViewModel.createWorkmate(uid, email, name, urlPicture);
    }

}

