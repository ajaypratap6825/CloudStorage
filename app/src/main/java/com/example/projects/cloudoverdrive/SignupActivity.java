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

import com.example.projects.cloudoverdrive.Models.Info;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    TextInputLayout name, username, email, password;
    ImageView logo;
    TextView text, text2, text3;
    Button register, login;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        logo = findViewById(R.id.logo);
        text = findViewById(R.id.text);
        text2 = findViewById(R.id.text2);
        text3 = findViewById(R.id.text3);
        register = findViewById(R.id.register);
        login = findViewById(R.id.login);
        auth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog pd = new ProgressDialog(SignupActivity.this);
                pd.setMessage("Signing in");
                pd.show();
                if (!validationName() || !validationPassword() || !validationEmail() || !validationUsername()) {
                    return;
                }
                String valn = name.getEditText().getText().toString();
                String valu = username.getEditText().getText().toString();
                String vale = email.getEditText().getText().toString();
                String valpa = password.getEditText().getText().toString();
                auth.createUserWithEmailAndPassword(vale, valpa).addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "Registration successful !!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, DashboardActivity.class));
                            finish();
                            String valn = name.getEditText().getText().toString();
                            String valu = username.getEditText().getText().toString();
                            String vale = email.getEditText().getText().toString();
                            String valpa = password.getEditText().getText().toString();
                            Info i = new Info(valn, valu, vale, valpa);
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            String userId = user.getUid();
                            FirebaseDatabase.getInstance().getReference().child("Users").child(userId).setValue(i);
                        } else {
                            Toast.makeText(SignupActivity.this, "Registration failed !!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                /*FirebaseFirestore db= FirebaseFirestore.getInstance();
                Map<String,Object> data= new HashMap<>();
                data.put("Name", valn);
                data.put("Username", valu);
                data.put("E-mail", vale);
                data.put("Password", valpa);
                db.collection("Users").document(valu).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            //Toast.makeText(MainActivity.this, "Values added", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/
            }
        });
    }

    private boolean validationName() {
        String valn = name.getEditText().getText().toString();
        if (valn.isEmpty()) {
            name.setError("Field cannot be empty");
            return false;
        } else {
            name.setError(null);
            name.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validationUsername() {
        String valu = username.getEditText().getText().toString();
        if (valu.isEmpty()) {
            username.setError("Field cannot be empty");
            return false;
        } else if (valu.length() >= 15) {
            username.setError("Username too long");
            return false;
        } else if (username.getEditText().getText().toString().contains(" ")) {
            username.setError("No Spaces Allowed");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validationEmail() {
        String vale = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (vale.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validationPassword() {
        String valpa = password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                ".{4,}" +               //at least 4 characters
                "$";
        if (valpa.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!valpa.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else if (username.getEditText().getText().toString().contains(" ")) {
            username.setError("No Spaces Allowed");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
}