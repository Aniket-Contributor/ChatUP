package com.example.chatup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView chatup, tO, welcomE, froM, anikeT;
    Animation topanimation, bottomanimation;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        chatup = findViewById(R.id.chatUPP);
        tO = findViewById(R.id.tO);
        welcomE = findViewById(R.id.welcomE);
        froM = findViewById(R.id.froM);
        anikeT = findViewById(R.id.anikeT);

        topanimation = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bottomanimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);

        welcomE.setAnimation(topanimation);
        tO.setAnimation(topanimation);
        chatup.setAnimation(topanimation);
        froM.setAnimation(bottomanimation);
        anikeT.setAnimation(bottomanimation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, firstScreen.class);
                startActivity(intent);
                finish();

            }
        }, 3000);


    }
}

