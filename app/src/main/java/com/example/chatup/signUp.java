package com.example.chatup;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import de.hdodenhof.circleimageview.CircleImageView;

public class signUp extends AppCompatActivity {
    TextView loginto ;
    EditText username,email,password,repassword;

    CircleImageView profilepic;
    Button signup;
    FirebaseAuth auth;
    Uri imageURI;
    String imageuri,itoken;
    String patternemail = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    FirebaseDatabase database;
    FirebaseStorage storage;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().hide();
        database=FirebaseDatabase.getInstance();
        storage=FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();

        loginto =findViewById(R.id.signfirst);
        username =findViewById(R.id.usernamesignup);
        email =findViewById(R.id.emailsignup);
        password =findViewById(R.id.passwordsignup);
        repassword =findViewById(R.id.passwordresignup);
        signup = findViewById(R.id.signup);
        profilepic= findViewById(R.id.profilepic);

        loginto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(signUp.this,firstScreen.class);
                startActivity(intent);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseMessaging.getInstance().getToken()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {

                                    Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                                    return;
                                }

                                // Get new FCM registration token
                                itoken = task.getResult();
                                // Log and toast


                                // Log and toast

                            }
                        });

                String namee = username.getText().toString();
                String emaill = email.getText().toString();
                String Password = password.getText().toString();
                String cPassword = repassword.getText().toString();
                String status = "Hey I am using chatUP";

                if(TextUtils.isEmpty(namee) || TextUtils.isEmpty(emaill) || TextUtils.isEmpty(Password) || TextUtils.isEmpty(cPassword)){
                    Toast.makeText(signUp.this, "Please Enter Valid Information", Toast.LENGTH_SHORT).show();
                }else if(!emaill.matches(patternemail)){
                    email.setError("Type a Valid Email here");
                } else if (Password.length()<6) {
                    password.setError("Password must be greater than 6 characters");

                } else if (!Password.equals(cPassword)) {
                    repassword.setError("Password doesn't matches");

                }else {
                    auth.createUserWithEmailAndPassword(emaill,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){

                                String id=task.getResult().getUser().getUid();
                                DatabaseReference reference= database.getReference().child("user").child(id);
                                StorageReference storageReference = storage.getReference().child("Upload").child(id);



                                if(imageURI!=null){
                                    storageReference.putFile(imageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                            if(task.isSuccessful()){
                                                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        imageuri =uri.toString();
                                                        Users users =  new Users(id,namee,emaill,Password,imageuri,status,itoken);
                                                        reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){

                                                                    Toast.makeText(signUp.this, "Your account is successfully created WELCOME to chatUP", Toast.LENGTH_SHORT).show();
                                                                    Intent intent=new Intent(signUp.this,Mainplatform.class);
                                                                    startActivity(intent);
                                                                    finish();

                                                                }else{
                                                                    Toast.makeText(signUp.this, "Error in creating your account", Toast.LENGTH_SHORT).show();
                                                                }

                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }else {
                                    String status = "Hey you are using chatUP";
                                    imageuri ="https://firebasestorage.googleapis.com/v0/b/chatup-1e48a.appspot.com/o/man.png?alt=media&token=3eff23f9-b054-44c1-8e36-debc4959685c";
                                    Users users=new Users(id,namee,emaill,Password,status,imageuri,itoken);
                                    reference.setValue(users).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Intent intent=new Intent(signUp.this,Mainplatform.class);
                                                startActivity(intent);
                                                finish();
                                            }else{
                                                Toast.makeText(signUp.this, "Error in creating your account", Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    });


                                }
                            }else {
                                Toast.makeText(signUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });



        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10){
            if(data!=null){
                imageURI =data.getData();
                profilepic.setImageURI(imageURI);

            }
        }
    }
}