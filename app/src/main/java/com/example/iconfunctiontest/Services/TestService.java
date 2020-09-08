package com.example.iconfunctiontest.Services;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iconfunctiontest.Presentation.AliveActivity;

import java.util.ArrayList;
import java.util.Random;

public class TestService {

    private int numberOfTrials;
    int currentTrial;
    ArrayList<Trial> trials;

    // static variable single_instance of type Singleton
    private static TestService testService = null;

    private TestService(){
        //Empty Constructor
        currentTrial=0;
        trials=new ArrayList<>();
    }

    public static TestService getInstance() {
        if(testService==null)
            testService=new TestService();
        return testService;
    }


    public void startTest(int number_of_trials,int number_of_blocks, AppCompatActivity callingActivity){
        numberOfTrials=number_of_trials*number_of_blocks;
        //The following two loops create an array list filled with all trials of the total subtest
        for(int i=0; i<number_of_blocks;i++){
            //Each Target is a number between 0 and number_of_trials, each number needs to appear once
            int [] shuffledTargetBlock=getShuffledTargetBlock(number_of_trials);
            for(int j=0; j<number_of_trials;j++){
                   trials.add(new Trial(j,i,shuffledTargetBlock[j]));
            }
        }

        Intent i = new Intent(callingActivity, AliveActivity.class);
        i.putExtra("TRIAL", "1/"+ numberOfTrials);
        i.putExtra("TARGET",trials.get(currentTrial).getTarget());
        callingActivity.startActivity(i);
    }

    private int[] getShuffledTargetBlock(int size){

        int[] array = new int[size];
        for(int i=0; i<array.length;i++){
            array[i]=i;
        }

        //Randomize the order of the Targets
        Random rand = new Random();
        for(int i=0; i<array.length;i++){
            int randomIndex = rand.nextInt(array.length);
            int temp = array[randomIndex];
            array[randomIndex] = array[i];
            array[i] = temp;
        }
        return array;
    }

    public void onAnswer(int selectedOption, AppCompatActivity callingActivity){
        //wird ausgeführt, wenn die Testperson die Aufgabe ausgeführt hat (lift off)
        //TODO: Add messured time as parameter
        //TODO: Add positive/negative sound
        //TODO: Add delay, that toast is seen on original trial
        //TODO: Add trial again if answered wrong



        if(trials.get(currentTrial).setAnswer(selectedOption)){
            //answer was correct
            System.out.println("CORRECT");

        }
        else{
            //answer was wrong
            //TODO: add functionality to repeat this task
            System.out.println("WRONG");
        }

        currentTrial++;
        Intent i = new Intent(callingActivity, AliveActivity.class);
        i.putExtra("TRIAL", currentTrial+"/"+ numberOfTrials);
        i.putExtra("TARGET",trials.get(currentTrial++).getTarget());

        callingActivity.startActivity(i);
    }
}
