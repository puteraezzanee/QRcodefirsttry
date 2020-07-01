package com.example.qrcodefirsttry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

import static com.example.qrcodefirsttry.notify.CHANNEL_1_ID;

public class bookreturnUser extends AppCompatActivity {

    private Button btnBack22,buttonScan22,button12;
    private TextView textViewTitle22,textViewPublisher22,textViewDate22,textViewISBN22,textViewDate23,textViewReturn23,textViewBorrow23;
    private EditText editText5;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    private DatabaseReference reff;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookreturn_user);

        textViewTitle22 = findViewById(R.id.textViewTitle22);
        textViewPublisher22 = findViewById(R.id.textViewPublisher22);
        textViewDate22 = findViewById(R.id.textViewDate22);
        textViewISBN22 = findViewById(R.id.textViewISBN22);
        editText5 = findViewById(R.id.editText5);
        button12 = findViewById(R.id.button12);

        final String x = editText5.getText().toString();

        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        reff = mFirebaseDatabase.getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @NonNull

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(bookreturnUser.this,"You are logged in",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(bookreturnUser.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };


        buttonScan22 = findViewById(R.id.btnScan22);
        btnBack22 = findViewById(R.id.buttonback22);

        btnBack22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMenu = new Intent(bookreturnUser.this, MenuAdmin.class);
                startActivity(intToMenu);
            }
        });

        //intializing scan object
        qrScan = new IntentIntegrator(this);


        //attaching onclick listener
        buttonScan22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editText5.length()==0)
                {
                    editText5.setError("Enter UID");
                }
                else {
                    qrScan.initiateScan();
                }
            }
        });

        button12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reff = mFirebaseDatabase.getReference();

                reff.child("ongoing").child(x).removeValue();
                Toast.makeText(bookreturnUser.this,"Borrowing details removed from database",Toast.LENGTH_SHORT).show();
            }
        });



    }


    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json



                    JSONObject obj = new JSONObject(result.getContents());

                    //setting values to textviews
                    textViewTitle22.setText(obj.getString("Title"));
                    textViewPublisher22.setText(obj.getString("Publisher"));
                    textViewDate22.setText(obj.getString("Date"));
                    textViewISBN22.setText(obj.getString("ISBN"));

                   // String k1 = textViewISBN22.getText().toString();
                   // String k2 = textViewTitle22.getText().toString();

                    //FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                    //String userID = mFirebaseUser.getUid();
                    //reff.child("status").child(userID).child("ISBN").setValue(k1);
                    //reff.child("status").child(userID).child("Title").setValue(k2);


                    Toast.makeText(bookreturnUser.this, "Book returned data saved", Toast.LENGTH_LONG).show();

                    //update borrow return date
                    Calendar c = Calendar.getInstance();
                    Date today = c.getTime();

                    textViewDate23 = findViewById(R.id.textViewDate23);
                    textViewBorrow23 = findViewById(R.id.textViewBorrow23);
                    textViewReturn23 = findViewById(R.id.textViewReturn23);

                    textViewDate23.setText("Return date : " + today);

                    final String x = editText5.getText().toString();

                    reff = FirebaseDatabase.getInstance().getReference().child("ongoing").child(x);

                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String borrow = dataSnapshot.child("E_Borrow").getValue().toString();
                            String expect = dataSnapshot.child("F_Return").getValue().toString();

                            textViewBorrow23.setText("Borrow date : " + borrow);
                            textViewReturn23.setText("Due date : " + expect);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                            Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();


                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    @Override
    public void onBackPressed(){

    }

}
