package com.example.first_firebase.Activities;

import android.content.Intent;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.first_firebase.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    EditText emailEditText , passwordEditText;
    ProgressBar progressBar ;
    Button loginButton;
    TextView registerLink;
    private FirebaseAuth auth ;
    private Intent HomeActivity;
    private Intent RegisterActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.editText7);
        passwordEditText = findViewById(R.id.editText6);
        progressBar = findViewById(R.id.progressBar2);

        progressBar.setVisibility(View.INVISIBLE);

        loginButton=findViewById(R.id.loginButton);

        registerLink=findViewById(R.id.registerLinkTextView);

        auth=FirebaseAuth.getInstance();


        HomeActivity = new Intent(this, com.example.first_firebase.Activities.HomeActivity.class);
        RegisterActivity= new Intent(this, com.example.first_firebase.Activities.RegisterActivity.class);

        registerLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(RegisterActivity);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);

                final String email = emailEditText.getText().toString();
                final String password = passwordEditText.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    showMessage(" Invalid Input ");
                    loginButton.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else {
                    logIn(email,password);
                }
            }
        });

    }

    private void logIn(final String email, final String password) {

        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    progressBar.setVisibility(View.INVISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                    updateUserInfo();

                }else {
                    showMessage(task.getException().toString());
                }
            }
        });


    }

    private void updateUserInfo() {
        startActivity(HomeActivity);
        finish();

    }

    private void showMessage(String s) {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        if ( user != null){
            // user is already connected so we need to redirect
            updateUserInfo();

        }

    }
}
