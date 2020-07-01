package com.example.qrcodefirsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class MenuAdmin extends AppCompatActivity {

    private Button generatorbtn,button,btnLogout,button4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);



        generatorbtn = findViewById(R.id.generatorbtn);
        button =findViewById(R.id.button);
        button4 = findViewById(R.id.button4);

        btnLogout = findViewById(R.id.button10);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(MenuAdmin.this, adminlogin.class);
                startActivity(intToMain);
            }
        });

        generatorbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intadmin = new Intent(MenuAdmin.this, qrgenerator2.class);
                startActivity(intadmin);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intadmin = new Intent(MenuAdmin.this, adminscroll.class);
                startActivity(intadmin);
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intadmin = new Intent(MenuAdmin.this, bookreturnUser.class);
                startActivity(intadmin);
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}
