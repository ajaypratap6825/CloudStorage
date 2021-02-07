package com.example.projects.cloudoverdrive;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    ImageView logo;
    TextView text;
    TextView text2;
    TextView text3;
    Button forgot, login, signUp;
    TextInputLayout email, password;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
        }
        setContentView(R.layout.activity_login);
        logo = findViewById(R.id.logo);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        forgot = findViewById(R.id.forgot);
        login = findViewById(R.id.login);
        signUp = findViewById(R.id.signUp);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(i);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text_email = email.getEditText().getText().toString();
                String text_password = password.getEditText().getText().toString();
                if(text_email.isEmpty() && text_password.isEmpty()){
                    email.setError("Field cannot be empty");
                    password.setError("Field cannot be empty");
                    return;
                }
                if(text_password.isEmpty()){
                    email.setError(null);
                    password.setError("Field cannot be empty");
                    return;
                }
                if(text_email.isEmpty()){
                    password.setError(null);
                    email.setError("Field cannot be empty");
                    return;
                }
                auth.signInWithEmailAndPassword(text_email, text_password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
                            pd.setMessage("Logging in");
                            pd.show();
                            Toast.makeText(LoginActivity.this, "Login successful !!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login failed !! Check your details.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });
    }

            @Override
            public void onBackPressed() {
                Intent ie = new Intent(Intent.ACTION_MAIN);
                ie.addCategory(Intent.CATEGORY_HOME);
                ie.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(ie);
            }

}

