package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;

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
    }

    public void onClickBt_Alive(View view){
        Intent i = new Intent(MainActivity.this, AliveActivity.class);
        i.putExtra("TRIAL", "");
        i.putExtra("TARGET",-1);
        i.putExtra("testID",0);
        startActivity(i);

    }

    public void onClickBt_Test(View view){
        Intent i = new Intent(MainActivity.this, TestMenuActivity.class);
        startActivity(i);
        Animatoo.animateSwipeLeft(MainActivity.this);
    }

}