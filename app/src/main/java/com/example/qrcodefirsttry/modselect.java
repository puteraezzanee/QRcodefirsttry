package com.example.qrcodefirsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class modselect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modselect);

        Button selectadminbtn,selectuserbtn;

        selectadminbtn = findViewById(R.id.selectadminbtn);
        selectuserbtn = findViewById(R.id.selectuserbtn);

        selectadminbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intadmin = new Intent(modselect.this, adminlogin.class);
                startActivity(intadmin);
            }
        });

        selectuserbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intuser = new Intent(modselect.this, Login.class);
                startActivity(intuser);
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}
