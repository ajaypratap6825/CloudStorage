package com.example.projects.cloudoverdrive;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class SettingsActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView text, head, headEmail, email, heading, appVersion, number, privacy;
    Button notification, cache, logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = findViewById(R.id.toolbar);
        text = findViewById(R.id.text);
        head = findViewById(R.id.head);
        headEmail = findViewById(R.id.headEmail);
        email = findViewById(R.id.email);
        heading = findViewById(R.id.heading);
        appVersion = findViewById(R.id.appVersion);
        number = findViewById(R.id.number);
        privacy = findViewById(R.id.privacy);
        notification = findViewById(R.id.notification);
        cache = findViewById(R.id.cache);
        logout = findViewById(R.id.logout);
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
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(SettingsActivity.this, "Logged out !!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
                finish();
            }
        });

        number.setText(BuildConfig.VERSION_NAME);

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
        notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                intent.putExtra("app_package", getPackageName());
                intent.putExtra("app_uid", getApplicationInfo().uid);
                intent.putExtra("android.provider.extra.APP_PACKAGE", getPackageName());
                startActivity(intent);
            }
        });
        cache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
    }
}