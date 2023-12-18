package com.example.pblapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class seeRecord extends AppCompatActivity {
    private ListView listView;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seerecord);

        listView = findViewById(R.id.ListView);
        ArrayList<Information> list = new ArrayList<>();
        CustomAdapter adapter = new CustomAdapter(list);
        listView.setAdapter(adapter);
        Intent intent = getIntent();
        String gcode = intent.getStringExtra("fromAdminPageGrpCode");

        database = FirebaseDatabase.getInstance().getReference("Groups");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot groupSnap : snapshot.getChildren()) {
                    String groupCode = String.valueOf(groupSnap.child("group_code").getValue());
                    if (groupCode.equals(gcode)) {
                        DataSnapshot collectionSnapShot = groupSnap.child("Collection");
                        for (DataSnapshot colSnap : collectionSnapShot.getChildren()) {
                            for (DataSnapshot dateSnap : colSnap.getChildren()) {
                                String nameOfMember = String.valueOf(dateSnap.child("nameOfMember").getValue());
                                String collectedAmount = String.valueOf(dateSnap.child("collected_amount").getValue());
                                String collectedDate = String.valueOf(dateSnap.child("collected_date").getValue());

                                Information info = new Information(nameOfMember, collectedAmount, collectedDate);
                                list.add(info);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(seeRecord.this, "Data Retrieved Successfully", Toast.LENGTH_SHORT).show();
                        Log.d("pbl", "hoo");
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(seeRecord.this, "Error retrieving data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private class CustomAdapter extends ArrayAdapter<Information> {
        public CustomAdapter(ArrayList<Information> list) {
            super(seeRecord.this, 0, list);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, parent, false);
            }

            Information info = getItem(position);
            TextView nameTextView = convertView.findViewById(R.id.nameTextView);
            TextView amountTextView = convertView.findViewById(R.id.amountTextView);
            TextView dateTextView = convertView.findViewById(R.id.dateTextView);

            if (info != null) {
                nameTextView.setText(info.getNameOfMember());
                amountTextView.setText(info.getCollected_amount());
                dateTextView.setText(info.getCollected_date());
            }

            return convertView;
        }
    }
}