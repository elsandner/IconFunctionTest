package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.TestMode;
import com.example.iconfunctiontest.Services.TestService;

public class InfoActivity extends AppCompatActivity {

    private Bundle bundle;
    private TextView tV_heading, tV_explanation;
    private TestService testService;
    private String trial;
    private TestMode testMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        testService=new TestService();
        bundle = getIntent().getExtras();

        if(bundle!=null)
            trial = bundle.getString("trial");

        testMode=TestMode.getTestMode(trial);

        tV_heading = (TextView) findViewById(R.id.tV_heading);
        tV_heading.setText("Break Screen");

        tV_explanation =(TextView) findViewById(R.id.tV_explanation);
        tV_explanation.setText("Now do a break as long as you want. Press  the button to continue");

    }

    public void onClickBt_Start(View view){
        Intent i;

        switch(testMode){
            case Welcome:
                i = new Intent(InfoActivity.this, InfoActivity.class);
                break;
            case VisualMode:
                i = new Intent(InfoActivity.this, AliveActivity.class);
                break;
            case BlindMode:
                i = new Intent(InfoActivity.this, BlindMode.class);
                break;
            case End:
                i = new Intent(InfoActivity.this, InfoActivity.class);
                break;
            default:
                i = new Intent(InfoActivity.this, InfoActivity.class);
                break;
        }
        i.putExtra("trial","Test 1");
        startActivity(i);
    }
}