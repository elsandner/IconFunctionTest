package com.example.iconfunctiontest.Services;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.Presentation.AliveActivity;
import com.example.iconfunctiontest.Presentation.InfoActivity;

import java.util.ArrayList;
import java.util.Random;

public class TestService {

    private int numberOfTrials;
    private int highestTrialID;
    private int highestBlockID;
    private int currentTrial;
    ArrayList<Trial> trials;

    // static variable single_instance of type Singleton
    private static TestService testService = null;

    private TestService(){
    }

    public static TestService getInstance() {
        if(testService==null)
            testService=new TestService();
        return testService;
    }


    public void startTest(int number_of_trials,int number_of_blocks, AppCompatActivity callingActivity){
        numberOfTrials=(number_of_trials*number_of_blocks)-1;
        highestTrialID=number_of_trials-1;
        highestBlockID=number_of_blocks-1;
        currentTrial=0;
        trials=new ArrayList<>();

        //The following two loops create an array list filled with all trials of the total subtest
        int blockCounter=1;
        for(int i=0; i<number_of_blocks;i++){
            //Each Target is a number between 0 and number_of_trials, each number needs to appear once
            int [] shuffledTargetBlock=getShuffledTargetBlock(number_of_trials);
            for(int j=0; j<number_of_trials;j++){
                   trials.add(new Trial(j,i,shuffledTargetBlock[j]));

                   if(j==highestTrialID&&blockCounter==Parameter.blocksBetweenBreak) {
                       trials.get(trials.size()-1).setDoBreak(true);
                       blockCounter=0;
                   }
            }
            blockCounter++;
        }


        Intent i = new Intent(callingActivity, AliveActivity.class);
        i.putExtra("TRIAL", "1/"+ trials.size());    //+1 because index starts with 0
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


    //TODO: Add messured time as parameter
    //TODO: Add positive/negative sound
    //TODO: Add break after x blocks
    //wird ausgeführt, wenn die Testperson die Aufgabe ausgeführt hat (lift off)
    public void onAnswer(int selectedOption, final AppCompatActivity callingActivity){

        if(currentTrial<trials.size()-1) {
            if(trials.get(currentTrial).setAnswer(selectedOption))//answer was correct
                Toast.makeText(callingActivity, "correct", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(callingActivity, "wrong", Toast.LENGTH_SHORT).show();
                addTrialAgain(currentTrial);
            }


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
                    Thread.sleep(Parameter.nextActivity_Delay); //to wait till toast finishes
                    final Intent i;

                    if(finish){
                        i = new Intent(callingActivity, InfoActivity.class);
                        i.putExtra("HEADING", "Finish");
                        i.putExtra("EXPLANATION", "Test 2A is done. Thank you very much!");
                    }

                    else if(trials.get(currentTrial-1).isDoBreak()){ // -1 because it allready got increased
                        //doBreak
                        i = new Intent(callingActivity, InfoActivity.class);
                        i.putExtra("HEADING", "Break");
                        i.putExtra("EXPLANATION", "Enjoy your break. Press continue when you want to go on with the test.");

                        i.putExtra("TRIAL", (currentTrial + 1) + "/" + trials.size());
                        i.putExtra("TARGET", trials.get(currentTrial).getTarget());
                        i.putExtra("testID", 2);
                    }
                    else{
                        //continue with next trial
                        i = new Intent(callingActivity, AliveActivity.class);
                        i.putExtra("TRIAL", (currentTrial + 1) + "/" + trials.size());
                        i.putExtra("TARGET", trials.get(currentTrial).getTarget());
                        i.putExtra("testID", 2);
                    }

                    callingActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callingActivity.startActivity(i);
                            Animatoo.animateZoom(callingActivity);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

    }

    private void addTrialAgain(int currentTrial){

        Trial trial = trials.get(currentTrial);
        int blockID = trial.getBlockID();

        if(blockID==highestBlockID){
            trials.add(trials.get(currentTrial)); //if Element in last block - add on end of list
        }
        else {
            int i=currentTrial;
            boolean x = true;
            while(x) {
                if (trials.get(i).getBlockID() != blockID) {
                    //Adds Trial to position of first element of following block and shift all following elements to the "right"
                    Trial newTrial = Trial.clone(trials.get(currentTrial)); //copy by value not reference!!
                    trials.add(i, newTrial);
                    trials.get(i-1).setDoBreak(false);
                    trials.get(i).setDoBreak(true);
                    x = false;
                }
                i++;
            }


        }


    }

}
