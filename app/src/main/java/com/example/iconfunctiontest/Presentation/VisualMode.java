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

import java.text.DecimalFormat;

import static java.lang.Math.abs;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class VisualMode extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 1;

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

    private static final String TAG = "BlindMode";
    private TextView tv_Description;
    private Button bt_Icon;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_visual_mode);

        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        bt_Icon = findViewById(R.id.bt_Icon);
        tv_Description = findViewById(R.id.tv_Direction);



        bt_Icon.setOnTouchListener(new GestureService(VisualMode.this) {

            double downX = 0;
            double downY=0;
            String oldValue="";

            @SuppressLint({"SetTextI18n", "DefaultLocale"})

            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {

                int action = motionEvent.getAction();

                switch(action) {
                    case (MotionEvent.ACTION_DOWN) :
                        tv_Description.setBackgroundResource(R.color.design_default_color_on_primary);
                        oldValue= (String) tv_Description.getText();
                        downX=motionEvent.getX();
                        downY=motionEvent.getY();
                        Log.d(TAG,"Action was DOWN");
                        return true;

                    case (MotionEvent.ACTION_MOVE) :
                        double diffX=motionEvent.getX()-downX;
                        double diffY=motionEvent.getY()-downY;

                        double currentAlpha = calcAngle(diffX, diffY);

                        DecimalFormat df = new DecimalFormat("#.##");
                      //  System.out.println("X(0)="+downX+"\tY(0)="+downY+"\t\t\tDiffX:"+diffX+" DiffY:"+diffY+"\t\t\tAlpha:"+currentAlpha);

                        if(abs(diffX)>cancel_threshold||abs(diffY)>cancel_threshold){
                            Toast toast = Toast.makeText(getApplicationContext(), "Selection Canceled", Toast.LENGTH_SHORT);
                            toast.show();
                            tv_Description.setText(oldValue);
                        }
                        else if(currentAlpha==-2){
                            tv_Description.setText("Click");
                        }
                        else{
                            tv_Description.setText(AngleToDirection(currentAlpha).toString() +"\n"+ df.format(currentAlpha));
                        }

                        Log.d(TAG,"Action was MOVE");
                        return true;

                    case (MotionEvent.ACTION_UP) :
                        tv_Description.setBackgroundResource(R.color.design_default_color_primary_variant);
                        Log.d(TAG,"Action was UP");
                        //Log.d(TAG,"diffX="+(motionEvent.getX()-downX)+"diffY="+(motionEvent.getY()-downY));
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
        });

    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {        //Part of Fullscreen
        super.onPostCreate(savedInstanceState);

        // Trigger the initial hide() shortly after the activity has been
        // created, to briefly hint to the user that UI controls
        // are available.
        delayedHide(100);
    }

    private void hide() {   //Part of Fullscreen
        // Hide UI first
        System.out.println("HIDE IS EXECUTED");
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mControlsView.setVisibility(View.GONE);

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    private void delayedHide(int delayMillis) {         //Part of Fullscreen
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////

    public void onClickBt_Icon(View view){
        tv_Description.setText("Click");
        Log.d(TAG, "onClickBt_Icon clicked");
    }
}