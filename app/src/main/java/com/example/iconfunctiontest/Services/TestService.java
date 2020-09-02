package com.example.iconfunctiontest.Services;

import android.os.Bundle;

public class TestService {

    public TestService(){
        //Empty Constructor
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

            case "Test3":
                explanation = "Click on the Icon an keep your finger on the screen. " +
                        "Depending on the direction you move your finger, " +
                        "the Text-View shows you your current choice. " +
                        "Try to select “North-West”. ";
                break;

            case "Test4":
                explanation = "Try to call a shortcut-function " +
                        "by swiping South-East on the alive-icon as exactly as possible. ";
                break;

            case "Test5":
                explanation = "It is pretty hard to use the alive icon when it is located close to the edge, isn’t it? " +
                        "In the following test, you are asked to do a LongClick on the icon. " +
                        "As soon as you feel a vibration, you can move the icon to any position " +
                        "on the screen where it is easier to use. " +
                        "Then release the icon and press it again to use the visual selection. " +
                        "Find the Option “West” and select it. ";
                break;

            case "Test6":
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




}
