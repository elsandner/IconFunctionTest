package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

import java.util.Arrays;

//This class contains contains onClick Methodes to start the designated test.
public class TestMenuActivity extends AppCompatActivity {

    private TestService testService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_menu);
        testService = TestService.getInstance();
    }

    public void onClick_bt_Test1A(View view) {
        Parameter.shuffleItems(true);
        testService.startTest(Parameter.number_of_trials, Parameter.number_of_blocks, TestMenuActivity.this,AliveActivity.class,1);
        Animatoo.animateDiagonal(TestMenuActivity.this);
    }

    public void onClick_bt_Test1B(View view) {
        Parameter.shuffleItems(false);
        testService.startTest(Parameter.number_of_trials, Parameter.number_of_blocks, TestMenuActivity.this,StandardActivity.class,2);
        Animatoo.animateDiagonal(TestMenuActivity.this);
    }

    public void onClick_bt_Test2A(View view) {
        Parameter.shuffleItems(true);
        testService.startTest(Parameter.number_of_trials, Parameter.number_of_blocks, TestMenuActivity.this,AliveActivity.class,3);
        Animatoo.animateDiagonal(TestMenuActivity.this);
    }

    public void onClick_bt_Test2B(View view) {
        Parameter.shuffleItems(false);
        testService.startTest(Parameter.number_of_trials, Parameter.number_of_blocks, TestMenuActivity.this,StandardActivity.class,4);
        Animatoo.animateDiagonal(TestMenuActivity.this);
    }

    public void onClick_bt_Test3A(View view) {
        Parameter.shuffleItems(true);
        testService.startTest(Parameter.number_of_trials, Parameter.number_of_blocks, TestMenuActivity.this,AliveActivity.class,5);
        Animatoo.animateDiagonal(TestMenuActivity.this);
    }

    public void onClick_bt_Test3B(View view) {
        Parameter.shuffleItems(false);
        testService.startTest(Parameter.number_of_trials, Parameter.number_of_blocks, TestMenuActivity.this,StandardActivity.class,6);
        Animatoo.animateDiagonal(TestMenuActivity.this);

    }





}