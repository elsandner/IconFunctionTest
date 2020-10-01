package com.example.iconfunctiontest.Services;

public class Parameter {
    //General
    private static String name="default";

    private static int DEFAULT=4;

    public static boolean hide_Target_In_Test=false;
    public static boolean show_radialSegments=true;
    public static int LongClick_Vibration_time = 200; //in miliseconds

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
    public static int number_of_Items_Alive = 4; //Default=4

    public static boolean enableBlindMode =true; //Default=true, disable it only for testing novice users

    public static int AliveIcon_LongClick_duration = Integer.MAX_VALUE; //in seconds //deaktivate: Integer.MAX_VALUE;
    // Delay time between releasing the finger from screen till icon starts moving back to original position
    public static int AliveIcon_MoveIconBack_Delay = 1; //in seconds
    public static int MoveIconBack_Duration =1000; //in miliseconds

    public static double popUp_threshold = 100;
    public static double cancel_threshold = 200; //swipe more than this value to cancel selection

//Standard Icon
    public static int number_of_Items_Standard = DEFAULT; //Default=4

// Tests Expert Users - Alive Icon vs Standard Icon
    //2A...Subtest using alive-icon
    //2B...Subtest using standard-icon
    public static int number_of_trials_2=DEFAULT; //Default: same number as Icons
    public static int number_of_blocks_2=6;
    public static int nextActivity_Delay = 1000; //Default=2000 because this is the time, the Toast is on the screen
                                                  //in miliseconds

    public static int blocksBetweenBreak =2;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Parameter.name = name;
    }
}
