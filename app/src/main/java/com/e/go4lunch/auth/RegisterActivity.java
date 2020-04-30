package com.e.go4lunch.auth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.e.go4lunch.R;
import com.e.go4lunch.ui.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    //FOR DESIGN
    @BindView(R.id.et_email_register)
    EditText mEmailRegister;
    @BindView(R.id.et_password_register)
    EditText mPasswordRegister;
    @BindView(R.id.button_register)
    Button mButtonRegister;
    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        mFirebaseAuth = FirebaseAuth.getInstance();



        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewWorkmate();
            }
        });
    }

    private void registerNewWorkmate() {
        mProgressBar.setVisibility(View.VISIBLE);

        String email, password;
        email = mEmailRegister.getText().toString();
        password = mPasswordRegister.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Please enter email...", Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Please enter password!", Toast.LENGTH_LONG).show();
            return;
        }
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);

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
        // UTILS
        // --------------------




    @Nullable
    protected FirebaseUser getCurrentUser() {
        return FirebaseAuth.getInstance().getCurrentUser();
    }

    protected Boolean isCurrentUserLogged() {
        return (this.getCurrentUser() != null);
    }
}
