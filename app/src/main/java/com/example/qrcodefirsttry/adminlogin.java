package com.example.qrcodefirsttry;

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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class adminlogin extends AppCompatActivity {

    private Button btnSignIn,backbtn;
    private EditText emailId,password;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);

        mFirebaseAuth = FirebaseAuth.getInstance();

        backbtn = findViewById(R.id.backbtn);
        emailId = findViewById(R.id.editText11);
        password = findViewById(R.id.editText12);
        btnSignIn = findViewById(R.id.button8);
        final String x = "admin@admin.co";

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(adminlogin.this, modselect.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(adminlogin.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(email.equals(x) && !(pwd.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pwd).addOnCompleteListener(adminlogin.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(adminlogin.this,"Login Error, Please Login Again",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                    Intent intToHome = new Intent(adminlogin.this,LoginAdmin.class);
                                    startActivity(intToHome);
                                }
                            }
                    });
                }
                else{
                    Toast.makeText(adminlogin.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}
