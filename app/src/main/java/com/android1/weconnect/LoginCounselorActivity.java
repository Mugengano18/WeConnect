package com.android1.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginCounselorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.counUserNameL) EditText counUsernameOl;
    @BindView(R.id.counPasswordL) EditText counPasswordOl;
    @BindView(R.id.counEmailL) EditText counEmailOl;
    @BindView(R.id.counLoginButton) Button counLogin;
    @BindView(R.id.counSwap) TextView counsignUp;
    @BindView(R.id.counforget) TextView counresetPassword;

    private FirebaseAuth authorized;

    private FirebaseAuth.AuthStateListener authorizedListener;

    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        authorized = FirebaseAuth.getInstance();
        createAuthProgressDialog();


        authorizedListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(LoginCounselorActivity.this, MessageActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };

        counLogin.setOnClickListener(this);
        counsignUp.setOnClickListener(this);
        counresetPassword.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        if (v == counLogin) {
            loginWithPassword();
        }

        if (v == counsignUp) {
            Intent intent1 = new Intent(LoginCounselorActivity.this, SignUpActivity.class);
            startActivity(intent1);
            finish();
        }

        if (v == counresetPassword) {
            Intent intent2 = new Intent(LoginCounselorActivity.this, ResetPasswordActivity.class);
            startActivity(intent2);
            finish();

        }
    }

    private void createAuthProgressDialog() {
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading...");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        authorized.addAuthStateListener(authorizedListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authorizedListener != null) {
            authorized.removeAuthStateListener(authorizedListener);
        }
    }

    private void loginWithPassword() {
        String counUsername = counUsernameOl.getText().toString().trim();
        String counPassword = counPasswordOl.getText().toString().trim();
        String counEmail = counEmailOl.getText().toString().trim();

        if (counUsername.equals("")) {
            counUsernameOl.setError("Please enter your username");
            return;
        }
        if (counPassword.equals("")) {
            counPasswordOl.setError("Password cannot be blank");
            return;
        }
        if (counEmail.equals("")) {
            counEmailOl.setError("Please enter your email");
            return;
        }
        mAuthProgressDialog.show();
        authorized.signInWithEmailAndPassword(counEmail, counPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        mAuthProgressDialog.dismiss();
                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginCounselorActivity.this, "Authentication successful.",
                                    Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginCounselorActivity.this, MessageActivity.class);
                            startActivity(intent);
                        }
                    }
                });


    }



}

