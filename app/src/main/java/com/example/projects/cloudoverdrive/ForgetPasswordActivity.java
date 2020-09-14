package com.example.projects.cloudoverdrive;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {
    ImageView image;
    TextView text, text2;
    TextInputLayout email;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        image= findViewById(R.id.image);
        text= findViewById(R.id.text);
        text2= findViewById(R.id.text2);
        email= findViewById(R.id.email);
        submit= findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email= email.getEditText().getText().toString();
                if(TextUtils.isEmpty(text_email)){
                    Toast.makeText(ForgetPasswordActivity.this, "Invalid Email-id", Toast.LENGTH_SHORT).show();
                }
                else{
                    sendPasswordReset(text_email);
                }
            }
        });
    }

    public void sendPasswordReset( String text_email) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(text_email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgetPasswordActivity.this, "Email sent !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    @Override
    public void onBackPressed(){
        Intent i = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
        startActivity(i);

    }

}