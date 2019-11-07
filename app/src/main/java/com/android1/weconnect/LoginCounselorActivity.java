package com.android1.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android1.weconnect.R.layout.activity_login_counciler;

public class LoginCounselorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = LoginActivity.class.getSimpleName();

    @BindView(R.id.iPasswordC) EditText counPasswordOl;
    @BindView(R.id.iEmailC) EditText counEmailOl;
    @BindView(R.id.loginButtonC) Button counLogin;
    @BindView(R.id.swapC) TextView counsignUp;
    @BindView(R.id.forgetC) TextView counresetPassword;
    @BindView(R.id.logCounsellor)RelativeLayout loggin;

    private FirebaseAuth authorized;

    private FirebaseAuth.AuthStateListener authorizedListener;

    private ProgressDialog mAuthProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(activity_login_counciler);

        ButterKnife.bind(this);


        loggin=(RelativeLayout)findViewById(R.id.logCounsellor);


        Animation logg= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        loggin.startAnimation(logg);


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

        String counPassword = counPasswordOl.getText().toString().trim();
        String counEmail = counEmailOl.getText().toString().trim();


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

