package com.example.iconfunctiontest.Presentation;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.GestureService;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class firstTest extends AppCompatActivity {


    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1; //Original 3000

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {
            // Delayed removal of status and navigation bar

            // Note that some of these constants are new as of API 16 (Jelly Bean)
            // and API 19 (KitKat). It is safe to use them, as they are inlined
            // at compile-time and do nothing on earlier devices.
            mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private View mControlsView;
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
            mControlsView.setVisibility(View.VISIBLE);
        }
    };
    private boolean mVisible;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    /**
     * Touch listener to use for in-layout UI controls to delay hiding the
     * system UI. This is to prevent the jarring behavior of controls going away
     * while interacting with activity UI.
     */
    private static final String TAG = "firstTest";
    private TextView tv_Description;
    private Button bt_Icon;

    private final View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {

            switch (motionEvent.getAction()) {

                case MotionEvent.ACTION_DOWN:
                    if (AUTO_HIDE) {
                        delayedHide(AUTO_HIDE_DELAY_MILLIS);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    view.performClick();
                    break;
                default:
                    break;
            }

            return false;
        }
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_first_test);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);


        //My Code:
        bt_Icon = findViewById(R.id.bt_Icon);
        tv_Description = findViewById(R.id.tv_Direction);

        bt_Icon.setOnTouchListener(new GestureService(firstTest.this) {

            @Override
            public void swipeE(){
                super.swipeE();
                tv_Description.setText("East");
            }
            @Override
            public void swipeNE(){
                super.swipeNE();
                tv_Description.setText("North-East");
            }
            @Override
            public void swipeN(){
                super.swipeN();
                tv_Description.setText("North");
            }
            @Override
            public void swipeNW(){
                super.swipeNW();
                tv_Description.setText("North-West");
            }
            @Override
            public void swipeW(){
                super.swipeW();
                tv_Description.setText("West");
            }
            @Override
            public void swipeSW(){
                super.swipeSW();
                tv_Description.setText("South-West");
            }
            @Override
            public void swipeS(){
                super.swipeS();
                tv_Description.setText("South");
            }
            @Override
            public void swipeSE() {
                super.swipeSE();
                tv_Description.setText("South-West");
            }
            /*
            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                tv_Description.setText("Left");
                //Toast.makeText(firstTest.this, "Swipe Left gesture detected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                tv_Description.setText("Right");
                //Toast.makeText(firstTest.this, "Swipe Right gesture detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeUp() {
                super.onSwipeUp();
                tv_Description.setText("Up");
                //Toast.makeText(firstTest.this, "Swipe Up gesture detected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                tv_Description.setText("Down");
                //Toast.makeText(firstTest.this, "Swipe Down gesture detected", Toast.LENGTH_SHORT).show();
            }

             */
        });






    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {
        // Hide UI first
        System.out.println("HIDE IS EXECUTED");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in delay milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }


    //------------------My Methods-----------------------//


    public void onClickBt_Icon(View view){
        tv_Description.setText("Click");
        Log.d(TAG, "onClickBt_Icon clicked");
    }

}

