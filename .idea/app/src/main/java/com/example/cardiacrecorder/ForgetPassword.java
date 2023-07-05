package com.example.cardiacrecorder;

import static android.view.View.GONE;

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
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPassword extends AppCompatActivity {

    EditText Email;
    Button reset;
    ProgressBar progressBar;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        Email=findViewById(R.id.email);
        reset=findViewById(R.id.resetpassword);
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(GONE);
        auth=FirebaseAuth.getInstance();

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetpassword();
            }
        });

    }

    private void resetpassword() {
        String email=Email.getText().toString().trim();
        if(email.isEmpty()){
            Email.setError("Email is required");
            Email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Email.setError("Email is not verified");
            Email.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(GONE);
                    Toast.makeText(ForgetPassword.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ForgetPassword.this,MainActivity.class));
                }
                else{
                    progressBar.setVisibility(GONE);
                    Toast.makeText(ForgetPassword.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void onBackPressed(){
        startActivity(new Intent(ForgetPassword.this,MainActivity.class));
    }
}