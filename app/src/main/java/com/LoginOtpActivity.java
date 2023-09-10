package com;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.noteapp.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.utils.AndroidUtil;

import java.util.concurrent.TimeUnit;

public class LoginOtpActivity extends AppCompatActivity {


    String phoneNumber;
    Long timeoutSeconds=60L;
    String verificationCode;
    PhoneAuthProvider.ForceResendingToken ResendingToken;

    EditText otpInput;
    Button nextBtn;
    ProgressBar progressBar;
    TextView resendOtpTextView;
    FirebaseAuth mAuth= FirebaseAuth.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_otp);

        otpInput=findViewById(R.id.login_otp);
        nextBtn=findViewById(R.id.login_next);
        progressBar=findViewById(R.id.login_progress_bar);
        resendOtpTextView=findViewById(R.id.resend_otp_textview);

        phoneNumber=getIntent().getExtras().getString("phone");
        sendOtp(phoneNumber,false);
    }
    void sendOtp(String phoneNumber,boolean isResend){
        setInProgress(true);
        PhoneAuthOptions.Builder builder=
                PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                        signIn(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(FirebaseException e) {
                        AndroidUtil.showToast(getApplicationContext(),"OTP Verification failed");
                    }

                    @Override
                    public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(s, forceResendingToken);
                        verificationCode=s;
                        ResendingToken=forceResendingToken;
                        AndroidUtil.showToast(getApplicationContext(),"OTP sent succesfully");
                    }
                });

    }

    void setInProgress(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            nextBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            nextBtn.setVisibility(View.VISIBLE);
        }
    }

    void signIn(PhoneAuthCredential phoneAuthCredential){

    }
}