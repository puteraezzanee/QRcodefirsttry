package com.example.qrcodefirsttry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrgenerator2 extends AppCompatActivity {

    EditText titlevalue,publishervalue,datevalue,ISBNvalue,editText69;
    ImageView qrImage;
    Button start,button11;
    String inputValue;
    Bitmap bitmap;
    QRGEncoder qrgEncoder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrgenerator2);

        qrImage = findViewById(R.id.QR_Image);
        titlevalue = findViewById(R.id.title_value);
        publishervalue = findViewById(R.id.publisher_value);
        datevalue = findViewById(R.id.date_value);
        ISBNvalue = findViewById(R.id.ISBN_value);
        start = findViewById(R.id.start);
        button11 = findViewById(R.id.button11);
        editText69 = findViewById(R.id.editText69);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String a = titlevalue.getText().toString().trim();
                String b = publishervalue.getText().toString().trim();
                String c = datevalue.getText().toString().trim();
                String d = ISBNvalue.getText().toString().trim();
                String e = editText69.getText().toString().trim();


                final String totalValue = ("{\"Title\":\"" + a +"\",\"CopyNum\":\"" + e + "\",\"Publisher\":\"" + b + "\",\"Date\":\"" + c + "\",\"ISBN\":\"" + d + "\"}");
                inputValue = totalValue;
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    bitmap = qrgEncoder.getBitmap();
                    qrImage.setImageBitmap(bitmap);
                } else {
                    titlevalue.setError("Required");
                    publishervalue.setError("Required");
                    datevalue.setError("Required");
                    ISBNvalue.setError("Required");
                    editText69.setError("Required");
                }
            }
        });
        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMain = new Intent(qrgenerator2.this, MenuAdmin.class);
                startActivity(intToMain);
            }
        });
    }
    @Override
    public void onBackPressed(){

    }
}