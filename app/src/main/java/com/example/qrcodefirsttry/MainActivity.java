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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.qrcodefirsttry.notify.CHANNEL_1_ID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "MainActivity";

    //View Objects
    private Button buttonScan, btnBack;
    private TextView textViewTitle, textViewPublisher, textViewDate, textViewISBN, textView69;

    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseAuth mAuth;
    private DatabaseReference reff;

    private NotificationManagerCompat notificationManager;

    //qr code scanner object
    private IntentIntegrator qrScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

Toast.makeText(MainActivity.this, "Firebase connection Success", Toast.LENGTH_LONG).show();

        //View objects
        buttonScan = findViewById(R.id.buttonScan);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewPublisher = findViewById(R.id.textViewPublisher);
        textViewDate = findViewById(R.id.textViewDate);
        textViewISBN = findViewById(R.id.textViewISBN);
        textView69 = findViewById(R.id.textView69);


        notificationManager = NotificationManagerCompat.from(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        reff = mFirebaseDatabase.getReference();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @NonNull

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = firebaseAuth.getCurrentUser();
                if( mFirebaseUser != null ){
                    Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"Please Login",Toast.LENGTH_SHORT).show();
                }
            }
        };



        // Read from the database
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //Book=new Book();
        //reff= FirebaseDatabase.getInstance().getReference().child("Book");

        //logout button
        btnBack = findViewById(R.id.logout);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intToMenu = new Intent(MainActivity.this, Menu.class);
                startActivity(intToMenu);
            }
        });

        //intializing scan object
        qrScan = new IntentIntegrator(this);


        //attaching onclick listener
        buttonScan.setOnClickListener(this);





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
                    textViewTitle.setText(obj.getString("Title"));
                    textViewPublisher.setText(obj.getString("Publisher"));
                    textViewDate.setText(obj.getString("Date"));
                    textViewISBN.setText(obj.getString("ISBN"));
                    textView69.setText(obj.getString("CopyNum"));

                    String k1 = textViewISBN.getText().toString();
                    String k2 = textViewTitle.getText().toString();
                    String k3 = textViewDate.getText().toString();
                    String k4 = textViewPublisher.getText().toString();
                    String k5 = textView69.getText().toString();

                    FirebaseUser mFirebaseUser = mAuth.getCurrentUser();
                    String userID = mFirebaseUser.getUid();
                    String userEmail = mFirebaseUser.getEmail();
                    reff.child("User_id").child(userID).child("userID").setValue(userID);
                    reff.child("User_id").child(userID).child("Email").setValue(userEmail);
                    reff.child("ongoing").child(userID).child("A_ISBN").setValue(k1);
                    reff.child("ongoing").child(userID).child("B_Title").setValue(k2);
                    reff.child("ongoing").child(userID).child("C_Date").setValue(k3);
                    reff.child("ongoing").child(userID).child("D_Publisher").setValue(k4);
                    reff.child("ongoing").child(userID).child("E_CopyNum").setValue(k5);

                    Toast.makeText(MainActivity.this, "data inserted to database", Toast.LENGTH_LONG).show();

                    //update borrow return date
                    Calendar c = Calendar.getInstance();
                    Date today = c.getTime();

                    c.add(Calendar.DATE, 3);

                    Date future = c.getTime();
                    TextView textViewDate = findViewById(R.id.borrowdate);
                    TextView textViewDate2 = findViewById(R.id.returndate);

                    textViewDate.setText("Borrow date : " + today);
                    textViewDate2.setText("Return date : " + future);
                    String L = today.toString();
                    String L1 = future.toString();
                    reff.child("ongoing").child(userID).child("E_Borrow").setValue(L);
                    reff.child("ongoing").child(userID).child("F_Return").setValue(L1);


                    String title = "Book Return Reminder";
                    String message = "Return borrowed book";
                    final Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                            .setSmallIcon(R.drawable.ic_one)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .build();

//30000=30seconds,86400000=1day
                    new CountDownTimer(259200000, 129600000) {

                        public void onTick(long millisUntilFinished) {

                            notificationManager.notify(1, notification);
                        }

                        public void onFinish() {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                            builder.setCancelable(true);
                            builder.setTitle("REMINDER");
                            builder.setMessage("TODAY IS THE DUE DATE FOR RETURNING BORROWED BOOK");

                            builder.setNegativeButton("OKAY", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                        }
                    }.start();

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
    public void onClick(View view) {

        //initiating the qr code scan
        qrScan.initiateScan();
    }
    @Override
    public void onBackPressed(){

    }

}
