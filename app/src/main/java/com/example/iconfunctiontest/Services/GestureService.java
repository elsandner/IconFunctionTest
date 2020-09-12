package com.example.iconfunctiontest.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;

//public class GestureService implements View.OnTouchListener, View.OnLongClickListener {
public class GestureService implements View.OnTouchListener {

    private static final String TAG = "Gesture Service";
    private GestureDetector gestureDetector;
    private double touch_downX=0;
    private double touch_downY=0;

    private ArrayList<String> directions=new ArrayList<String>(); //NEW

    //Constructor
    public GestureService(Context c) {
        directions.addAll(Arrays.asList(Parameter.Items));
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
            if(diffX==0.0){
                if(diffY==0)
                    return -2;//Click
                else if(diffY<0)
                    return 90;
                else
                    return 270;
            }
            else {
                System.out.println("calcAngle results in -1!!!!!!!\tdiffX=" + diffX + "\tdiffY=" + diffY);
                alpha = -1; //Error
            }
        }

        return alpha;
    }



    public String AngleToDirectionSTRING(double alpha, int number_of_Items){

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


    public int AngleToDirection(double alpha, int number_of_Items, int iconID){

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



            return 0;
        }
        for(int i=1;i<lower_limit.size();i++){//Loop all segments
            if(alpha>lower_limit.get(i)&&alpha<upper_limit.get(i))
                return i;
        }

        return -1;
    }



    private boolean isBetween(double value, double higherValue, double lowerValue){
        return value <= higherValue && value >= lowerValue;
    }


}