package com.example.iconfunctiontest.Services;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.Presentation.AliveActivity;
import com.example.iconfunctiontest.Presentation.InfoActivity;
import com.example.iconfunctiontest.Presentation.MainActivity;
import com.example.iconfunctiontest.Presentation.StandardActivity;
import com.opencsv.CSVWriter;

import org.w3c.dom.ls.LSOutput;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.jar.JarOutputStream;

import static java.lang.Math.abs;

public class TestService {

    private int highestBlockID;
    private int currentTrial;
    ArrayList<Trial> trials;
    private Class nextClass;
    private int testID;

    // static variable single_instance of type Singleton
    private static TestService testService = null;

    //empty private constructor (Singleton Pattern)
    private TestService(){

    }

    //get Singleton instance
    public static TestService getInstance() {
        if(testService ==null)
            testService =new TestService();
        return testService;
    }

    //This method is called when the test beginns, depending on parameters and test type it creates all needed trials for the test and opens the Alive or Standard Activity
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

    //used to randomize the order of the targets
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

    //This method is executed when participant hit his selection
    //It receives all parameters which are tracked and stores them into the trials ArrayList (from where it gets readed for logging)
    //also this method calls the designated feedback method or the designated next activity
    public void onAnswer(int selectedOption, final AppCompatActivity callingActivity,
                         long time_wait, long time_move,
                         double downX, double downY,
                         double upX, double upY,
                         ArrayList<Long> logMovement_Timestamp, ArrayList<Float>logMovement_Coordinate_X, ArrayList<Float> logMovement_Coordinate_Y, ArrayList<Integer> logMovement_Visiteditems){

        trials.get(currentTrial).setTime_wait(time_wait);
        trials.get(currentTrial).setTime_execute(time_move);

        trials.get(currentTrial).setDownX(downX);
        trials.get(currentTrial).setDownY(downY);
        trials.get(currentTrial).setUpX(upX);
        trials.get(currentTrial).setUpY(upY);

        trials.get(currentTrial).setLogMovement_Timestamp(logMovement_Timestamp);
        trials.get(currentTrial).setLogMovement_Coordinate_X(logMovement_Coordinate_X);
        trials.get(currentTrial).setLogMovement_Coordinate_Y(logMovement_Coordinate_Y);
        trials.get(currentTrial).setLogMovement_VisitedItems(logMovement_Visiteditems);


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

    //calls the designated feedbackOnAnswer method which needs to be a element of the Activity class.
    private void callFeedbackOnAnswer(int testID, boolean answer){
        if(testID==2||testID==4||testID==6) {
            StandardActivity.feedbackOnAnswer(answer);
        }
        else{
            AliveActivity.feedbackOnAnswer(answer);
        }
    }

    //This Method manages to call the designated next Activity and pass all needed parameters to it
    private void nextActivity(final AppCompatActivity callingActivity, final boolean finish){

        final Thread thread = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    final Intent i;
                    if(finish){
                        createCSV_Main(callingActivity);
                        i = new Intent(callingActivity, InfoActivity.class);
                        i.putExtra("HEADING", "Finish");
                        i.putExtra("EXPLANATION", "Test is done. Thank you very much!");
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

                    Thread.sleep(Parameter.nextActivity_Delay); //To see feedback

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

    //If participant hits the wrong answer, the trial is added to the end of the current block again
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
    //This Method Manages the logging of the main file
    //it gets the data from all trial Objects of the test and writes it into a CSV file
    //each entry contains information of one trial
    //This CSV file into the root folder of the phones file system
    private void createCSV_Main(final AppCompatActivity callingActivity){

        String [] headingStandard=  {"Key","User ID", "Icon Type","Test Type","Trial ID", "Block ID","Repetition","Target Item", "Target Index", "Selected Item", "Selected Index", "Answer", "Used Finger", "Prepare Time", "Execution Time"};
        String[] headingAlive=      {"Key","User ID", "Icon Type","Test Type","Trial ID", "Block ID","Repetition","Target Item", "Target Index", "Selected Item", "Selected Index", "Answer","Used Finger", "Prepare Time", "Execution Time", "Touch Down X", "Touch Down Y", "Lift Off X", "Lift Off Y", "Swipe Distance","Travel Distance","Visited Items", "Nr of Visits","Mode"};
        String key,userID, iconType,testType, trialID, blockID,repetition, targetItem, targetIndex, selectedItem, selectedIndex, answer, usedFinger, prepareTime, executeTime, downX,downY,upX,upY,swipeDistance,travelDistance, VisitedItems,NrVisits, Mode;

        userID=Parameter.getUserID();
        String time = "DATE_TIME";
        DateTimeFormatter formatter;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime localDateTime = LocalDateTime.now();
            formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy_hh:mm");
            time=formatter.format(localDateTime);
        }
        String FILENAME_Main="/MAIN_Test"+testID+"_ID"+Parameter.getUserID()+"_"+time+".csv";
        String PATH_Main = (Environment.getExternalStorageDirectory().getAbsolutePath() +FILENAME_Main);

        System.out.println("PATH_Main: "+PATH_Main+" !!!");

        //Write coordinate log file and get travelDistances
        ArrayList<Double> travelDistances = new ArrayList<>();
        if(testID%2==1)
            travelDistances=createCSV_Coordinate(callingActivity, time);

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(PATH_Main),
                    Parameter.seperatorCSV,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );

                                            List<String[]> data = new ArrayList<>();

                                            if(testID%2==0)    //Test ID is Even --> 2,4,6 =Standard Icon
                                                data.add(headingStandard);
                                            else
                                                data.add(headingAlive);

                                            int n=0;
                                            for(Trial cT: trials){//cT...current Trial
                                                n++;
                                                key=Integer.toString(n);

                                                iconType="Alive";
                                                if(testID%2==0)   //Test ID is Even --> 2,4,6 =Standard Icon
                                                    iconType="Standard";

                                                testType=getTestType(testID);
                                                trialID=Integer.toString(cT.getTrialID());
                                                blockID=Integer.toString(cT.getBlockID());
                                                repetition=countRepetitions((n-1));
                                                targetItem = Parameter.Items[cT.getTarget()];
                                                targetIndex=Integer.toString(cT.getTarget());

                                                if(cT.getAnswer()==-1) {
                                                    selectedItem = "Cancel";
                                                    selectedIndex = "-1";
                                                }

                                                else {
                                                    selectedItem = Parameter.Items[cT.getAnswer()];
                                                    selectedIndex = Integer.toString(cT.getAnswer());
                                                }

                                                if(targetItem.equals(selectedItem))
                                                    answer="correct";
                                                else
                                                    answer="wrong";

                                                usedFinger=Parameter.usedFinger;
                                                prepareTime = Long.toString(cT.getTime_wait());
                                                executeTime = Long.toString(cT.getTime_execute());

                                                if(testID%2==1){ //Only for Alive Icon
                                                    downX=Double.toString(cT.getDownX());
                                                    downY=Double.toString(cT.getDownX());
                                                    upX=Double.toString(cT.getUpX());
                                                    upY=Double.toString(cT.getUpY());

                                                    if(Parameter.DistanceInMilimeter) {
                                                        swipeDistance = convertPixelToMilimeters(cT.getSwipeDistance(), callingActivity);
                                                        travelDistance = convertPixelToMilimeters(travelDistances.get(n - 1), callingActivity);
                                                    }else{
                                                        swipeDistance = Double.toString(cT.getSwipeDistance());
                                                        travelDistance = Double.toString(travelDistances.get(n - 1));
                                                    }

                                                    //define vistedItems
                                                    VisitedItems="";
                                                    for(int i=0;i<cT.getLogMovement_VisitedItems().size();i++){
                                                        VisitedItems+=cT.getLogMovement_VisitedItems().get(i);
                                                    }
                                                    NrVisits=Integer.toString(cT.getLogMovement_VisitedItems().size());

                                                    if(cT.getSwipeDistance()<Parameter.popUp_threshold)
                                                        Mode="Blind Mode";
                                                    else
                                                        Mode="Visual Mode";

                                                    data.add(new String[]{key,userID,iconType,testType,trialID,blockID,repetition,targetItem,targetIndex,selectedItem,selectedIndex,answer,usedFinger,prepareTime,executeTime    ,downX,downY,upX,upY,swipeDistance,travelDistance,VisitedItems, NrVisits, Mode});
                                                }
                                                else
                                                    data.add(new String[]{key,userID,iconType,testType,trialID,blockID,repetition,targetItem,targetIndex,selectedItem,selectedIndex,answer,usedFinger,prepareTime,executeTime});
                                            }

            System.out.println("WRITE MAIN XML !!!");
            writer.writeAll(data); // data is adding to csv
            writer.close();

        } catch (IOException e) {
            System.out.println("IO EXCEPTION in WRITE MAIN XML !!!");
            e.printStackTrace();
        }
    }

    //This Method creates the second CSV File which contains the coordinates during the finger-movement. It returns an ArrayList
    //which contains the travelDistance of each trial. So this value also can be written into the main logfile
    private ArrayList<Double> createCSV_Coordinate(final AppCompatActivity callingActivity,String time) {
        ArrayList<Double> travelDistances=new ArrayList<>();
        String FILENAME_Coordinate="/COORDINATES_Test"+testID+"_ID"+Parameter.getUserID()+"_"+time+".csv";
        String PATH_Coordinate = (Environment.getExternalStorageDirectory().getAbsolutePath() +FILENAME_Coordinate);

        String[] heading={"Key","Travel Distance","Time Stamp", "X-Coordinate", "Y-Coordinate"};

        try {
            CSVWriter writer = new CSVWriter(new FileWriter(PATH_Coordinate),
                    Parameter.seperatorCSV,
                    CSVWriter.DEFAULT_QUOTE_CHARACTER,
                    CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                    CSVWriter.DEFAULT_LINE_END
            );

            List<String[]> data = new ArrayList<>();
            data.add(heading);


            int n=0;
            for(Trial cT: trials) {//cT...current Trial
                n++;
                int record_length = 2
                                    + cT.getLogMovement_Timestamp().size()
                                    + cT.getLogMovement_Coordinate_X().size()
                                    + cT.getLogMovement_Coordinate_Y().size();

                System.out.println("RECORD LENGTH: "+record_length+"!!!!!!!!!!!!!!!");

                String [] record = new String[record_length];
                record[0]=Integer.toString(n);

                double travelDistance = calculateTravelDistance(cT);
                record[1]=Double.toString(travelDistance);
                travelDistances.add(travelDistance);

                //loop all entries of logMovement arraylists and add them to record String
                int record_index=2;
                for(int i=0;i<cT.getLogMovement_Timestamp().size();i++){//i is index of LogMovement Array Lists
                    record[record_index]=Long.toString(cT.getLogMovement_Timestamp().get(i));
                    record[record_index+1]=Double.toString(cT.getLogMovement_Coordinate_X().get(i));
                    record[record_index+2]=Double.toString(cT.getLogMovement_Coordinate_X().get(i));
                    record_index+=3;
                }
                data.add(record);
            }

            System.out.println("WRITE COORDINATE XML !!!");
            writer.writeAll(data); // data is adding to csv
            writer.close();
        } catch (IOException e) {
            System.out.println("IO EXCEPTION in WRITE MAIN XML !!!");
            e.printStackTrace();
        }

        return travelDistances;
    }

    //helper method for logging which counts how many trial-objects with the same blockID and the same trialID exist
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

    //All distances are values in pixel. this method allows to log the distances in mm
    private String convertPixelToMilimeters(double pixelValue, final AppCompatActivity callingActivity){
        final DisplayMetrics dm = callingActivity.getResources().getDisplayMetrics();
        double mmValue= pixelValue / TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_MM, 1, dm);
        return Double.toString(mmValue);
    }

    //returns a String for the log-file depending on the testID
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

    //this method calculates the sum of all euclidian distances between two serial touch points
    private double calculateTravelDistance(Trial trial){
         double travelDistance=0;
         for(int i=1;i<trial.getLogMovement_Coordinate_X().size();i++){
             double diffX = abs(trial.getLogMovement_Coordinate_X().get(i)-trial.getLogMovement_Coordinate_X().get(i-1));
             double diffY = abs(trial.getLogMovement_Coordinate_Y().get(i)-trial.getLogMovement_Coordinate_Y().get(i-1));
             travelDistance+=Math.sqrt(diffX*diffX+diffY*diffY);
         }

         return travelDistance;
    }

}
