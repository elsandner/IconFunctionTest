package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iconfunctiontest.R;

import com.example.iconfunctiontest.Services.TestService;

public class InfoActivity extends AppCompatActivity {

    private Bundle bundle;
    private TextView tV_heading, tV_explanation;
    private String trial;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        bundle = getIntent().getExtras();

        if(bundle!=null)
            trial = bundle.getString("trial");

        tV_heading = (TextView) findViewById(R.id.tV_heading);
        tV_heading.setText("Break Screen");

        tV_explanation =(TextView) findViewById(R.id.tV_explanation);
        tV_explanation.setText("Now do a break as long as you want. Press  the button to continue");

    }

    public void onClickBt_Start(View view){
        Intent i = new Intent(InfoActivity.this, InfoActivity.class);
        i.putExtra("trial","Test 1");
        startActivity(i);
    }
}