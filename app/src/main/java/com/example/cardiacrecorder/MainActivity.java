package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class MainActivity extends AppCompatActivity {

    Button register, login, Forgotpassword;
    EditText Email, password;
    FirebaseAuth mauth;
    ProgressBar progressBar;
    DatabaseReference ref,dataBaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://cardiac-recorder-6e7f6-default-rtdb.firebaseio.com/");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        register = (Button) findViewById(R.id.register);
        login = (Button) findViewById(R.id.login);
        mauth = FirebaseAuth.getInstance();
        Forgotpassword=findViewById(R.id.forgotpassword);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        //String id = "1907121", pass = "121";
        //1907121 & 121



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userLogin();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Registration.class);
                startActivity(intent);
            }
        });
        Forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ForgetPassword.class);
                startActivity(intent);
            }
        });
    }

    private void userLogin() {
        progressBar.setVisibility(View.VISIBLE);
        String email = Email.getText().toString().trim();
        String pass = password.getText().toString().trim();
        if(email.isEmpty()){
            //Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
            Email.setError("Enter Email");
            Email.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        if(pass.isEmpty()){
            //Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
            password.setError("Please Enter Password");
            password.requestFocus();
            progressBar.setVisibility(View.GONE);
            return;
        }
        mauth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser emp = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref= FirebaseDatabase.getInstance().getReference("USER");
                    String userId;
                    userId=emp.getUid();
                    //Toast.makeText(MainActivity.this, userId, Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference("USER").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User profile=snapshot.getValue(User.class);
                            boolean verified=true;
                            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                            if (firebaseUser.isEmailVerified() && verified) {
                                Intent intent = new Intent(MainActivity.this, Home.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Log in Succesfull", Toast.LENGTH_SHORT).show();
                                intent.putExtra("email", email);

                                startActivity(intent);
                                finish();
                            }
                            else {
                                firebaseUser.sendEmailVerification();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "check your email to verify", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "log in Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void onBackPressed(){
        startActivity(new Intent(MainActivity.this,MainActivity.class));
    }
}