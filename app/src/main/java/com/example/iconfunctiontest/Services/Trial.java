package com.example.iconfunctiontest.Services;

public class Trial {
    //Store: trialID, Target, Answer, ..
    int trialID, blockID;
    int target, answer; //Stored as Index of Parameter.Item Array
    int counter; //increases if participant needs to repeat this trial

    public Trial (int trialID, int blockID, int target){
        this.trialID=trialID;
        this.blockID=blockID;
        this.target=target;
        this.counter=0;
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

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }
}
