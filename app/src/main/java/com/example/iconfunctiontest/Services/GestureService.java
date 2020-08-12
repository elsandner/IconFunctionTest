package com.example.iconfunctiontest.Services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class GestureService implements View.OnTouchListener {

    private static final String TAG = "Gesture Service";
    private GestureDetector gestureDetector;

    //Constructor
    public GestureService(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
    }

    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return gestureDetector.onTouchEvent(motionEvent);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private static final int SWIPE_THRESHOLD = 50;     //"Distanz-Grenzwert
        private static final int SWIPE_VELOCITY_THRESHOLD = 100; //Geschwindigkeits-Grenzwert

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return super.onSingleTapUp(e);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        @Override
        public boolean onFling(MotionEvent mE_touch_down, MotionEvent mE_lift_off, float velocityX, float velocityY) {
            
            try {
                float diffY = mE_lift_off.getY() - mE_touch_down.getY();
                float diffX = mE_lift_off.getX() - mE_touch_down.getX();

                double alpha = Math.atan2(diffY,diffX);
                alpha=alpha*180/Math.PI; //convert from "Bogenmaß" to Degree
                System.out.println("Alpha: "+alpha);

                if(isBetween(alpha,22.5,-22.5)){
                    swipeE();
                }
                else if(isBetween(alpha,-22.5,-67.5)){
                    swipeNE();
                }
                else if(isBetween(alpha,-67.5,-112.5)){
                    swipeN();
                }
                else if(isBetween(alpha,-112.5,-157.5)){
                    swipeNW();
                }
                else if(isBetween(alpha,-157.5,157.5)){
                    swipeW();
                }
                else if(isBetween(alpha,157.5,112.5)){
                    swipeSW();
                }
                else if(isBetween(alpha,112.5,67.5)){
                    swipeS();
                }
                else if(isBetween(alpha,67.5,22.5)){
                    swipeSE();
                }


            /* Old way to calculate direction of swipe
                if (Math.abs(diffX) > Math.abs(diffY)) {    //abs returns the absolute value
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                }
                else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }

             */

                Log.d(TAG, "diffY: "+diffY);
                Log.d(TAG, "diffX: "+diffX);
                Log.d(TAG, "velocityY: "+velocityY);
                Log.d(TAG, "velocityX: "+velocityX);
                System.out.println();

            }
            catch (Exception exception) {
                exception.printStackTrace();
            }


            return false;
        }
    }



    //What should happen on each event
    public void swipeE(){
        Log.d(TAG, "\nSwipe East");
    }

    public void swipeNE(){
        Log.d(TAG, "\nSwipe North-East");
    }

    public void swipeN(){
        Log.d(TAG, "\nSwipe North");
    }

    public void swipeNW(){
        Log.d(TAG, "\nSwipe North-West");
    }

    public void swipeW(){
        Log.d(TAG, "\nSwipe West");
    }

    public void swipeSW(){
        Log.d(TAG, "\nSwipe South-West");
    }

    public void swipeS(){
        Log.d(TAG, "\nSwipe South");
    }

    public void swipeSE(){
        Log.d(TAG, "\nSwipe South-East");
    }


    /*

    public void onSwipeRight() {
        Log.d(TAG, "\nRight");
    }
    public void onSwipeLeft() {
        Log.d(TAG, "\nLeft");

    }

    public void onSwipeUp() {
        Log.d(TAG, "\nUp");

    }

    public void onSwipeDown() {
        Log.d(TAG, "\nDown");

    }


     */

    private void onClick() {
        Log.d(TAG, "\nClick");
    }

    private void onDoubleClick() {
        Log.d(TAG, "\nDoubleClick");
    }

    private void onLongClick() {
        Log.d(TAG, "\nLongClick");
    }


    private boolean isBetween(double value, double higherValue, double lowerValue){
        return value < higherValue && value >= lowerValue;
    }
}