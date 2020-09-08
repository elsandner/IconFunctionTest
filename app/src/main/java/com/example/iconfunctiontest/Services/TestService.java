package com.example.iconfunctiontest.Services;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iconfunctiontest.Presentation.AliveActivity;
import com.example.iconfunctiontest.Presentation.MainActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TestService {
    
    private int trials=0;
    int currentTrial;
    private int targets []; //what the user should select
    private int answer []; //what he/she actually selected


    // static variable single_instance of type Singleton
    private static TestService testService = null;

    private TestService(){
        //Empty Constructor
    }

    public static TestService getInstance() {
        if(testService==null)
            testService=new TestService();
        return testService;
    }


    public void startTest(int number_of_trials,int number_of_blocks, AppCompatActivity callingActivity){
        trials=number_of_trials*number_of_blocks;
        currentTrial=0;
        targets=new int[number_of_trials];
        answer = new int[number_of_trials];
        
        //Each Target is a number between 0 and number of trials, each number needs to appear once
        for(int i=0; i<targets.length;i++){
            targets[i]=i;
        }

        //Randomize the order of the Targets
        Random rand = new Random();
        for(int i=0; i<targets.length;i++){
            int randomIndex = rand.nextInt(targets.length);
            int temp = targets[randomIndex];
            targets[randomIndex] = targets[i];
            targets[i] = temp;
        }

        Intent i = new Intent(callingActivity, AliveActivity.class);
        i.putExtra("TRIAL", "1/"+trials);
        i.putExtra("TARGET",targets[0]);
        callingActivity.startActivity(i);
    }

    public void onAnswer(int selectedOption, AppCompatActivity callingActivity){
        //wird ausgeführt, wenn die Testperson die Aufgabe ausgeführt hat (lift off)
        //TODO: Add messured time as parameter

        answer[currentTrial]=selectedOption;
        currentTrial++;
        Intent i = new Intent(callingActivity, AliveActivity.class);
        i.putExtra("TRIAL", currentTrial+"/"+trials);
        i.putExtra("TARGET",targets[currentTrial]);

        if(targets[currentTrial]==selectedOption){
            //answer was correct
            System.out.println("CORRECT");

        }
        else{
            //answer was wrong
            //TODO: add functionality to repeat this task
            System.out.println("WRONG");
        }

        callingActivity.startActivity(i);
    }
}
