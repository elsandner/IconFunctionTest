package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService2;

public class TestMenuActivity extends AppCompatActivity {

    private TestService2 testService2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);
        testService2 = TestService2.getInstance();
    }

    public void onClick_bt_Test1A(View view) {
        Intent i = new Intent( TestMenuActivity.this, MainActivity.class);
        startActivity(i);
        Animatoo.animateCard(TestMenuActivity.this);
    }

    public void onClick_bt_Test1B(View view) {
        Intent i = new Intent( TestMenuActivity.this, MainActivity.class);
        startActivity(i);
        Animatoo.animateFade(TestMenuActivity.this);
    }

    public void onClick_bt_Test2A(View view) {
        testService2.startTest(Parameter.number_of_trials_2, Parameter.number_of_blocks_2, TestMenuActivity.this,AliveActivity.class,3);
    }

    public void onClick_bt_Test2B(View view) {
        testService2.startTest(Parameter.number_of_trials_2, Parameter.number_of_blocks_2, TestMenuActivity.this,StandardActivity.class,4);
    }

    public void onClick_bt_Test3A(View view) {
        Intent i = new Intent( TestMenuActivity.this, MainActivity.class);
        startActivity(i);
        Animatoo.animateSplit(TestMenuActivity.this);
    }

    public void onClick_bt_Test3B(View view) {
        Intent i = new Intent( TestMenuActivity.this, MainActivity.class);
        startActivity(i);
        Animatoo.animateSwipeRight(TestMenuActivity.this);
    }
}