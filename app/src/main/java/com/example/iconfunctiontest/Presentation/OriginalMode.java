package com.example.iconfunctiontest.Presentation;

import android.annotation.SuppressLint;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class OriginalMode extends AppCompatActivity {

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

    Button bt_TestIcon, bt_Icon1, bt_Icon2, bt_Icon3 ;
    LinearLayout L_PopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_original_mode);

        mVisible = true;
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mContentView = findViewById(R.id.fullscreen_content);

        bt_TestIcon = findViewById(R.id.bt_TestIcon);
        bt_Icon1=findViewById(R.id.bt_Icon1);
        bt_Icon2=findViewById(R.id.bt_Icon2);
        bt_Icon3=findViewById(R.id.bt_Icon3);
        L_PopUp = findViewById(R.id.L_PopUp);

        bt_TestIcon.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        longClickTestIcon();
                        return false;
                    }
                }
        );

    }

    ///////////////////////////////////////////////////////////////////////////////////
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

    ///////////////////////////////////////////////////////////////////////////////////


    private void longClickTestIcon(){
        vibrate(Parameter.LongClick_Vibration_time);

        L_PopUp.setVisibility(View.VISIBLE);
        bt_Icon1.setVisibility(View.INVISIBLE);
        bt_Icon2.setVisibility(View.INVISIBLE);
        bt_Icon3.setVisibility(View.INVISIBLE);

        Toast.makeText(getApplicationContext(), "Long Click", Toast.LENGTH_SHORT).show();
    }


    private void vibrate(int miliseconds){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(miliseconds);
    }

    public void onClickBt_Icon(View view) {

        System.out.println("Wrong Icon clicked");
    }

    public void onClickBt_TestIcon(View view) {
        System.out.println("Test Icon clicked");

    }

    public void onClickScreen(View view) {
        System.out.println("onClickScreen executed");
        //Make L_PopUp invisible and icons visible

        L_PopUp.setVisibility(View.INVISIBLE);
        bt_Icon1.setVisibility(View.VISIBLE);
        bt_Icon2.setVisibility(View.VISIBLE);
        bt_Icon3.setVisibility(View.VISIBLE);


    }

    public void onClickOption1(View view) {
        Toast.makeText(getApplicationContext(), "option 1 selected", Toast.LENGTH_SHORT).show();
        onClickScreen(view);


    }

    public void onClickOption2(View view) {
        Toast.makeText(getApplicationContext(), "option 2 selected", Toast.LENGTH_SHORT).show();
        onClickScreen(view);
    }

    public void onClickOption3(View view) {
        Toast.makeText(getApplicationContext(), "option 3 selected", Toast.LENGTH_SHORT).show();
        onClickScreen(view);
    }

    public void onClickOption4(View view) {
        Toast.makeText(getApplicationContext(), "option 4 selected", Toast.LENGTH_SHORT).show();
        onClickScreen(view);
    }
}