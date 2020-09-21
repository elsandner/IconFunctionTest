package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.google.android.material.textfield.TextInputEditText;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextInputEditText name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        name = findViewById(R.id.input_Name);
    }


    public void onClickBt_Standard(View view) {
        Intent i = new Intent(MainActivity.this, StandardActivity.class);
        i.putExtra("TRIAL", "");
        i.putExtra("TARGET",-1);
        i.putExtra("testID",0);
        startActivity(i);
        Animatoo.animateDiagonal(MainActivity.this);
    }

    public void onClickBt_Alive(View view){
        Intent i = new Intent(MainActivity.this, AliveActivity.class);
        i.putExtra("TRIAL", "");
        i.putExtra("TARGET",-1);
        i.putExtra("testID",0);
        startActivity(i);
        Animatoo.animateDiagonal(MainActivity.this);

    }

    public void onClickBt_Test(View view){
        //TODO: create new activity to enter name or find an other better solution
        //save name
        String username = name.getText().toString();
        if(username.matches("")){
            Toast.makeText(getApplicationContext(), "Enter  your name first!", Toast.LENGTH_LONG).show();
        }
        else{
            Parameter.setName(username);
            Intent i = new Intent(MainActivity.this, TestMenuActivity.class);
            startActivity(i);
            Animatoo.animateSlideUp(MainActivity.this);

            //Ask user for permission to write csv file
            if (Build.VERSION.SDK_INT >= 23) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        }

    }

}