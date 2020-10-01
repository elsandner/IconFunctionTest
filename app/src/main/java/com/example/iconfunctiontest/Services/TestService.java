package com.example.iconfunctiontest.Services;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.Presentation.AliveActivity;
import com.example.iconfunctiontest.Presentation.InfoActivity;
import com.example.iconfunctiontest.Presentation.StandardActivity;
import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class TestService {

    private int highestBlockID;
    private int currentTrial;
    ArrayList<Trial> trials;
    private Class nextClass;
    private int testID;


    // static variable single_instance of type Singleton
    private static TestService testService = null;

    private TestService(){

    }

    public static TestService getInstance() {
        if(testService ==null)
            testService =new TestService();
        return testService;
    }

    public void startTest(int number_of_trials,int number_of_blocks, AppCompatActivity callingActivity, Class nextClass, int testID){
        this.nextClass=nextClass;
        this.testID=testID;
        highestBlockID=number_of_blocks-1;
        currentTrial=0;
        trials=new ArrayList<>();

        if(testID>4)    //Test3A (ID=5), Test3B(ID=6)
           number_of_trials=number_of_trials*4;

        //The following two loops create an array list filled with all trials of the total subtest
        int blockCounter=1;//needed for setting break-flag
        for(int i=0; i<number_of_blocks;i++){
            //Each Target is a number between 0 and number_of_trials, each number needs to appear once
            int [] shuffledTargetBlock= shuffleIntArray(number_of_trials);
            for(int j=0; j<number_of_trials;j++){
                trials.add(new Trial(j,i,shuffledTargetBlock[j]));

                if(j==number_of_trials-1&&blockCounter==Parameter.blocksBetweenBreak) {
                    trials.get(trials.size()-1).setDoBreak(true);
                    blockCounter=0;
                }
            }
            blockCounter++;
        }

        Intent i = new Intent(callingActivity, nextClass);
        i.putExtra("TRIAL", "1/"+ trials.size());    //+1 because index starts with 0
        i.putExtra("TARGET",trials.get(currentTrial).getTarget());
        i.putExtra("testID", testID);
        callingActivity.startActivity(i);


    }

    public int[] shuffleIntArray(int size){

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

    //wird ausgeführt, wenn die Testperson die Aufgabe ausgeführt hat (lift off)
    public void onAnswer(int selectedOption, final AppCompatActivity callingActivity, long time_wait, long time_move, double downX, double downY, double upX, double upY){

        trials.get(currentTrial).setTime_wait(time_wait);
        trials.get(currentTrial).setTime_execute(time_move);

        trials.get(currentTrial).setDownX(downX);
        trials.get(currentTrial).setDownY(downY);
        trials.get(currentTrial).setUpX(upX);
        trials.get(currentTrial).setUpY(upY);

        if (trials.get(currentTrial).setAnswer(selectedOption)){//answer was correct
            callFeedbackOnAnswer(testID, true);
        }
        else {//answer was wrong
            addTrialAgain(currentTrial);
            callFeedbackOnAnswer(testID, false);
        }

        if(currentTrial<trials.size()-1) {
            currentTrial++;
            nextActivity(callingActivity,false);
        }
        else{//Finish
            trials.get(currentTrial).setAnswer(selectedOption);
            nextActivity(callingActivity,true);
        }

    }

    private void callFeedbackOnAnswer(int testID, boolean answer){
        if(testID==2||testID==4||testID==6) {
            StandardActivity.feedbackOnAnswer(answer);
        }
        else{
            AliveActivity.feedbackOnAnswer(answer);
        }
    }

    private void nextActivity(final AppCompatActivity callingActivity, final boolean finish){

        final Thread thread = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    Thread.sleep(Parameter.nextActivity_Delay); //to wait till toast finishes
                    final Intent i;

                    if(finish){
                        i = new Intent(callingActivity, InfoActivity.class);
                        i.putExtra("HEADING", "Finish");
                        i.putExtra("EXPLANATION", "Test is done. Thank you very much!");
                        createCSV(callingActivity);
                    }

                    else if(trials.get(currentTrial-1).isDoBreak()){ // -1 because it allready got increased
                        //doBreak
                        i = new Intent(callingActivity, InfoActivity.class);
                        i.putExtra("HEADING", "Break");
                        i.putExtra("EXPLANATION", "Press continue when you want to go on with the test.");

                        i.putExtra("TRIAL", (currentTrial + 1) + "/" + trials.size());
                        i.putExtra("TARGET", trials.get(currentTrial).getTarget());
                        i.putExtra("testID", testID);
                    }
                    else{
                        //continue with next trial
                        i = new Intent(callingActivity, nextClass);
                        i.putExtra("TRIAL", (currentTrial + 1) + "/" + trials.size());
                        i.putExtra("TARGET", trials.get(currentTrial).getTarget());
                        i.putExtra("testID", testID);
                    }

                    callingActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            callingActivity.startActivity(i);
                            Animatoo.animateSlideLeft(callingActivity);
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

        if(blockID==highestBlockID){//if Element in last block - add on end of list
            Trial newTrial = Trial.clone(trials.get(currentTrial)); //copy by value not reference!!
            trials.add(newTrial);
            trials.get(trials.size()-2).setDoBreak(false);
            trials.get(trials.size()-1).setDoBreak(true);
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createCSV(final AppCompatActivity callingActivity){

        String FILENAME="/logfile_Test"+testID+"_"+Parameter.getName()+".csv";//TestID, Name
        String csv = (Environment.getExternalStorageDirectory().getAbsolutePath() +FILENAME); // Here csv file name is MyCsvFile.csv

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());

        String iconType="Alive";
        if(testID%2==0){    //Test ID is Even --> 2,4,6 =Alive Icon
            iconType="Standard";
        }

        String [] testInfo={"Participant:", Parameter.getName(),"", "Date:",date.toString(), "","Icon Type:", iconType,"","Test Type:",getTestType(testID)};
        String [] blankLine={};
        String [] headingStandard={"Trial","Trial ID", "Block ID","Repetition","Target Item", "Selected Item","Answer", "Prepare Time", "Execution Time"};
        String[] headingAlive={"Trial","Trial ID", "Block ID","Repetition","Target Item", "Selected Item","Answer", "Prepare Time", "Execution Time", "Touch Down X", "Touch Down Y", "Lift Off X", "Lift Off Y", "Swipe Distance [mm]"}; //TODO: Add swipe distance
        String trial, trialID, blockID,repetition, targetItem, selectedItem, answer, prepareTime, executeTime, downX,downY,upX,upY,swipeDistance;
        CSVWriter writer = null;
        try {
            writer = new CSVWriter(new FileWriter(csv));

            List<String[]> data = new ArrayList<String[]>();
            data.add(testInfo);
            data.add(blankLine);

            if(testID%2==0)    //Test ID is Even --> 2,4,6 =Standard Icon
                data.add(headingStandard);
            else
                data.add(headingAlive);



            int n=0;
            for(Trial cT: trials){//cT...current Trial
                n++;
                trial=Integer.toString(n);
                trialID=Integer.toString(cT.getTrialID());
                blockID=Integer.toString(cT.getBlockID());
                repetition=countRepetitions((n-1));

                targetItem = Parameter.Items[cT.getTarget()];

                if(cT.getAnswer()==-1)
                    selectedItem="Cancel";
                else
                    selectedItem = Parameter.Items[cT.getAnswer()];

                if(targetItem.equals(selectedItem))
                    answer="correct";
                else
                    answer="wrong";
                prepareTime = Long.toString(cT.getTime_wait());
                executeTime = Long.toString(cT.getTime_execute());

                if(testID%2==1){ //Only for Alive Icon
                    downX=Double.toString(cT.getDownX());
                    downY=Double.toString(cT.getDownX());
                    upX=Double.toString(cT.getUpX());
                    upY=Double.toString(cT.getUpY());
                    swipeDistance=convertPixelToMilimeters(cT.getSwipeDistance(),callingActivity);
                    data.add(new String[]{trial,trialID,blockID,repetition,targetItem,selectedItem,answer,prepareTime,executeTime,downX,downY,upX,upY,swipeDistance});
                }
                else
                    data.add(new String[]{trial,trialID,blockID,repetition,targetItem,selectedItem,answer,prepareTime,executeTime});

            }


            writer.writeAll(data); // data is adding to csv
            writer.close();
            //callRead();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("executed write csv from TestService");
    }

    private String countRepetitions(int refIndex){
        int repetition=1;
        Trial trial = trials.get(refIndex);
        int refTrialID=trial.getTrialID();
        int refBlockID=trial.getBlockID();

        for(int i=0;i<refIndex;i++){
            Trial currentTrial = trials.get(i);
            int currentTrialID=currentTrial.getTrialID();
            int currentBlockID=currentTrial.getBlockID();

            if(refTrialID==currentTrialID&&refBlockID==currentBlockID){
                repetition++;
            }
        }
        return Integer.toString(repetition);
    }

    private String convertPixelToMilimeters(double pixelValue, final AppCompatActivity callingActivity){
        final DisplayMetrics dm = callingActivity.getResources().getDisplayMetrics();
        double mmValue= pixelValue / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
        return Double.toString(mmValue);
    }

    private String getTestType(int testID){
        switch(testID){
            case 1:
            case 2: return "Novice User";
            case 3:
            case 4: return "Expert User";
            case 5:
            case 6: return "Learning Performance";
        }
        return "invalid input";
    }

}
