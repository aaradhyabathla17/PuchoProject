
package com.puchoInc.diya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {
     EditText name;
     EditText email;
     EditText password;
     EditText confirmPassword;
     Button signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
    }
    public void init()
    {
        name=(EditText)findViewById(R.id.name_et);
        email=(EditText) findViewById(R.id.email_signup_et);
        password=(EditText) findViewById(R.id.Pass_et);
        confirmPassword=(EditText) findViewById(R.id.Confirm_Pass_et);
        signup=(Button) findViewById(R.id.btn_signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidate())
                {
                    storeDataInFirebase();
                    Intent intent=new Intent(SignUpActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
    public boolean isValidate(){
        if (name.getText().toString() == null || name.getText().toString().trim().length() <= 0) {
            name.setError(getResources().getString(R.string.empty_field_message));
            return false;
        }
        if (email.getText().toString() == null || email.getText().toString().trim().length() <= 0) {
            email.setError(getResources().getString(R.string.empty_field_message));
            return false;
        }
        if (password.getText().toString() == null || password.getText().toString().trim().length() <= 0) {
            password.setError(getResources().getString(R.string.empty_field_message));
            return false;
        }

        if (confirmPassword.getText().toString() == null || confirmPassword.getText().toString().trim().length() <= 0) {
            confirmPassword.setError(getResources().getString(R.string.empty_field_message));
            return false;
        }
        if (confirmPassword.getText().toString().trim().equals(password.getText().toString().trim()) == false) {
            confirmPassword.setError(getResources().getString(R.string.incorrect_match));
            return false;
        }
        return true;
    }
    public void storeDataInFirebase(){
       final User user=new User();
        user.setEmail(email.getText().toString());
        user.setName(name.getText().toString());
        user.setPassword(password.getText().toString());
        DatabaseReference mDatabase=FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").push().setValue(user).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(SignUpActivity.this,"Successful Registration",Toast.LENGTH_LONG).show();}
                else
                {
                    Toast.makeText(SignUpActivity.this,"Something Went Wrong.",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
