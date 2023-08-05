package com.example.chatup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.Objects;

public class firstScreen extends AppCompatActivity {



    Button button,logii;
    EditText email, password;
    FirebaseAuth auth;
    String patternemail = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    String token;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        getSupportActionBar().hide();

        auth =FirebaseAuth.getInstance();
        button = findViewById(R.id.signup);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.passwordsignup);
        logii= findViewById(R.id.signupfirst);

        logii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(firstScreen.this, signUp.class);
                startActivity(intent);
                finish();

            }
        });

        if(auth.getCurrentUser() != null){
            Intent intent =new Intent(firstScreen.this,Mainplatform.class);
            startActivity(intent);
            finish();
        }






        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                String Pass = password.getText().toString();
                if((TextUtils.isEmpty(Email))){
                    Toast.makeText(firstScreen.this, "Enter the email", Toast.LENGTH_SHORT).show();
                } else if ((TextUtils.isEmpty(Pass))) {
                    Toast.makeText(firstScreen.this, "Enter  the password", Toast.LENGTH_SHORT).show();

                } else if (!Email.matches(patternemail)) {
                    email.setError("Give proper Email");

                } else if (password.length()<6) {
                    password.setError("Password should be more than 6 character");
                    Toast.makeText(firstScreen.this, "Password must be longer than 6 digit", Toast.LENGTH_SHORT).show();

                }else {

                    auth.signInWithEmailAndPassword(Email,Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if(task.isSuccessful()){
                                try {

                                    Intent intent = new Intent(firstScreen.this, Mainplatform.class);
                                    startActivity(intent);
                                    finish();
                                }catch (Exception e){
                                    Toast.makeText(firstScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(firstScreen.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }


            }
        });


    }
}