package com.example.iconfunctiontest.Services;

import android.os.Bundle;
import android.util.Log;

public class TestService {

    public TestService(){
        //Empty Constructor
    }

    public String nextTrial(String currentTrial, boolean info_OR_test){
        //info_OR_test ... 0=Test, 1=Info

        if(currentTrial.equals("Welcome")){
            return "Test1";
        }

        //Last Test
        if(currentTrial.equals("Test8")){
            if(info_OR_test)
                return "Test8";
            return "End";
        }

        if(currentTrial.substring(0,4).equals("Test")) {
            if(info_OR_test)
                return currentTrial;

            int num = Integer.parseInt(currentTrial.substring(currentTrial.length() - 1));
            num++;
            return "Test" + num;
        }
        return "Error";
    }

    public String getTestHeading(String trial) {

        switch (trial) {
            case "Visual Mode": return "Visual Mode";
            case "Test1": return "Visual Selection #1";
            case "Test2": return "Blind Selection #1";
            case "Test3": return "Visual Selection #2";
            case "Test4": return "Blind Selection #2";
            case "Test5": return "Visual Selection #3";
            case "Test6": return "Blind Selection #3";
            case "Test7": return "Visual Selection #4";
            case "Test8": return "Blind Selection #4";

            default:
                return "Error";

        }
    }

    public String getInfoHeading(String trial){

        String heading ="Default Heading";

        assert trial != null;

        switch (trial) {
            case "Welcome":
                heading = "Welcome !";
                break;

            case "Test1":
                heading = "Visual Selection #1";
                break;

            case "Test2":
                heading = "Blind Selection #1";
                break;

            case "Test3":
                heading = "Visual Selection #2";
                break;

            case "Test4":
                heading = "Blind Selection #2";
                break;

            case "Test5":
                heading = "Visual Selection #3";
                break;

            case "Test6":
                heading = "Blind Selection #3";
                break;

            case "Test7":
                heading = "Visual Selection #4";
                break;

            case "Test8":
                heading = "Blind Selection #4";
                break;

            case "Ending":
                heading = "Thank You!";
                break;

            default:
                heading = "Error";
        }

        return heading;
    }

    public String getInfoExplanation(String trial){

        String explanation ="Default Explanation";

        assert trial != null;

        switch (trial) {
            case "Welcome":
                explanation = "In the following Tests, you will see a home-screen with only one Icon. " +
                        "The position of the Icon may change. " +
                        "Before each Test, you will see a description of your task. ";
                break;


            /////// Icon in Screens Center ///////////
            case "Test1":
                explanation = "Click on the Icon an keep your finger on the screen. " +
                        "Depending on the direction you move your finger, " +
                        "the Text-View shows you your current choice. " +
                        "Try to select “South-West”. ";
                break;

            case "Test2":
                explanation = "Try to call a shortcut-function " +
                        "by swiping north on the alive-icon as exactly as possible. ";
                break;

            ////// Cancel Selection ///////////
            case "Test3":
                explanation = "Click on the Icon an keep your finger on the screen. " +
                        "Depending on the direction you move your finger, " +
                        "the Text-View shows you your current choice. " +
                        "Try to cancel your selection by swiping a longer distance to any direction. ";
                break;

            case "Test4":
                explanation = "Start swiping to any direction. Instead of releasing your finger, " +
                        "keep swiping till your selection gets canceled.";
                break;

            /////// Icon in corner ///////////
            case "Test5":
                explanation = "Click on the Icon an keep your finger on the screen. " +
                        "Depending on the direction you move your finger, " +
                        "the Text-View shows you your current choice. " +
                        "Try to select “North-West”. ";
                break;

            case "Test6":
                explanation = "Try to call a shortcut-function " +
                        "by swiping South-East on the alive-icon as exactly as possible. ";
                break;

            ////// Displacing the Alive-Icon///////
            case "Test7":
                explanation = "It is pretty hard to use the alive icon when it is located close to the edge, isn’t it? " +
                        "In the following test, you are asked to do a LongClick on the icon. " +
                        "As soon as you feel a vibration, you can move the icon to any position " +
                        "on the screen where it is easier to use. " +
                        "Then release the icon and press it again to use the visual selection. " +
                        "Find the Option “West” and select it. ";
                break;


            case "Test8":
                explanation = "In the following test, you are asked to do a LongClick on the icon." +
                        " As soon as you feel a vibration, you can move the icon to any position " +
                        "on the screen where it is easier to use. " +
                        "Then release the icon and swipe East straight.";
                break;

            default:
                explanation = "Error";
        }


        return explanation;
    }

    public boolean onSelection(Direction direction, String trial){
        System.out.println("onSelection executed!");

        System.out.println("trial: "+trial);
        System.out.println("direction: "+direction);

        switch(trial){
            case "Test1":
                return direction == Direction.SouthWest;

            //TODO: Add other Test-Cases

            default:
                return false;
        }

    }

}
