package com.puchoInc.diya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.Login;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {
    private Button sendVerificationcodeButton;
    private EditText inputPhoneNumber,inputVerificationCode;

    private CountryCodePicker ccp;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private RelativeLayout relativeLayout;
    private  String phoneNumber = "",checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputPhoneNumber=findViewById(R.id.phone_number_input);
        inputVerificationCode=findViewById(R.id.phone_verification);

        sendVerificationcodeButton=findViewById(R.id.send_ver_code_button);
        relativeLayout = findViewById(R.id.phoneAuth);

        ccp = findViewById(R.id.ccp);
        ccp.registerCarrierNumberEditText(inputPhoneNumber);


        mAuth=FirebaseAuth.getInstance();

        loadingBar=new ProgressDialog(this,R.style.MyAlertDialogStyle);

        sendVerificationcodeButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                if(sendVerificationcodeButton.getText().equals("Submit") || checker.equals("Code Sent")) {
                    String verificationCode = inputVerificationCode.getText().toString();
                    if (verificationCode.equals("")) {
                        Toast.makeText(LoginActivity.this, "Write verification code first", Toast.LENGTH_SHORT).show();
                    } else {


                        loadingBar.setTitle("Code verification");
                        loadingBar.setMessage("Please wait, we are Verifying your Pass Code...");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, verificationCode);
                        signInWithPhoneAuthCredential(credential);


                    }


                }

                else {


                    phoneNumber = ccp.getFullNumberWithPlus();
                    if (TextUtils.isEmpty(phoneNumber)) {
                        Toast.makeText(LoginActivity.this, "please enter your phone number first...", Toast.LENGTH_SHORT).show();

                    } else {

                        loadingBar.setTitle("Phone Number verification");
                        loadingBar.setMessage("Please wait, we will be verifying your Phone Number");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                phoneNumber,        // Phone number to verify
                                60,                 // Timeout duration
                                TimeUnit.SECONDS,   // Unit of timeout
                                LoginActivity.this,               // Activity (for callback binding)
                                callbacks);        // OnVerificationStateChangedCallbacks


                    }
                }


            }

        });





        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential)
            {
                signInWithPhoneAuthCredential(phoneAuthCredential);
                SharedPreferences splogin=getSharedPreferences("isLogin",0);
                SharedPreferences.Editor editor=splogin.edit();
                editor.putBoolean("isAlreadyLogin",true);
                editor.putString("PhoneNumber",inputPhoneNumber.getText().toString());
                editor.commit();
            }

            @Override
            public void onVerificationFailed(FirebaseException e)
            {

                Toast.makeText(LoginActivity.this,"invalid,please enter correct phone number with your country code",Toast.LENGTH_SHORT).show();

                loadingBar.dismiss();
                relativeLayout.setVisibility(View.VISIBLE);
                sendVerificationcodeButton.setText("CONTINUE");
                inputVerificationCode.setVisibility(View.GONE);



            }


            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;
                relativeLayout.setVisibility(View.GONE);
                checker = "Code Sent";
                sendVerificationcodeButton.setText("Submit");
                inputVerificationCode.setVisibility(View.VISIBLE);

                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this,"code has been sent, please check and verify...",Toast.LENGTH_SHORT).show();


            }

        };
    }



    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            loadingBar.dismiss();
                            sendUserToMainActivity();

                        }

                        else {

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(LoginActivity.this,"Error: "  +  message,Toast.LENGTH_SHORT).show();


                        }
                    }
                });
    }






    private void sendUserToMainActivity() {
        Intent mainIntent =new Intent(LoginActivity.this,HomePageActivity.class);
        startActivity(mainIntent);
        finish();



    }


}
