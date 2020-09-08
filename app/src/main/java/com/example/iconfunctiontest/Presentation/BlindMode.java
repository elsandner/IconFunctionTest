package com.example.iconfunctiontest.Presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.GestureService;
import com.example.iconfunctiontest.Services.Parameter;

import java.text.DecimalFormat;

import static java.lang.Math.abs;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class BlindMode extends AppCompatActivity {

////////////////////////////////////////////////////////////////////////////////////////////////////

    //THIS CLASS IS NOT NEEDED ANYMORE AND NOT USED ANYMORE
    //TODO: REMOVE THIS CLASS

////////////////////////////////////////////////////////////////////////////////////////////////////

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
////////////////////////////////////////////////////////////////////////////////////////////////////

    private static final String TAG = "BlindMode";
    private TextView tv_Description;
    private Button bt_Icon;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_blind_mode);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);


        bt_Icon = findViewById(R.id.bt_Icon);
        tv_Description = findViewById(R.id.tv_Target);

        bt_Icon.setOnTouchListener(new GestureService(BlindMode.this){

            double downX = 0, downY=0;

            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {

                int action = motionEvent.getAction();

                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                        downX=motionEvent.getX();
                        downY=motionEvent.getY();
                        Log.d(TAG,"Action was DOWN");
                        return true;

                    case (MotionEvent.ACTION_MOVE) :
                        //Implement Canceling a Selection with a Zig-Zag Movement

                        Log.d(TAG,"Action was MOVE");
                        return true;

                    case (MotionEvent.ACTION_UP) :

                        double diffX=motionEvent.getX()-downX;
                        double diffY=motionEvent.getY()-downY;

                        double currentAlpha = calcAngle(diffX, diffY);

                        DecimalFormat df = new DecimalFormat("#.##");

                        //LOG diffX, diffY --> adjust threshold
                        Log.d(TAG, "diffX: "+diffX+" diffY: "+diffY);

                        if(abs(diffX)> Parameter.cancel_threshold||abs(diffY)>Parameter.cancel_threshold){
                            Toast.makeText(getApplicationContext(), "Selection Canceled", Toast.LENGTH_SHORT).show();
                        }
                        else if(currentAlpha==-2){
                            tv_Description.setText("Click");
                        }
                        else{
                        //    tv_Description.setText(AngleToDirection(currentAlpha, Parameter.number_of_Items_Alive).toString() +"\n"+ df.format(currentAlpha));
                        }

                        Log.d(TAG,"Action was UP");
                        return true;

                    case (MotionEvent.ACTION_CANCEL) :
                        Log.d(TAG,"Action was CANCEL");
                        return true;

                    case (MotionEvent.ACTION_OUTSIDE) :
                        Log.d(TAG,"Movement occurred outside bounds " +
                               "of current screen element");
                        return true;

                    default :
                        return super.onTouch( view, motionEvent);
               }
           }

        }

        );

    }


//////////////////////////////////////////////////////////////////////////////////////////////
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

    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
///////////////////////////////////////////////////////////////////////////////////////////7

    public void onClickBt_Icon(View view){
        tv_Description.setText("Click");
        Log.d(TAG, "Bt_Icon clicked");
    }

}

