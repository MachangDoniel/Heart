package com.example.cardiacrecorder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity implements SelectListener {


    FirebaseUser emp = FirebaseAuth.getInstance().getCurrentUser();
    String userId=emp.getUid();
    RecyclerView recyclerView;
    DatabaseReference database=FirebaseDatabase.getInstance().getReference("Details");
    MyAdapter myAdapter;
    String testId = "";
    ImageView Add,Logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.list);
        Add=findViewById(R.id.add);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Logout=findViewById(R.id.logout);

        EditText editText = findViewById(R.id.text);

        showData("");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = String.valueOf(editable).toLowerCase();
                showData(text);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AddNewData.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showData(String key) {
        ArrayList<Details> list = new ArrayList<>();
        database.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Details details = dataSnapshot.getValue(Details.class);
                    if (key.isEmpty() || (details != null && details.name.toLowerCase().contains(key))) {
                        list.add(details);
                    }
                }
                myAdapter = new MyAdapter(Home.this, list, Home.this);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void onBackPressed() {
        Intent intent = new Intent(Home.this, Home.class);
        startActivity(intent);
    }

    @Override
    public void onItemClicked(Details details) {
        testId = String.valueOf(details.getId());
        Toast.makeText(this, details.getName() + " Clicked.", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Home.this, Test.class);
        intent.putExtra("testId", testId);
        startActivity(intent);
    }
}
