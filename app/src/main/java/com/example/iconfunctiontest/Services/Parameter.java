package com.example.iconfunctiontest.Services;

public class Parameter {
    //General

    private static int DEFAULT=2;//Value used for number_of_Items_Alive,number_of_Items_Standard, number_of_trials,

    public static boolean hide_Target_In_Test=false; //in a test-trial first the designated target and the continue button are shown.
                                                    // when pressing continue the UI changes and depending on this value the participant
                                                    //needs to remember the target (true) or the textView with the target stays on the screen.

    public static boolean show_radialSegments=true; //in instruction Mode of Alive Icon
    public static int LongClick_Vibration_time = 5; //in miliseconds    //to switch vibration of -> set to 0

    public static String[] Items = {
            "Red A",
            "Red B",
            "Red C",
            "Red D",   //Nr 4
            "Blue A",
            "Blue B",
            "Blue C",
            "Blue D",
            "Yellow A",
            "Yellow B",
            "Yellow C",
            "Yellow D",
            "Green A",
            "Green B",
            "Green C",
            "Green D",
            "Item M",
            "Item N",
            "Item O",
            "Item P",
            "Item Q",
            "Item R",
            "Item S",
            "Item T",
            "Item U",
            "Item V",
            "Item W",
            "Item X",
            "Item Y",
            "Item Z"
    };

//Alive Icon
    public static int number_of_Items_Alive = DEFAULT; //Default=4

    public static boolean enableBlindMode =true; //Default=true, disable it only for testing novice users
    public static int AliveIcon_LongClick_duration = Integer.MAX_VALUE; //in seconds //deaktivate: Integer.MAX_VALUE;
    // Delay time between releasing the finger from screen till icon starts moving back to original position
    public static int AliveIcon_MoveIconBack_Delay = 1; //in seconds
    public static int MoveIconBack_Duration =1000; //in miliseconds

    public static double popUp_threshold = 100;
    public static double cancel_threshold = 200; //swipe more than this value to cancel selection

    //distance between finger and pop up menu (to avoid that the users finger hides the hint)
    public static int popUp_distance_toTouchPoint_X=40;
    public static int popUp_distance_toTouchPoint_Y=30;


//Standard Icon
    public static int number_of_Items_Standard = DEFAULT;

// Tests Expert Users - Alive Icon vs Standard Icon
    //2A...Subtest using alive-icon
    //2B...Subtest using standard-icon
    public static int number_of_trials =DEFAULT; //Default: same number as Icons
    public static int number_of_blocks =2;
    public static int nextActivity_Delay = 1000; //Default=2000 because this is the time, the Toast is on the screen
                                                  //in miliseconds

    public static int blocksBetweenBreak =2;

    //Logging
    public static char seperatorCSV =',';
    public static String usedFinger="thump";
    public static Boolean DistanceInMilimeter=false;    //Depending on this value the swipe-distance and the travel-distance in main-logfile are written in mm or in pixel

    private static String userID ="default"; //this value is changed programmatically
    public static String getUserID() {
        return userID;
    }
    public static void setUserID(String userID) {
        Parameter.userID = userID;
    }
}
