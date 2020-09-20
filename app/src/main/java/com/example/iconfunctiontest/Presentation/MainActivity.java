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
import android.view.View;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickBt_Standard(View view) {
        Intent i = new Intent(MainActivity.this, StandardActivity.class);
        i.putExtra("TRIAL", "");
        i.putExtra("TARGET",-1);
        i.putExtra("testID",0);
        startActivity(i);
        Animatoo.animateDiagonal(MainActivity.this);
        writeXML();
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

    ////////////////////////////////////
    //just fot test purpose
    public void writeXML(){
        //Ask for run-time permission

        String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() + "/logfile.csv"); // Here csv file name is MyCsvFile.csv
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<String[]>();
            data.add(new String[]{"Country", "Capital"});
            data.add(new String[]{"India", "New Delhi"});
            data.add(new String[]{"United States", "Washington D.C"});
            data.add(new String[]{"Germany", "Berlin"});

            writer.writeAll(data); // data is adding to csv

            writer.close();
            //callRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Saved Data");
        System.out.println(csv);
    }
    //////////////////////////////////////7

}