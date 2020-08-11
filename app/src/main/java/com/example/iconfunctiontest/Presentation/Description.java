package com.example.iconfunctiontest.Presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iconfunctiontest.R;


public class Description extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test1__description);
    }


    public void onClickBt_StartTestOne(View view){
        System.out.println("Test");

        Intent i = new Intent(Description.this, firstTest.class);
        startActivity(i);
    }
}