package com.example.DELL.new_blood.SMSActivities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.DELL.new_blood.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyNumberActivity extends AppCompatActivity {

    public static final String TAG = "VerifyNumberActivity";

    EditText verifyNumberEditText;
    Button verifyButton;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    PhoneAuthProvider.ForceResendingToken mResendToken;
    String mVerificationId;

    FirebaseAuth mAuth;

    String phoneNumber = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_number_activity);

        verifyNumberEditText = findViewById(R.id.verifyNumberEditText);
        verifyButton = findViewById(R.id.verifyButton);

        verifyButton.setOnClickListener(verifyButtonListener);

        getPhoneNumber();
        setUpFirebase();
        SendSMS();


    }

    private void getPhoneNumber() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            phoneNumber = bundle.getString("phoneNumber");
        }
    }


    View.OnClickListener verifyButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String code = verifyNumberEditText.getText().toString();

            showToast("Verifying number...");
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
            signInWithPhoneAuthCredential(credential);

        }
    };

    private void setUpFirebase() {

        mAuth = FirebaseAuth.getInstance();

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    showToast(e.getMessage());
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };
    }


    private void SendSMS() {

        Log.e(TAG, "phone " + phoneNumber);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            showToast("Number is verified!");
                            //TODO put the logic here to start next activity...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid

                                verifyNumberEditText.setText(null);
                                showToast("Invalid Code Please enter the code again");
                            }
                        }
                    }
                });
    }


    private void showToast(String text) {
        Toast.makeText(VerifyNumberActivity.this, text, Toast.LENGTH_SHORT).show();
    }
}
