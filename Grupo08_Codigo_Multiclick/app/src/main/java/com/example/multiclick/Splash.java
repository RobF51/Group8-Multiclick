package com.example.multiclick;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Splash extends AppCompatActivity implements Animation.AnimationListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        ImageView splashLogo = (ImageView) findViewById(R.id.splashLogo);
        Animation animSplashLogo = AnimationUtils.loadAnimation(this, R.anim.anim_logo);
        BounceInterpolator interpolator = new BounceInterpolator(0.2,20);
        animSplashLogo.setInterpolator(interpolator);
        splashLogo.startAnimation(animSplashLogo);


        ImageView splashTextLogo = (ImageView) findViewById(R.id.splashTextLogo);
        Animation animSplashTextLogo = AnimationUtils.loadAnimation(this, R.anim.anim_textlogo);


        splashTextLogo.startAnimation(animSplashTextLogo);

        animSplashLogo.setAnimationListener(this);


    }


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        Intent intent = new Intent(Splash.this,AccountType.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}