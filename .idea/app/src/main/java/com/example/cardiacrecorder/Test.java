package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    ImageView Edit, Delete;
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
        Delete=findViewById(R.id.delete);

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
//                else{
//                    Toast.makeText(Test.this, "Something went Wrong hahahha", Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Test.this, EditTest.class);
                intent.putExtra("testId", testId);
                startActivity(intent);
            }
        });
        Delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Details").child(userId).child(testId);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().removeValue();
                        Toast.makeText(Test.this, "Item is deleted Successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                Intent intent = new Intent(Test.this, Home.class);
                intent.putExtra("testId", testId);
                startActivity(intent);
            }
        });
    }


    public void onBackPressed() {
        Intent intent = new Intent(Test.this, Home.class);
        startActivity(intent);
    }
}