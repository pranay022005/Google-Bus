package com.example.googlebus;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    TextView tvwelcom1, tvgooglebus;
    Animation fadeInAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        tvwelcom1 = findViewById(R.id.tvwelcome1);
        tvgooglebus = findViewById(R.id.tvgooglebus);

        fadeInAnim = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.fadein);
        tvwelcom1.setAnimation(fadeInAnim);

        fadeInAnim = AnimationUtils.loadAnimation(SplashActivity.this,R.anim.fadein);
        tvgooglebus.setAnimation(fadeInAnim);

        Animation myanimation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.animation);
        tvwelcom1.setAnimation(myanimation);

        Animation myanimation1 = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.animation1);
        tvgooglebus.startAnimation(myanimation1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 3000);
    }
}