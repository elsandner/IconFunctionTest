package com.example.iconfunctiontest.Services;

public class Trial {
    //Store: trialID, Target, Answer, ..
    int trialID, blockID;
    int target, answer; //Stored as Index of Parameter.Item Array
    boolean doBreak; //is true if after this element a break should follow

    public Trial (int trialID, int blockID, int target){
        this.trialID=trialID;
        this.blockID=blockID;
        this.target=target;
        doBreak=false;
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
}
