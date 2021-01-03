package com.example.iconfunctiontest.Services;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.List;

public class Parameter {
    //General
    private static int DEFAULT=4;//Value used for number_of_Items_Alive, number_of_Items_Standard, number_of_trials,

    public static boolean hide_Target_In_Test=false; //in a test-trial first the designated target and the continue button are shown.
                                                    // when pressing continue the UI changes and depending on this value the participant
                                                    //needs to remember the target (true) or the textView with the target stays on the screen.

    public static boolean show_radialSegments=true; //in instruction Mode of Alive Icon
    public static int LongClick_Vibration_time = 5; //in miliseconds    //to switch vibration of -> set to 0

    public static boolean shuffleItemsBeforeTest=true;  //defines if Items will be shuffled before each test or if they will be in the original order

    public static String[] Items = { //This array needs to include at least (4 * number_of_items) elements!v
            "Plus",
            "Minus",
            "Multiplication",
            "Division",   //Nr 4
            "Copy last result",
            "Diagram",
            "Advanced",
            "Calculator Settings",

            "Video",
            "Selfie",
            "Panorama",
            "Gallery",
            "SLO-MO",
            "Timer",
            "Picture",
            "Camera Settings",

            "My classes",
            "My exams",
            "My lessons",
            "Confirmations",
            "Registration",
            "My Account",
            "Exam Book",
            "AAU Settings",

            "Today",
            "Add appointment",
            "Add task",
            "Add reminder",
            "This week",
            "This month",
            "next appointment",
            "CalenderSettings"
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
    public static int number_of_blocks =4;
    public static int nextActivity_Delay = 1000; //Default=2000 because this is the time, the Toast is on the screen
                                                  //in miliseconds

    public static int blocksBetweenBreak =2;

    //Logging
    public static char seperatorCSV =',';
    public static String usedFinger="Thump";
    public static Boolean DistanceInMilimeter=true;    //Depending on this value the swipe-distance and the travel-distance in main-logfile are written in mm or in pixel

    private static String userID ="default"; //this value is changed programmatically
    public static String getUserID() {
        return userID;
    }
    public static void setUserID(String userID) {
        Parameter.userID = userID;
    }


    public static void shuffleItems (boolean iconTypeAlive){

        if(shuffleItemsBeforeTest) {
            int numItems = number_of_Items_Standard;
            if (iconTypeAlive)
                numItems = number_of_Items_Alive;

            List<String> itemList1 = Arrays.asList(Arrays.copyOfRange(Items, 0, numItems));
            List<String> itemList2 = Arrays.asList(Arrays.copyOfRange(Items, numItems, 2 * numItems));
            List<String> itemList3 = Arrays.asList(Arrays.copyOfRange(Items, (2 * numItems), 3 * numItems));
            List<String> itemList4 = Arrays.asList(Arrays.copyOfRange(Items, (3 * numItems), 4 * numItems));

            Collections.shuffle(itemList1);
            Collections.shuffle(itemList2);
            Collections.shuffle(itemList3);
            Collections.shuffle(itemList4);

            List<String> itemList = new ArrayList<String>();
            itemList.addAll(itemList1);
            itemList.addAll(itemList2);
            itemList.addAll(itemList3);
            itemList.addAll(itemList4);

            Items = itemList.toArray(new String[itemList.size()]);
        }
    }

}
