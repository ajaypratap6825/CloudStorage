package com.example.projects.cloudoverdrive;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {
    TextInputLayout name, username;
    Toolbar toolbar;
    Button update;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        toolbar = findViewById(R.id.toolbar);
        name= findViewById(R.id.name);
        username= findViewById(R.id.username);
        update = findViewById(R.id.update);
        toolbar.setTitle("");
        toolbar.setBackgroundColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                finish();
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validationName() || !validationUsername() )
                {
                    return;
                }
                String changedName= name.getEditText().getText().toString();
                String changedUsername= username.getEditText().getText().toString();
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userId = user.getUid();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
                ref.child("valn").setValue(changedName);
                ref.child("valu").setValue(changedUsername);
                startActivity(new Intent(UpdateActivity.this, ProfileActivity.class));
                finish();
            }
        });
    }
    private boolean validationName(){
        String valn= name.getEditText().getText().toString();
        if(valn.isEmpty()){
            name.setError("Field cannot be empty");
            return false;
        }else{
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }
    private boolean validationUsername(){
        String valu= username.getEditText().getText().toString();
        if(valu.isEmpty()){
            username.setError("Field cannot be empty");
            return false;
        } else if(valu.length()>=15) {
            username.setError("Username too long");
            return false;
        }
        else if (username.getEditText().getText().toString().contains(" ")) {
            username.setError("No Spaces Allowed");
            return false;
        }else{
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }
}