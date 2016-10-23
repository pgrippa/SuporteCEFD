package br.ufes.cefd.suportcefd.utils;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import br.ufes.cefd.suportcefd.main.LoginActivity;
import br.ufes.cefd.suportcefd.R;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    ImageView logo;
    TextView cefd;
    Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        logo = (ImageView) findViewById(R.id.splash_logo);
        cefd = (TextView) findViewById(R.id.splash_text);

        animation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        logo.setAnimation(animation);
        cefd.setAnimation(animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        },SPLASH_TIME_OUT);
    }
}
