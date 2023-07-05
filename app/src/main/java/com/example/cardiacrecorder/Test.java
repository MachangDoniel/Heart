package com.example.cardiacrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Test extends AppCompatActivity {



    FirebaseUser emp = FirebaseAuth.getInstance().getCurrentUser();
    String userId=emp.getUid();
    DatabaseReference ref;

    String testId;

    TextView Name, HeartRate, Systolic, Diastolic, Date, Time, Comment;
    ImageView Edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Name=findViewById(R.id.name);
        HeartRate=findViewById(R.id.heartrate);
        Systolic=findViewById(R.id.systolic);
        Diastolic=findViewById(R.id.diastolic);
        Date=findViewById(R.id.date);
        Time=findViewById(R.id.time);
        Comment=findViewById(R.id.comment);
        Edit=findViewById(R.id.edit);

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
                    Toast.makeText(Test.this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void onBackPressed() {
        Intent intent = new Intent(Test.this, Home.class);
        startActivity(intent);
    }
}