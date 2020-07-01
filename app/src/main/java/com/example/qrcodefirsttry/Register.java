package com.example.qrcodefirsttry;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText emailId, password, username, phonenum,password2;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;

    DatabaseReference reff;
    user user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editText);
        password = findViewById(R.id.editText2);
        password2 = findViewById(R.id.editText55);
        username = findViewById(R.id.editText4);
        phonenum = findViewById(R.id.editText3);
        btnSignUp = findViewById(R.id.button2);
        tvSignIn = findViewById(R.id.textViewISBN22);

        user=new user();
        reff= FirebaseDatabase.getInstance().getReference().child("user");

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone= phonenum.getText().toString();
                String name = username.getText().toString();
                String email = emailId.getText().toString();
                String pwd = password.getText().toString();
                String pwd2 = password2.getText().toString();

                if(phone.isEmpty()){
                    phonenum.setError("Please enter Phone number");
                    phonenum.requestFocus();
                }
                if(phone.length()>11){
                    phonenum.setError("Invalid phone number, must be in between 10 to 12 digit only");
                    phonenum.requestFocus();
                }
                if(phone.length()<10){
                    phonenum.setError("Invalid phone number, must be in between 10 to 12 digit only");
                    phonenum.requestFocus();
                }
                if(name.isEmpty()){
                    username.setError("Please enter Name");
                    username.requestFocus();
                }
                if(email.isEmpty()){
                    emailId.setError("Please enter email id");
                    emailId.requestFocus();
                }
                //tak isi password
                else  if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else  if(pwd2.isEmpty()){
                    password2.setError("Please re-enter your password");
                    password2.requestFocus();
                }
                else if(!(pwd.equals(pwd2))){
                    password2.setError("Password does not match");
                    password2.requestFocus();
                }
                else if (!isValidPassword(pwd)){
                    password.setError("Password require at least one number,one uppercase letter, one lower case letter and one special character");
                    password.requestFocus();
                }
                else  if(pwd.length()<12){
                    password.setError("Password should be at least 12 characters");
                    password.requestFocus();
                }
                //tak isi dua dua
                else  if(email.isEmpty() && pwd.isEmpty()){
                    Toast.makeText(Register.this,"Fields Are Empty!",Toast.LENGTH_SHORT).show();
                }
                else  if(!(email.isEmpty() && pwd.isEmpty())){


                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @NonNull
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Objects.requireNonNull(mFirebaseAuth.getCurrentUser()).sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "Registered successfully. Please check email for verification.", Toast.LENGTH_SHORT).show();

                                            emailId.setText("");
                                            password.setText("");

                                            user.setUsername(username.getText().toString().trim());
                                            user.setPhonenum(phonenum.getText().toString().trim());
                                            reff.push().setValue(user);
                                            FirebaseAuth.getInstance().signOut();
                                            Intent intToMain = new Intent(Register.this, Register.class);
                                            startActivity(intToMain);
                                        }
                                    }
                                });


                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this,"Error Occurred!",Toast.LENGTH_SHORT).show();

                }
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
    }

    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    @Override
    public void onBackPressed(){

    }
}
