package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.iconfunctiontest.R;

public class MainActivity extends AppCompatActivity {

    private Button bt_Start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bt_Start = (Button) findViewById(R.id.bt_Start);

    }

    public void onClickBt_Start(View view){
        System.out.println("Test");
    }



}