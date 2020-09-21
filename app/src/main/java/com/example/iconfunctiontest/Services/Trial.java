package com.example.iconfunctiontest.Services;

import android.util.DisplayMetrics;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Trial {
    private int trialID, blockID;
    private int target, answer; //Stored as Index of Parameter.Item Array
    private boolean doBreak; //is true if after this element a break should follow
    private long time_wait, time_execute; //messured Time between pull-down and lift-off
    private double downX, downY, upX, upY; //only needed for Alive-Icon
    //TODO: Add touch down and lift off coordinates for Alive Icon

    public Trial (int trialID, int blockID, int target){
        this.trialID=trialID;
        this.blockID=blockID;
        this.target=target;
        doBreak=false;
    }


    public static Trial clone(Trial refTrial){
        Trial newTrial=new Trial(refTrial.getTrialID(), refTrial.getBlockID(),refTrial.getTarget());
        newTrial.setAnswer(refTrial.getAnswer());
        newTrial.setDoBreak(refTrial.isDoBreak());

        return newTrial;
    }


    public int getTrialID() {
        return trialID;
    }

    public void setTrialID(int trialID) {
        this.trialID = trialID;
    }

    public int getBlockID() {
        return blockID;
    }

    public void setBlockID(int blockID) {
        this.blockID = blockID;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public int getAnswer() {
        return answer;
    }

    public boolean setAnswer(int answer) {
        this.answer = answer;
        return answer == target;
    }

    public boolean isDoBreak() {
        return doBreak;
    }
    public void setDoBreak(boolean doBreak) {
        this.doBreak = doBreak;
    }

    public long getTime_wait() {
        return time_wait;
    }
    public void setTime_wait(long time_wait) {
        this.time_wait = time_wait;
    }
    public long getTime_execute() {
        return time_execute;
    }
    public void setTime_execute(long time_execute) {
        this.time_execute = time_execute;
    }

    public double getDownX() {
        return downX;
    }
    public void setDownX(double downX) {
        this.downX = downX;
    }
    public double getDownY() {
        return downY;
    }
    public void setDownY(double downY) {
        this.downY = downY;
    }
    public double getUpX() {
        return upX;
    }
    public void setUpX(double upX) {
        this.upX = upX;
    }
    public double getUpY() {
        return upY;
    }
    public void setUpY(double upY) {
        this.upY = upY;
    }


    public double getSwipeDistance() {
        double diffX=abs(upX-downX);
        double diffY=abs(upY-downY);

        double swipeDistance=sqrt(diffX*diffX+diffY*diffY);//in pixels




        return sqrt(diffX*diffX+diffY*diffY);
    }

}
