package com.example.pblapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class createGrp extends AppCompat {
    Button eng,mar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_grp);
        eng = findViewById(R.id.engbtn);
        mar = findViewById(R.id.marbtn);
        LanguageManager lang = new LanguageManager(this);
        eng.setOnClickListener(view -> {
            lang.updateResource("en");
            recreate();
        });

        mar.setOnClickListener(view -> {
            lang.updateResource("mr");
            recreate();
        });
    }
    public void create(View view) {
        Intent i = new Intent(createGrp.this, group_registration.class);
        startActivity(i);

    }

    public void go(View view) {
        Intent i = new Intent(createGrp.this, login_page.class);
        startActivity(i);
    }
}