package com.example.pblapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
public class MainActivity extends AppCompatActivity {
    Animation top_anim, bott_anim;
    ImageView img;
    TextView titleTv, sloganTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        top_anim = AnimationUtils.loadAnimation(this, R.anim.top_animation);
        bott_anim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation);
        img = findViewById(R.id.img1);
        titleTv = findViewById(R.id.title);
        sloganTv = findViewById(R.id.slogan);

        img.setAnimation(top_anim);
        titleTv.setAnimation(bott_anim);
        sloganTv.setAnimation(bott_anim);
        MyThread mt = new MyThread();
        mt.start();
    }

    class MyThread extends Thread
    {
        @Override
        public void run() {
            try {
                sleep(1000);
                Intent i = new Intent(MainActivity.this, createGrp.class);
                startActivity(i);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }
    }//MyThread
}
