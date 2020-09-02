package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

    public void onClickBt_BlindMode(View view){
        Intent i = new Intent(MainActivity.this, BlindMode.class);
        startActivity(i);
    }

    public void onClickBt_VisualMode(View view){
        Intent i = new Intent(MainActivity.this, VisualMode.class);
        startActivity(i);
    }

    public void onClickBt_Start(View view){
        Intent i = new Intent(MainActivity.this, InfoActivity.class);

        i.putExtra("trial","Welcome");

        startActivity(i);
    }


}