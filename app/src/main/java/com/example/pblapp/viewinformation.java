package com.example.pblapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class viewinformation extends AppCompatActivity {
    Button editProfile,groupinfo;
    TextView groupnamev;
    TextView adminnamev;
    private String grpCode;
    private String grpName;
    private String userName;
    TextView totalamtedit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewinformation);

        totalamtedit = findViewById(R.id.totalamtedit);
        groupnamev = findViewById(R.id.groupnamev);
        adminnamev = findViewById(R.id.adminnamev);
        editProfile = findViewById(R.id.btnprofilesetting);
        groupinfo = findViewById(R.id.btnbachatgatinfo);

        Intent intent = getIntent();
        grpCode = intent.getStringExtra("passGrpCode");
        grpName = intent.getStringExtra("passGrpName");
        userName = intent.getStringExtra("passUserName");

        groupnamev.append(grpName + " [ " + grpCode + " ]");
        adminnamev.append(" " + userName);


        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editprofileIntent ;
                editprofileIntent = new Intent(viewinformation.this,profilesettings.class);
                editprofileIntent.putExtra("passGrpCode", grpCode);
                editprofileIntent.putExtra("passGrpName", grpName);
                editprofileIntent.putExtra("passUserName", userName);
                startActivity(editprofileIntent);
            }
        });

        groupinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent grpsetting = new Intent(viewinformation.this,GroupInformation.class);
                grpsetting.putExtra("passGrpCode", grpCode);
                grpsetting.putExtra("passGrpName", grpName);
                grpsetting.putExtra("passUserName", userName);

                startActivity(grpsetting);
            }
        });
    }

}