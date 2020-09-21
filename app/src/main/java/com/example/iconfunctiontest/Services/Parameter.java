package com.example.iconfunctiontest.Services;

public class Parameter {
    //General
    private static String name="default";

    private static int DEFAULT=2;
    public static int LongClick_Vibration_time = 200; //in miliseconds

    public static String[] Items = {
            "Item A",
            "Item B",
            "Item C",
            "Item D",   //Nr 4
            "Item E",
            "Item F",   //Nr 6
            "Item G",
            "Item H",   //Nr 8
            "Item I",
            "Item J",
            "Item K",
            "Item L",   //Nr 12
            "Item M",
            "Item N",
            "Item O",
            "Item P",   //Nr 16
            "Item Q",
            "Item R",
            "Item S",
            "Item T",
            "Item U",
            "Item V",
            "Item W",
            "Item X",
            "Item Y",
            "Item Z"    //Nr26
    };

//Alive Icon
    public static int number_of_Items_Alive = DEFAULT; //Default=4

    public static boolean enableBlindMode =true; //Default=true, disable it only for testing novice users
    public static int VisualMode_LongClick_duration = 3; //in seconds

    // Delay time between releasing the finger from screen till icon starts moving back to original position
    public static int VisualMode_MoveIconBack_Delay = 1; //in seconds

    public static int MoveIconBack_Duration =1000; //in miliseconds

    public static double popUp_threshold = 100;
    public static double cancel_threshold = 200; //swipe more than this value to cancel selection

//Standard Icon
    public static int number_of_Items_Standard = DEFAULT; //Default=4



// Test 2 Expert Users - Alive Icon vs Standard Icon
    //2A...Subtest using alive-icon
    //2B...Subtest using standard-icon
    public static int number_of_trials_2=number_of_Items_Alive; //Default: same number as Icons
    public static int number_of_blocks_2=2;
    public static int nextActivity_Delay = 1000; //Default=2000 because this is the time, the Toast is on the screen
                                                  //in miliseconds

    public static int blocksBetweenBreak =1;

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Parameter.name = name;
    }
}
