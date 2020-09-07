package com.example.iconfunctiontest.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

import static androidx.core.content.ContextCompat.getSystemService;

//public class GestureService implements View.OnTouchListener, View.OnLongClickListener {
public class GestureService implements View.OnTouchListener {

    private static final String TAG = "Gesture Service";
    private GestureDetector gestureDetector;
    private double touch_downX=0;
    private double touch_downY=0;

    private ArrayList<String> directions=new ArrayList<String>(); //NEW

    //Constructor
    public GestureService(Context c) {
        directions.addAll(Arrays.asList(Parameter.directions));
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
          return gestureDetector.onTouchEvent(motionEvent);
    }

    public double calcAngle(double diffX, double diffY){
        /*
        if(diffX==0&&diffY==0){
            return -2; //Click
        }
        */

        double alpha = Math.atan2(diffX,diffY);
        alpha=alpha*(180/Math.PI); //convert from "Bogenmaß" to Degree

        if(alpha>=0&&alpha<90){
            alpha+=270;
        }
        else if(alpha>=90&&alpha<180){
            alpha-=90;
        }
        else if(alpha<0&&alpha>-90){
            alpha+=270;
        }
        else if(alpha<=-90&&alpha>-180){
            alpha+=270;
        }
        else{
            alpha=-1; //Error
        }

        return alpha;
    }

    /*
    public Direction AngleToDirection(double alpha){
        if(isBetween(alpha,22.5,0)||isBetween(alpha, 360,337.5)){
            swipeE();
            return Direction.East;
        }
        else if(isBetween(alpha,67.5,22.5)){
            swipeNE();
            return Direction.NorthEast;
        }
        else if(isBetween(alpha,112.5,67.5 )){
            swipeN();
            return Direction.North;
        }
        else if(isBetween(alpha,157.5,112.5)){
            swipeNW();
            return Direction.NorthWest;
        }
        else if(isBetween(alpha,202.5 ,157.5)){
            swipeW();
            return Direction.West;
        }
        else if(isBetween(alpha,247.5,202.5)){
            swipeSW();
            return Direction.SouthWest;
        }
        else if(isBetween(alpha,292.5, 247.5)){
            swipeS();
            return Direction.South;
        }
        else if(isBetween(alpha,337.5, 292.5)){
            swipeSE();
            return Direction.SouthEast;
        }
        return Direction.Invalid;
    }
    */

    public String AngleToDirection(double alpha, int number_of_Items){

        double angle = 360.0 / number_of_Items; //angle per section
        ArrayList<Double> lower_limit=new ArrayList<Double>();
        ArrayList<Double> upper_limit=new ArrayList<Double>();

        //First segment is balanced to x-axis
        lower_limit.add(360-(angle/2));
        upper_limit.add(angle/2);

        for(int i=1; i<number_of_Items;i++){
            lower_limit.add(upper_limit.get(i-1));
            upper_limit.add(upper_limit.get(i-1)+angle);
        }

        //Actual angleToDirection
        if(isBetween(alpha,upper_limit.get(0),0)||isBetween(alpha, 360,lower_limit.get(0))){
           return directions.get(0);
        }
        for(int i=1;i<lower_limit.size();i++){//Loop all segments
            if(alpha>lower_limit.get(i)&&alpha<upper_limit.get(i))
                return directions.get(i);
        }

        return "Error!!";
    }


    //What should happen on each event //implementeds as Methodes to use overwrite
    public void swipeE(){
        Log.d(TAG, "\nSwipe East");
    }
    public void swipeNE(){
        Log.d(TAG, "\nSwipe North-East");
    }
    public void swipeN(){
        Log.d(TAG, "\nSwipe North");
    }
    public void swipeNW(){
        Log.d(TAG, "\nSwipe North-West");
    }
    public void swipeW(){
        Log.d(TAG, "\nSwipe West");
    }
    public void swipeSW(){
        Log.d(TAG, "\nSwipe South-West");
    }
    public void swipeS(){
        Log.d(TAG, "\nSwipe South");
    }
    public void swipeSE(){
        Log.d(TAG, "\nSwipe South-East");
    }



    private boolean isBetween(double value, double higherValue, double lowerValue){
        return value <= higherValue && value >= lowerValue;
    }

    public double getTouch_downX() {
        return touch_downX;
    }

    public void setTouch_downX(double touch_downX) {
        this.touch_downX = touch_downX;
    }

    public double getTouch_downY() {
        return touch_downY;
    }

    public void setTouch_downY(double touch_downY) {
        this.touch_downY = touch_downY;
    }





}