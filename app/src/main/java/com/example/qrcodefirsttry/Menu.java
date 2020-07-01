package com.example.qrcodefirsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Menu extends AppCompatActivity {

    private Button btnBorrow,btnReturn2,btnLogout;
    private TextView useruser;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnBorrow = findViewById(R.id.toBorrow);
        btnReturn2 = findViewById(R.id.toReturn2);
        btnLogout = findViewById(R.id.logout);
        useruser = findViewById(R.id.useruser);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
        String userID = mFirebaseUser.getUid();
        String useremail = mFirebaseUser.getEmail();
        useruser.setText("Hello, " + useremail + "\n\nUser ID : " + userID);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(Menu.this, Login.class);
                startActivity(intToMain);
            }
        }
        );

        btnBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intborrow = new Intent(Menu.this, MainActivity.class);
                startActivity(intborrow);
            }
        }
        );

        btnReturn2.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View v) {
                                             Intent intreturn2 = new Intent(Menu.this, showborrow.class);
                                             startActivity(intreturn2);
                                         }
                                     }
        );
    }
    @Override
    public void onBackPressed(){

    }
}
