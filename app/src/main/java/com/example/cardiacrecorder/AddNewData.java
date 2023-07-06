package com.example.cardiacrecorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

public class AddNewData extends AppCompatActivity {

    EditText Name, Systolic, Diastolic, Heartrate;
    Button Add;
    DatabaseReference database;
    long max=2147483647;
    FirebaseUser emp = FirebaseAuth.getInstance().getCurrentUser();
    String userId=emp.getUid();
    String comment="";
    int heartRateValue,systolicValue,diastolicValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_data);

        database = FirebaseDatabase.getInstance().getReference("Details");

        Name = findViewById(R.id.name);
        Heartrate = findViewById(R.id.heartrate);
        Systolic = findViewById(R.id.systolic);
        Diastolic = findViewById(R.id.diastolic);
        Add = findViewById(R.id.add);

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                heartRateValue = Integer.parseInt(Heartrate.getText().toString().trim());
                systolicValue = Integer.parseInt(Systolic.getText().toString().trim());
                diastolicValue = Integer.parseInt(Diastolic.getText().toString().trim());


                if(heartRateValue<0){
                    Heartrate.setError("Heart rate can't be negative");
                    Heartrate.requestFocus();
                    return;
                }
                else if(heartRateValue<60){
                    comment=" Bradycardia(slow heart rate)";
                }
                else if(heartRateValue<=120){
                    comment=" Normal Heart Rate";
                }
                else if(heartRateValue<-200){
                    comment=" Tachycardia(fast heart rate)";
                }
                else{
                    Heartrate.setError(" Heart rate is too much");
                    Heartrate.requestFocus();
                    return;
                }

                if(systolicValue<diastolicValue){
                    Systolic.setError("Systolic value can't be less than Diastolic Value");
                    Systolic.requestFocus();
                    return;
                }

                if(systolicValue<0){
                    Systolic.setError("Systolic rate can't be too small");
                    Systolic.requestFocus();
                    return;
                }
                else if(systolicValue<=120){
                    comment+="\n Normal systolic pressure";
                }
                else if(systolicValue<=129){
                    comment+="\n Elavated systolic pressure";
                }
                else if(systolicValue<=139){
                    comment+="\n Hypertension stage 1 systolic pressure";
                }
                else if(systolicValue<=179){
                    comment+="\n Hypertension stage 2 systolic pressure";
                }
                else if(systolicValue<=250){
                    comment+="\n Hypertension crisis systolic pressure";
                }
                else{
                    Systolic.setError("Heart rate is too much");
                    Systolic.requestFocus();
                    return;
                }

                if(systolicValue<0){
                    Diastolic.setError("Diastolic rate can't be negative");
                    Diastolic.requestFocus();
                    return;
                }
                else if(diastolicValue<=80){
                    comment+="\n Normal diastolic pressure";
                }
                else if(diastolicValue<=89){
                    comment+="\n Elavated diastolic pressure";
                }
                else if(diastolicValue<=99){
                    comment+="\n Hypertension stage 1 diastolic pressure";
                }
                else if(diastolicValue<=119){
                    comment+="\n Hypertension stage 2 diastolic pressure";
                }
                else if(diastolicValue<=200){
                    comment+="\n Hypertension crisis diastolic pressure";
                }
                else{
                    Diastolic.setError("Diastolic rate is too much");
                    Diastolic.requestFocus();
                    return;
                }
                addRecords();
            }
        });
    }

    public void addRecords() {

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                long maxid=snapshot.child(userId).getChildrenCount();
//                Toast.makeText(AddNewData.this, "Maxid is: "+maxid, Toast.LENGTH_SHORT).show();
                int newId = (int) (max-maxid); // Decrement the max id by 1
                String name = Name.getText().toString().trim();
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                Details details = new Details(heartRateValue, systolicValue, diastolicValue, newId, comment, name, date, time);
                DatabaseReference newDetailsRef = FirebaseDatabase.getInstance().getReference("Details").child(userId).child(String.valueOf(newId));
                newDetailsRef.setValue(details)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddNewData.this, "Data added successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AddNewData.this, Home.class);
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddNewData.this, "Failed to add data", Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
