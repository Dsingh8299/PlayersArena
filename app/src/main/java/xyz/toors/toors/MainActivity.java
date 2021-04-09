package xyz.toors.toors;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class MainActivity extends AppCompatActivity {
   Animation topanim,bottmanim;
   ImageView splashimg;
   TextView logotxt,slogan;
   private static int SPLASH_SCREEN = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        topanim= AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottmanim=AnimationUtils.loadAnimation(this,R.anim.bottom_animation);
        splashimg=findViewById(R.id.splashimg);
        logotxt=findViewById(R.id.logotxt);
        slogan=findViewById(R.id.slogan);

        splashimg.setAnimation(topanim);
        logotxt.setAnimation(bottmanim);
        slogan.setAnimation(bottmanim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
                finish();
            }
        },SPLASH_SCREEN);


    }



}

