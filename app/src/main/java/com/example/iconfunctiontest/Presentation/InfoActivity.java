package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;

public class InfoActivity extends AppCompatActivity {

    private Bundle bundle;
    private TextView tV_heading, tV_explanation;

    //Values to pass to trial after break
    private String trial;
    private int target, testID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        bundle = getIntent().getExtras();

        tV_heading = (TextView) findViewById(R.id.tV_heading);
        tV_heading.setText("Break Screen");

        tV_explanation =(TextView) findViewById(R.id.tV_explanation);
        tV_explanation.setText("Now do a break as long as you want. Press  the button to continue");

        if(bundle!=null){
            tV_heading.setText(bundle.getString("HEADING"));
            tV_explanation.setText(bundle.getString("EXPLANATION"));

            trial=bundle.getString("TRIAL");
            target=bundle.getInt("TARGET");
            testID=bundle.getInt("testID");
        }

    }

    public void onClickBt_Start(View view){
        final Intent i;
        if(trial==null) {   //Finish-Screen
            i = new Intent(InfoActivity.this, MainActivity.class);
            i.putExtra("USERNAME", Parameter.getName());
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    Animatoo.animateDiagonal(InfoActivity.this);
                }
            });

        }else {

            if(testID%2==0) // 2,4,6 ... =Standard icon
                i = new Intent(InfoActivity.this, StandardActivity.class);
            else
                i = new Intent(InfoActivity.this, AliveActivity.class);

            i.putExtra("TRIAL", trial);
            i.putExtra("TARGET", target);
            i.putExtra("testID", testID);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(i);
                    Animatoo.animateSlideLeft(InfoActivity.this);
                }
            });
        }

    }
}