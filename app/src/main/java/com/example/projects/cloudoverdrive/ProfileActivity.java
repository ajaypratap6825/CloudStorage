package com.example.projects.cloudoverdrive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView profile_image;
    TextView text, uName, userName, head, headName, fullName, headEmail, email;
    Button update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        toolbar = findViewById(R.id.toolbar);
        profile_image = findViewById(R.id.profile_image);
        update = findViewById(R.id.update);
        text = findViewById(R.id.text);
        uName = findViewById(R.id.uName);
        userName = findViewById(R.id.userName);
        head = findViewById(R.id.head);
        headName = findViewById(R.id.headName);
        fullName = findViewById(R.id.fullName);
        headEmail = findViewById(R.id.headEmail);
        email = findViewById(R.id.email);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent (ProfileActivity.this, UpdateActivity.class);
                startActivity(i);
                finish();
            }
        });

        getE();
        getF();
        getU();

    }
    private void getU(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        reference.child("valu").addValueEventListener(new ValueEventListener() {
            String fullname, emai, username;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username  = dataSnapshot.getValue(String.class);
                userName.setText(username);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "No such user exists", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getE(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("vale");
            ref.addValueEventListener(new ValueEventListener() {
                String emai;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    emai = snapshot.getValue(String.class);
                    email.setText(emai);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
    }
    private void getF(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String userId = user.getUid();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("valn");
        ref.addValueEventListener(new ValueEventListener() {
            String name;
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                name = snapshot.getValue(String.class);
                fullName.setText(name);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}


