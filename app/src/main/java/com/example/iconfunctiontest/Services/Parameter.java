package com.example.iconfunctiontest.Services;

public class Parameter {


    public static int LongClick_Vibration_time = 200; //in miliseconds

//Blind Mode
        //Nothing

//Visual Mode
    public static boolean enableBlindMode =true; //Default=true, disable it only for testing novice users
    public static int VisualMode_LongClick_duration = 3; //in seconds

    // Delay time between releasing the finger from screen till icon starts moving back to original position
    public static int VisualMode_MoveIconBack_Delay = 1; //in seconds

    public static int MoveIconBack_Duration =1000; //in miliseconds

    public static double popUp_threshold = 100;
    public static double cancel_threshold = 200; //swipe more than this value to cancel selection


}
