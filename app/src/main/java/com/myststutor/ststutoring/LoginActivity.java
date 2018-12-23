package com.myststutor.ststutoring;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.data.model.User;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    FirebaseUser user;

    @Override
    public  void onStart (){
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        // Check for existing Google Sign In account, if the user is already signed in
// the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        final EditText emailEditText = findViewById(R.id.emaileditText);
        final EditText passwordEditText = findViewById(R.id.passwordeditText);

        final Button buttonLogin = findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                mAuth.signInWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d("Test", "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(LoginActivity.this, "Login successful.",
                                            Toast.LENGTH_SHORT).show();
                                    UserManager.user = user;
                                    startActivity(new Intent(LoginActivity.this, TutorProfileActivity.class));

                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w("Test", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Login failed.",
                                            Toast.LENGTH_SHORT).show();
                                }

                                // ...
                            }
                        });
            }
        });
        final Button buttonSignup = findViewById(R.id.buttonSignup);
        buttonSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View view) {
                if (!emailEditText.getText().toString().isEmpty() && !passwordEditText.getText().toString().isEmpty()) {
                    mAuth.createUserWithEmailAndPassword(emailEditText.getText().toString(), passwordEditText.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d("Test", "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(LoginActivity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();
                                        UserManager.user = user;
                                        startActivity(new Intent(LoginActivity.this, TutorProfileActivity.class));

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("Test", "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                }
                                // ...

                            });
                }

            }

        });
        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);
    }

}
