package com.example.iconfunctiontest.Services;

public class Trial {
    //Store: trialID, Target, Answer, ..
    private int trialID, blockID;
    private int target, answer; //Stored as Index of Parameter.Item Array
    private boolean doBreak; //is true if after this element a break should follow
    private long time_wait, time_move; //messured Time between pull-down and lift-off

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

    public long getTime_move() {
        return time_move;
    }

    public void setTime_move(long time_move) {
        this.time_move = time_move;
    }
}
