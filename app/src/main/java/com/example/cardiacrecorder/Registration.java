package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Registration extends AppCompatActivity {

    DatabaseReference ref,dataBaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cardiac-recorder-6e7f6-default-rtdb.firebaseio.com/");
    private EditText Pass, Pass2, Name, DateOfBirth, MobileNo, Email;
    private Button Next;
    private TextView AccNO;
    private RadioGroup Gender;
    private RadioButton rbutton;
    private Double Balance = 0D;
    FirebaseAuth mauth;
    long maxid=0;
    int year=2001,month=5,day=28;
    String AccountNO,pass,pass2,email,name,dateofbirth,mobileno,gender,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        pre();
        generateUniqueAccountNo();

        DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;
                        DateOfBirth.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userSignUp();
                //Toast.makeText(Registration.this, "ok", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void pre(){
        Calendar calender=Calendar.getInstance();
        year=calender.get(Calendar.YEAR);
        month=calender.get(Calendar.MONTH);
        day=calender.get(Calendar.DAY_OF_MONTH);
        AccNO = findViewById(R.id.accountno);
        Pass = findViewById(R.id.password);
        Pass2= findViewById(R.id.password2);
        Email = findViewById(R.id.email);
        Name = findViewById(R.id.name);
        DateOfBirth = findViewById(R.id.dob);
        MobileNo = findViewById(R.id.mobile);
        Gender = findViewById(R.id.gender);
        Next = findViewById(R.id.next);
        mauth = FirebaseAuth.getInstance();
    }

    public void generateUniqueAccountNo(){

        dataBaseReference.child("AccountToemail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                maxid=snapshot.getChildrenCount();
                maxid++;
                maxid+=100000;
                AccountNO = String.valueOf(maxid);
                while(snapshot.hasChild(AccountNO)){
                    maxid++;
                    AccountNO = String.valueOf(maxid);
                }
                AccNO.setText(AccountNO);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void userSignUp() {
        AccountNO = AccNO.getText().toString();
        pass = Pass.getText().toString();
        pass2=Pass2.getText().toString();
        name = Name.getText().toString().trim();
        email = Email.getText().toString().trim();
        dateofbirth = DateOfBirth.getText().toString();
        mobileno = MobileNo.getText().toString().trim();

        int genderid= Gender.getCheckedRadioButtonId();

        if(name.isEmpty()){
            //Toast.makeText(CustomerRegister.this, "Enter Name", Toast.LENGTH_SHORT).show();
            Name.setError("Please Enter Name");
            Name.requestFocus();
            return;
        }
        if(mobileno.isEmpty()){
            //Toast.makeText(CustomerRegister.this, "Enter Name", Toast.LENGTH_SHORT).show();
            MobileNo.setError("Please Enter Name");
            MobileNo.requestFocus();
            return;
        }
        if(mobileno.length()!=10){
            //Toast.makeText(CustomerRegister.this, "Enter Name", Toast.LENGTH_SHORT).show();
            MobileNo.setError("Please Enter Valid Number");
            MobileNo.requestFocus();
            return;
        }
        mobileno="0"+mobileno;
        if(email.isEmpty()){
            //Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            Email.setError("Enter Email");
            Email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            //Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT).show();
            Email.setError("Please Enter Valid Email");
            Email.requestFocus();
            return;
        }
        if(pass.isEmpty()){
            //Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            Pass.setError("Please Enter Password");
            Pass.requestFocus();
            return;
        }
        if(pass.length()<6){
            //Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            Pass.setError("Minimum password length must be 6 character");
            Pass.requestFocus();
            return;
        }
        if(!pass2.equals(pass)){
            //Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            Pass2.setError("Password doesn't match");
            Pass2.requestFocus();
            return;
        }
        if(genderid==-1){
            Toast.makeText(this, "Select a Gender", Toast.LENGTH_SHORT).show();
            return;
        }
        rbutton = findViewById(genderid);
        gender = (String) rbutton.getText();
        fillUp();
    }
    public void fillUp(){
        mauth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            dataBaseReference.child("PhonenoToAccountno").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild(mobileno)){
                                        MobileNo.setError("Already Registered With this Mobile No");
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        if (user != null) {
                                            user.delete()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> deleteTask) {
                                                            if (deleteTask.isSuccessful()) {
                                                                MobileNo.requestFocus();
                                                                Toast.makeText(Registration.this, "Account creation failed. Mobile number already registered.", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                Toast.makeText(Registration.this, "Failed to delete user: " + deleteTask.getException(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                        else {
                                            MobileNo.requestFocus();
                                            Toast.makeText(Registration.this, "Account creation failed. Mobile number already registered.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    else{
                                        boolean verified=false;
                                        User User=new User(AccountNO,name,mobileno,email,pass,gender,dateofbirth,verified);
                                        uid=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        FirebaseDatabase.getInstance().getReference("USER").child(uid).setValue(User).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    phoneoraccountToemail();
                                                    Toast.makeText(Registration.this, "Registered", Toast.LENGTH_SHORT).show();
                                                    Intent intent=new Intent(Registration.this,MainActivity.class);
                                                    startActivity(intent);
                                                }
                                                else {
                                                    Toast.makeText(Registration.this, "Failed to register"+task.getException(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                        }
                        else {
                            Toast.makeText(Registration.this, "Failed to register"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void phoneoraccountToemail() {
        dataBaseReference.child("PhonenoToemail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBaseReference.child("PhonenoToemail").child(mobileno).child("Phone No").setValue(email);
                Toast.makeText(Registration.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dataBaseReference.child("AccountToemail").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                dataBaseReference.child("AccountToemail").child(AccountNO).child("Account No").setValue(email);
                Toast.makeText(Registration.this,"Registered Successfully",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void onBackPressed(){
        startActivity(new Intent(Registration.this,MainActivity.class));
    }

}