package com.example.funfitnessblender.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.funfitnessblender.R;

public class AdminActivity extends AppCompatActivity {

    private LinearLayout addPrgramLL,addAreaLL;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Hide the status bar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        addPrgramLL = findViewById(R.id.addPrgramLL);
        addAreaLL = findViewById(R.id.addAreaLL);
        backBtn = findViewById(R.id.backBtn);




        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        addAreaLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddAreaActivity.class);
                startActivity(intent);
            }
        });

        addPrgramLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminActivity.this, AddProgramActivity.class);
                startActivity(intent);
            }
        });

    }
}