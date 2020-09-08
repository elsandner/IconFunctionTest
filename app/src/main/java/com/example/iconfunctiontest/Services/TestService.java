package com.example.iconfunctiontest.Services;

import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.Presentation.AliveActivity;
import com.example.iconfunctiontest.Presentation.InfoActivity;

import java.util.ArrayList;
import java.util.Random;

public class TestService {

    private int numberOfTrials;
    private int currentTrial;
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
        numberOfTrials=(number_of_trials*number_of_blocks)-1;
        //The following two loops create an array list filled with all trials of the total subtest
        for(int i=0; i<number_of_blocks;i++){
            //Each Target is a number between 0 and number_of_trials, each number needs to appear once
            int [] shuffledTargetBlock=getShuffledTargetBlock(number_of_trials);
            for(int j=0; j<number_of_trials;j++){
                   trials.add(new Trial(j,i,shuffledTargetBlock[j]));
            }
        }

        //TODO: create method to call next activity
        Intent i = new Intent(callingActivity, AliveActivity.class);
        i.putExtra("TRIAL", "1/"+ (numberOfTrials+1));    //+1 because index starts with 0
        i.putExtra("TARGET",trials.get(currentTrial).getTarget());
        i.putExtra("testID", 2);
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

    public void onAnswer(int selectedOption, final AppCompatActivity callingActivity){
        //wird ausgeführt, wenn die Testperson die Aufgabe ausgeführt hat (lift off)
        //TODO: Add messured time as parameter
        //TODO: Add positive/negative sound
        //TODO: Add trial again if answered wrong
        //TODO: Add break after x blocks

        System.out.println("Executed onAnswer!\tcurrentTrial="+currentTrial);

        if(currentTrial<numberOfTrials) {
            if(trials.get(currentTrial).setAnswer(selectedOption))//answer was correct
                Toast.makeText(callingActivity, "correct", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(callingActivity, "wrong", Toast.LENGTH_SHORT).show();

            currentTrial++;
            nextActivity(callingActivity,false);

        }
        else{
            nextActivity(callingActivity,true);
        }
    }

    private void nextActivity(final AppCompatActivity callingActivity, final boolean finish){

        final Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(2000); //to wait till toast finishes
                    Intent i;
                    if(!finish) {
                        i = new Intent(callingActivity, AliveActivity.class);
                        i.putExtra("TRIAL", (currentTrial + 1) + "/" + (numberOfTrials + 1));
                        i.putExtra("TARGET", trials.get(currentTrial).getTarget());
                        i.putExtra("testID", 2);
                    }
                    else{
                        i = new Intent(callingActivity, InfoActivity.class);
                        i.putExtra("HEADING", "Finish");
                        i.putExtra("EXPLANATION", "Test 2A is done. Thank you very much!");
                    }
                    callingActivity.startActivity(i);
                    Animatoo.animateInAndOut(callingActivity.getApplicationContext());



                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }
}
