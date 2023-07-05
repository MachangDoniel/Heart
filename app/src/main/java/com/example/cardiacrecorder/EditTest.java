package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditTest extends AppCompatActivity {

    FirebaseUser emp = FirebaseAuth.getInstance().getCurrentUser();
    String userId=emp.getUid();
    DatabaseReference ref;

    String testId;
    String comment = "";

    TextView Name, HeartRate, Systolic, Diastolic, Date, Time, Comment;
    ImageView Save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_test);

        Name=findViewById(R.id.name);
        HeartRate=findViewById(R.id.heartrate);
        Systolic=findViewById(R.id.systolic);
        Diastolic=findViewById(R.id.diastolic);
        Date=findViewById(R.id.date);
        Time=findViewById(R.id.time);
        Comment=findViewById(R.id.comment);
        Save=findViewById(R.id.save);

        testId=getIntent().getStringExtra("testId");
        ref= FirebaseDatabase.getInstance().getReference("Details").child(userId).child(testId);


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Details details=snapshot.getValue(Details.class);

                if(details!=null)
                {
                    String name, heartrate, systolic, diastolic, date, time, comment;
                    name=details.name;
                    heartrate=String.valueOf(details.heartrate);

                    systolic=String.valueOf(details.systolic);
                    diastolic=String.valueOf(details.diastolic);
                    date=details.date;
                    time=details.time;
                    comment=details.comment;

                    Name.setText(name);
                    HeartRate.setText(heartrate);
                    Systolic.setText(systolic);
                    Diastolic.setText(diastolic);
                    Date.setText(date);
                    Time.setText(time);
                    Comment.setText(comment);
                }
                else{
                    Toast.makeText(EditTest.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                edit();
            }
        });
    }

    private void edit() {


        int newId = Integer.parseInt(testId); // Decrement the max id by 1
        String name = Name.getText().toString().trim();

        int heartRateValue = Integer.parseInt(HeartRate.getText().toString().trim());
        int systolicValue = Integer.parseInt(Systolic.getText().toString().trim());
        int diastolicValue = Integer.parseInt(Diastolic.getText().toString().trim());


        if (heartRateValue < 0) {
            HeartRate.setError("Heart rate can't be negative");
            HeartRate.requestFocus();
            return;
        } else if (heartRateValue < 60) {
            comment = "Bradycardia(slow heart rate), ";
        } else if (heartRateValue <= 120) {
            comment = "Normal Heart Rate, ";
        } else if (heartRateValue < -200) {
            comment = "Tachycardia(fast heart rate), ";
        } else {
            HeartRate.setError("Heart rate is too much");
            HeartRate.requestFocus();
            return;
        }

        if(systolicValue<diastolicValue){
            Systolic.setError("Systolic value can't be less than Diastolic Value");
            Systolic.requestFocus();
            return;
        }

        if (systolicValue < 0) {
            Systolic.setError("Systolic rate can't be negative");
            Systolic.requestFocus();
            return;
        } else if (systolicValue <= 120) {
            comment += "Normal systolic pressure, ";
        } else if (systolicValue <= 129) {
            comment += "Elavated systolic pressure, ";
        } else if (systolicValue <= 139) {
            comment += "Hypertension stage 1 systolic pressure, ";
        } else if (systolicValue <= 179) {
            comment += "Hypertension stage 2 systolic pressure, ";
        } else if (systolicValue <= 250) {
            comment += "Hypertension crisis systolic pressure, ";
        } else {
            Systolic.setError("Heart rate is too much");
            Systolic.requestFocus();
            return;
        }

        if (systolicValue < 0) {
            Diastolic.setError("Diastolic rate can't be negative");
            Diastolic.requestFocus();
            return;
        } else if (diastolicValue <= 80) {
            comment += "Normal diastolic pressure. ";
        } else if (diastolicValue <= 89) {
            comment += "Elavated diastolic pressure. ";
        } else if (diastolicValue <= 99) {
            comment += "Hypertension stage 1 diastolic pressure. ";
        } else if (diastolicValue <= 119) {
            comment += "Hypertension stage 2 diastolic pressure. ";
        } else if (diastolicValue <= 200) {
            comment += "Hypertension crisis diastolic pressure. ";
        } else {
            Diastolic.setError("Diastolic rate is too much");
            Diastolic.requestFocus();
            return;
        }


        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

        Details details = new Details(heartRateValue, systolicValue, diastolicValue, newId, comment, name, date, time);
        DatabaseReference newDetailsRef = FirebaseDatabase.getInstance().getReference("Details").child(userId).child(String.valueOf(newId));
        newDetailsRef.setValue(details)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(EditTest.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditTest.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });

        Intent intent = new Intent(EditTest.this, Test.class);
        intent.putExtra("testId", testId);
        startActivity(intent);
    }
    public void onBackPressed() {
        Intent intent = new Intent(EditTest.this, Test.class);
        intent.putExtra("testId", testId);
        startActivity(intent);
    }
}

