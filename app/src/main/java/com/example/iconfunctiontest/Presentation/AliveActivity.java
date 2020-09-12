package com.example.iconfunctiontest.Presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.GestureService;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class AliveActivity extends AppCompatActivity {

    private View mContentView;
    private static TextView tV_fullscreenContent;
    private static SoundPool soundPool;
    private static int sound_success, sound_error;

    private static final String TAG = "BlindMode";
    private TextView tV_Target;
    private TextView tV_PopUp;
    private TextView tV_Target_Heading;
    private Button bt_Icon;
    private Button bt_Continue;
    private TextView tV_Trial;

    private GestureService gs;
    private TestService testService;

    private Bundle bundle;

    private int testID;   //depending on this variable, the code executes different logic according to the tests
                        //0..Alive-Icon (no test), 1..Test1A, 2..Test1B, 3..Test2A, 4..Test2B, 5..Test3A, 6..Test3B

    private int[] itemMap;

    long timeStart, timePressDown;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alive_icon);

        /////// FULLSCREEN /////////////////
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        /////////////////////////////////////

        bt_Icon = findViewById(R.id.bt_Icon);
        bt_Continue = findViewById(R.id.bt_Continue);
        tV_Target_Heading = findViewById(R.id.tV_Target_Heading);
        tV_Target = findViewById(R.id.tV_Target);
        tV_PopUp = findViewById(R.id.tV_PopUp);
        tV_Trial = findViewById(R.id.tv_Trial);
        tV_fullscreenContent=findViewById(R.id.fullscreen_content);



        setGestureService();
        bt_Icon.setOnTouchListener(gs);
        testService = TestService.getInstance();


        //Adopt Text for each Test
        bundle = getIntent().getExtras();

        if(bundle!=null) {
            testID=bundle.getInt("testID");
            tV_Trial.setText("Trial "+bundle.getString("TRIAL"));
            if(bundle.getInt("TARGET")==-1) {
                tV_Target_Heading.setText("Alive Icon");
                bt_Icon.setVisibility(View.VISIBLE);
                bt_Continue.setVisibility(View.INVISIBLE);
                tV_Target.setVisibility(View.INVISIBLE);
            }
            else {
                tV_Target.setText(Parameter.Items[bundle.getInt("TARGET")]);
                bt_Icon.setVisibility(View.INVISIBLE);
                bt_Continue.setVisibility(View.VISIBLE);
            }
        }




        itemMap= testService.shuffleIntArray(Parameter.number_of_Items_Alive);

        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sound_success = soundPool.load(this, R.raw.success, 1);
        sound_error = soundPool.load(this, R.raw.error, 1);

    }

    public void onClick_Continue(View view) {
        bt_Continue.setVisibility(View.INVISIBLE);
        tV_Target.setVisibility(View.INVISIBLE);
        tV_Target_Heading.setVisibility(View.INVISIBLE);
        bt_Icon.setVisibility(View.VISIBLE);

        timeStart = System.currentTimeMillis();

    }

    //Needed for Fullscreen
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setGestureService(){
        gs= new GestureService(AliveActivity.this) {

            float dX, dY; //used for moving icon
            float originalX=0.0f;
            float originalY=0.0f;

            double downX = 0;
            double downY=0;
            boolean longClick=false;
            boolean dragMode=false;
            boolean touched=false;
            int selectedOption=-2;
            double selectedAngle =0.0;

            @SuppressLint({"SetTextI18n", "DefaultLocale", "ClickableViewAccessibility"})

            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {

                int action = motionEvent.getAction();
                double currentAlpha=0;

                switch(action) {
                    case (MotionEvent.ACTION_DOWN):
                        touched=true;

                        if(originalX==0.0f&&originalY==0.0f){ //set reference for moving back to origin position
                            originalX=view.getX();
                            originalY=view.getY();
                        }

                        dX = originalX - motionEvent.getRawX();//used for moving icon
                        dY = originalY - motionEvent.getRawY();//used for moving icon

                        downX=motionEvent.getX();
                        downY=motionEvent.getY();

                        //detect long click
                        longClick=true;
                        dragMode=false;
                        startCountDown(Parameter.VisualMode_LongClick_duration); //changes longClick to true

                        timePressDown = System.currentTimeMillis();
                        Log.d(TAG,"Action was DOWN");
                        return true;

                    case (MotionEvent.ACTION_MOVE):
                        longClick=false;


                        if(dragMode){
                            Log.d(TAG, "In Drag Mode!");
                            view.animate()  //used for moving icon
                                    .x(motionEvent.getRawX() + dX)
                                    .y(motionEvent.getRawY() + dY)
                                    .setDuration(0)
                                    .start();
                        }

                        else {
                            double diffX = motionEvent.getX() - downX;
                            double diffY = motionEvent.getY() - downY;

                            currentAlpha = calcAngle(diffX, diffY);

                            DecimalFormat df = new DecimalFormat("#");

                            if (abs(diffX) > Parameter.popUp_threshold || abs(diffY) > Parameter.popUp_threshold) {
                                tV_PopUp.setVisibility(View.VISIBLE);
                            }

                            if (  (abs(diffX) > Parameter.popUp_threshold  || abs(diffY) > Parameter.popUp_threshold)   ||   (Parameter.enableBlindMode&&testID!=1))
                            {
                                System.out.println("TestID="+testID);

                                if (abs(diffX) > Parameter.cancel_threshold || abs(diffY) > Parameter.cancel_threshold) {
                                    tV_PopUp.setText("Cancel");
                                    selectedOption = -1; //Cancel
                                } else {
                                    selectedAngle=currentAlpha; //TODO: Try to remove variable currentAlpha
                                    selectedOption=AngleToDirection(currentAlpha,Parameter.number_of_Items_Alive);


                                    if(testID==1||testID==2)
                                        selectedOption=itemMap[selectedOption];

                                    String item = Parameter.Items[selectedOption];

                                    tV_PopUp.setText(item + " (" + df.format(currentAlpha) + "°)");
                                }
                            }

                            tV_PopUp.animate()  //used for moving PopUp
                                    .x(motionEvent.getRawX() + dX - 40)
                                    .y(motionEvent.getRawY() + dY - 30)
                                    .setDuration(0)
                                    .start();
                        }

                        Log.d(TAG, "Action was MOVE");
                        return true;

                    case (MotionEvent.ACTION_UP) :
                        tV_PopUp.setVisibility(View.INVISIBLE);
                        longClick=false;
                        touched=false;

                        if(selectedOption!=-2) { //needed to disable blind-mode
                            switch (testID) {

                                case 0:
                                    //Alive Icon ohne Test - Zeigt toast mit selection
                                    String message;
                                    if (selectedOption == -1)
                                        message = "Cancel";
                                    else
                                        message = Parameter.Items[selectedOption];
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                    break;
                                case 1://Test1A novice users
                                case 3://Test2A expert users
                                    long time_wait = System.currentTimeMillis() - timeStart;
                                    long time_move =System.currentTimeMillis() - timePressDown;
                                    testService.onAnswer(selectedOption, AliveActivity.this, time_wait, time_move);
                                    break;
                            }
                        }

                        //Move Icon back to original position
                        new Thread() {
                            public void run() {

                                if (dragMode) {
                                    try {
                                        TimeUnit.SECONDS.sleep(Parameter.VisualMode_MoveIconBack_Delay);  //time delay
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if(!touched) {
                                    moveIconToOriginalPosition(view);
                                }
                            }
                        }.start();
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


            private void startCountDown(final int seconds){  //detect long click
                new Thread(){
                    public void run(){
                        for(int i= seconds;i>0;i--){
                            try {
                                TimeUnit.SECONDS.sleep(1);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        if(longClick){
                            Log.d(TAG, "LongClick Detected!!");
                            vibrate(Parameter.LongClick_Vibration_time);

                            dragMode=true;
                        }

                    }
                }.start();
            }

            private void moveIconToOriginalPosition(final View view){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.animate()  //used for moving icon
                                .x(originalX)
                                .y(originalY)
                                .setDuration(Parameter.MoveIconBack_Duration)
                                .start();
                    }
                });
            }

        };

    }


    public void onClickBt_Icon(View view){
        Log.d(TAG, "onClickBt_Icon clicked");
    }

    public void vibrate(int time){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(time);
    }

    public static void feedbackOnAnswer(boolean answer) {
        if(answer){
            soundPool.play(sound_success, 1, 1, 0, 0, 1);
            tV_fullscreenContent.setBackgroundResource(android.R.color.holo_green_light);
        }
        else {
            soundPool.play(sound_error, 1, 1, 0, 0, 1);
            tV_fullscreenContent.setBackgroundResource(android.R.color.holo_red_light);
        }   }




}