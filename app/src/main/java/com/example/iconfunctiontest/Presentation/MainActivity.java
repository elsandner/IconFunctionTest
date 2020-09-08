package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

public class MainActivity extends AppCompatActivity {
    TestService testService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testService=TestService.getInstance();
    }

    public void onClickBt_Standard(View view) {
        Intent i = new Intent(MainActivity.this, StandardActivity.class);
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
        testService.startTest(Parameter.number_of_trials_2, Parameter.number_of_blocks_2, MainActivity.this);
    }

}