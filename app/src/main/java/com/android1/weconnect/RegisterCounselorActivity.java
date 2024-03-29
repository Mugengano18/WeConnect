package com.android1.weconnect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterCounselorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG=RegisterCounselorActivity.class.getSimpleName();

    @BindView(R.id.counEmail) EditText emailCon;
    @BindView(R.id.occupation)EditText occupation;
    @BindView(R.id.Country)
    Spinner country;
    @BindView(R.id.counUsername)EditText usernameCon;
    @BindView(R.id.counPasswordI)EditText passwordCon;
    @BindView(R.id.iPassword1C)EditText comfirmPasswordCon;
    @BindView(R.id.counsellorsignup)
    Button submission;
    @BindView(R.id.checkC) CheckBox verifyCon;
    @BindView(R.id.counselor)RelativeLayout login;

    private FirebaseAuth authorized;
    private FirebaseAuth.AuthStateListener authorizedThListener;
    private ProgressDialog authorizedProgressDialog;

    private String counselorName;
    DatabaseReference consellor1;
    Counsellor counsellor2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_counciler);
        consellor1= FirebaseDatabase.getInstance().getReference("Counsellors");

        ButterKnife.bind(this);


        login=(RelativeLayout)findViewById(R.id.counselor);


        Animation logg= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.bounce);
        login.startAnimation(logg);

        authorized=FirebaseAuth.getInstance();
        createAuthStateListener();
        createAuthProgressDialog();
        submission.setOnClickListener(this);


    }

    private void createAuthProgressDialog() {

        authorizedProgressDialog = new ProgressDialog(this);
        authorizedProgressDialog.setTitle("Loading...");
        authorizedProgressDialog.setMessage("Authenticating with Firebase...");
        authorizedProgressDialog.setCancelable(true);
    }


    @Override
    public void onClick(View v) {

        if(v == submission){
            if(verifyCon.isChecked()){
                createNewAccount();
                String name=usernameCon.getText().toString().trim();
                String country2=country.getSelectedItem().toString();
                String email=emailCon.getText().toString().trim();
                String occup=occupation.getText().toString().trim();
                if (!TextUtils.isEmpty(name)){
                     String id = consellor1.push().getKey();
                    Counsellor counsellor=new Counsellor(country2,email,name,occup);
                    consellor1.child(id).setValue(counsellor);
                    Toast.makeText(this,"counsellor added",Toast.LENGTH_LONG);
                }
                else {
                    Toast.makeText(this,"enter a name",Toast.LENGTH_LONG);
                }
            }
            else{
                Animation clignote= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.blink);
                verifyCon.startAnimation(clignote);
            }
        }
    }

    private void createNewAccount() {

        final String counselorUsername=usernameCon.getText().toString().trim();
        final String counselorEmail = emailCon.getText().toString().trim();
//        final String counselorRegNumber=regNumber.getText().toString().trim();
        String counpassword = passwordCon.getText().toString().trim();
        String counconfirmPassword = comfirmPasswordCon.getText().toString().trim();
        counselorName=usernameCon.getText().toString().trim();


        boolean validEmail=isValidEmail(counselorEmail);
        boolean validUserName=isValidUserName(counselorUsername);
//        boolean validRegNumber=isValidRegistNumber(counselorRegNumber);
        boolean validPassword=isValidPassword(counpassword,counconfirmPassword);

        if(!validEmail||!validUserName||!validPassword)return;

        authorizedProgressDialog.show();


        authorized.createUserWithEmailAndPassword(counselorEmail, counpassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                           Log.d(TAG,"Authentication is successful;");
                        } else {
                            Toast.makeText(RegisterCounselorActivity.this,
                                    "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }




    private void createAuthStateListener() {
        authorizedThListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(RegisterCounselorActivity.this, savedCouns.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

        };
    }

    private boolean isValidEmail(String counselorEmail) {
        boolean isGoodEmail =
                (counselorEmail != null && android.util.Patterns.EMAIL_ADDRESS.matcher(counselorEmail).matches());
        if (!isGoodEmail) {
            emailCon.setError("Please enter a valid email address");
            return false;
        }
        return isGoodEmail;
    }

    private boolean isValidUserName(String counselorName) {
        if (counselorName.equals("")) {
            usernameCon.setError("Please enter your username");
            return false;
        }
        return true;
    }

    private boolean isValidPassword(String password, String confirmPassword) {
        if (password.length() < 6) {
            passwordCon.setError("Please create a password containing at least 6 characters");
            return false;
        } else if (!password.equals(confirmPassword)) {
            passwordCon.setError("Passwords do not match");
            return false;
        }
        return true;
    }

//    private boolean isValidRegistNumber(String counselorRegNumber) {
//        if(!counselorRegNumber.contains("RHPA")){
//            regNumber.setError("Please enter valid Registration number");
//            return false;
//        }
//        return true;
//    }

    private void createFirebaseUserProfile(final FirebaseUser user) {

        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(counselorName).build();

        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RegisterCounselorActivity.this, "The displayed username has been set", Toast.LENGTH_LONG).show();

                        }
                    }

                });
    }
    @Override
    public void onStart() {
        super.onStart();
        authorized.addAuthStateListener(authorizedThListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authorizedThListener != null) {
            authorized.removeAuthStateListener(authorizedThListener);
        }
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
