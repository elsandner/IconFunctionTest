package com.example.iconfunctiontest.Presentation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.GestureService;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.abs;

public class AliveActivity extends AppCompatActivity {
    private View mContentView;

    private static TextView tV_fullscreenContent;
    private static SoundPool soundPool;
    private static int sound_success, sound_error;

    private TextView tV_Target, tV_Target_Heading, tV_Trial, tV_Selection;
    private TextView tV_PopUp;
    private Button bt_Icon0, bt_Icon1, bt_Icon2, bt_Icon3, bt_Icon4, bt_Continue ;
    private TextView tV_label0, tV_label1, tV_label2, tV_label3, tV_label4;

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

        activateFullscreen();
        initializeUI_Elements();
        setTextAndVisibility();
        createSoundPool();

        bt_Icon0.setOnTouchListener(getGestureService(0));
        bt_Icon1.setOnTouchListener(getGestureService(1));
        bt_Icon2.setOnTouchListener(getGestureService(2));
        bt_Icon3.setOnTouchListener(getGestureService(3));
        bt_Icon4.setOnTouchListener(getGestureService(4));

        testService = TestService.getInstance();

        itemMap= testService.shuffleIntArray(Parameter.number_of_Items_Alive);
    }

/////OnCreate sub-methodes///////
    private void createSoundPool(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder().build();

            soundPool = new SoundPool
                    .Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        sound_success = soundPool.load(this, R.raw.success, 1);
        sound_error = soundPool.load(this, R.raw.error, 1);
    }

    private void activateFullscreen(){
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    private void initializeUI_Elements(){
        tV_Trial = findViewById(R.id.tv_Trial);
        tV_Target_Heading = findViewById(R.id.tV_Target_Heading);
        tV_Target = findViewById(R.id.tV_Target);
        tV_Selection = findViewById(R.id.tV_Selection);

        bt_Continue = findViewById(R.id.bt_Continue);

        bt_Icon0 = findViewById(R.id.bt_Icon0);
        tV_label0=findViewById(R.id.tV_label0);

        tV_PopUp = findViewById(R.id.tV_PopUp);

        bt_Icon1=findViewById(R.id.bt_Icon1);
        bt_Icon2=findViewById(R.id.bt_Icon2);
        bt_Icon3=findViewById(R.id.bt_Icon3);
        bt_Icon4=findViewById(R.id.bt_Icon4);

        tV_label1=findViewById(R.id.tV_label1);
        tV_label2=findViewById(R.id.tV_label2);
        tV_label3=findViewById(R.id.tV_label3);
        tV_label4=findViewById(R.id.tV_label4);

        tV_fullscreenContent=findViewById(R.id.fullscreen_content);
    }

    private void setTextAndVisibility(){
        bt_Icon1.setVisibility(View.INVISIBLE);
        bt_Icon2.setVisibility(View.INVISIBLE);
        bt_Icon3.setVisibility(View.INVISIBLE);
        bt_Icon4.setVisibility(View.INVISIBLE);

        tV_label1.setVisibility(View.INVISIBLE);
        tV_label2.setVisibility(View.INVISIBLE);
        tV_label3.setVisibility(View.INVISIBLE);
        tV_label4.setVisibility(View.INVISIBLE);

        bundle = getIntent().getExtras();

        if(bundle!=null) {
            testID=bundle.getInt("testID");
            tV_Trial.setText("Trial "+bundle.getString("TRIAL"));

            if(testID==0) {//instruction Mode
                tV_Target_Heading.setText("Alive Icon");
                tV_Target.setVisibility(View.INVISIBLE);
                bt_Continue.setVisibility(View.INVISIBLE);

                bt_Icon0.setVisibility(View.VISIBLE);
                tV_label0.setVisibility(View.VISIBLE);

                if(Parameter.show_radialSegments)
                    createRadialSegments(Parameter.number_of_Items_Alive);
            }

            else {//Test Mode
                tV_Target.setText(Parameter.Items[bundle.getInt("TARGET")]);
                bt_Continue.setVisibility(View.VISIBLE);

                bt_Icon0.setVisibility(View.INVISIBLE);
                tV_label0.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void createRadialSegments(int segments) {
        FrameLayout fL_radial_segment_container = findViewById(R.id.fL_radia_segment_container);
        int color[] = {
                Color.GREEN,
                Color.BLUE,
                Color.CYAN,
                Color.DKGRAY,
                Color.MAGENTA,
                Color.YELLOW,
                Color.LTGRAY,
                Color.argb(1,223, 219, 93),
                Color.argb(1,242, 110, 187),
                Color.argb(1,223, 93, 93),
                Color.argb(1,220, 242, 110),
                Color.argb(1,110, 185, 242),
                Color.argb(1,203, 110, 242),
                Color.argb(1,76, 154, 80),
                Color.argb(1,76, 133, 154 ),
                Color.argb(1,76, 77, 154),
                Color.argb(1,130, 76, 154 ),
                Color.argb(1,154, 76, 76 ),
                Color.argb(1,179, 195, 255 ),
        };//TODO: Extend this list to test with more Items

        int angle = (int) (360.0 / segments); //angle per segment = half of the real segment-angle

        LayoutInflater inflater = getLayoutInflater();

        ProgressBar radialSegment_Last = (ProgressBar) inflater.inflate(R.layout.radial_segment, null);
        radialSegment_Last.setProgress(360);
        radialSegment_Last.getProgressDrawable().setColorFilter(color[0], PorterDuff.Mode.SRC_ATOP);
        fL_radial_segment_container.addView(radialSegment_Last);

        for(int i=segments-1;i>=1;i--){
                ProgressBar radialSegment = (ProgressBar) inflater.inflate(R.layout.radial_segment, null);
                radialSegment.setProgress((angle * i)+(angle/2));
                radialSegment.getProgressDrawable().setColorFilter(color[i], PorterDuff.Mode.SRC_ATOP);
                fL_radial_segment_container.addView(radialSegment);
        }

        ProgressBar radialSegment_First = (ProgressBar) inflater.inflate(R.layout.radial_segment, null);
        radialSegment_First.setProgress(angle/2);
        radialSegment_First.getProgressDrawable().setColorFilter(color[0], PorterDuff.Mode.SRC_ATOP);
        fL_radial_segment_container.addView(radialSegment_First);

    }
////////////////////////////////

    public void onClick_Continue(View view) {

        bt_Continue.setVisibility(View.INVISIBLE);

        if(Parameter.hide_Target_In_Test){
            tV_Target_Heading.setVisibility(View.INVISIBLE);
            tV_Target.setVisibility(View.INVISIBLE);
        }

        if (testID != 5) {  //Test1A, Test2A - using 1 Icon
            bt_Icon0.setVisibility(View.VISIBLE);
            tV_label0.setVisibility(View.VISIBLE);
        }

        else{ //Test3B - using 4 Icons
            bt_Icon1.setVisibility(View.VISIBLE);
            bt_Icon2.setVisibility(View.VISIBLE);
            bt_Icon3.setVisibility(View.VISIBLE);
            bt_Icon4.setVisibility(View.VISIBLE);

            tV_label1.setVisibility(View.VISIBLE);
            tV_label2.setVisibility(View.VISIBLE);
            tV_label3.setVisibility(View.VISIBLE);
            tV_label4.setVisibility(View.VISIBLE);
        }

        timeStart = System.currentTimeMillis();
    }

    private GestureService getGestureService(final int iconID){
        GestureService gestureService = new GestureService(AliveActivity.this) {

            float dX, dY; //used for moving icon
            float originalX=0.0f;
            float originalY=0.0f;

            double downX =0;
            double downY=0;
            boolean longClick=false;
            boolean dragMode=false;
            boolean touched=false;
            int selectedOption=-2;

            ArrayList<Long> logMovement_Timestamp = new ArrayList<Long>();
            ArrayList<Float> logMovement_Coordinate_X= new ArrayList<>();
            ArrayList<Float> logMovement_Coordinate_Y = new ArrayList<>();

            @SuppressLint({"SetTextI18n", "DefaultLocale", "ClickableViewAccessibility"})
            @Override
            public boolean onTouch(final View view, final MotionEvent motionEvent) {

                    int action = motionEvent.getAction();
                    double currentAlpha = 0;

                    System.out.println("Timestamp bevore move:"+logMovement_Timestamp.toString());

                    switch (action) {
                        case (MotionEvent.ACTION_DOWN):


                            touched = true;

                            if (originalX == 0.0f && originalY == 0.0f) { //set reference for moving back to origin position
                                originalX = view.getX();
                                originalY = view.getY();
                            }

                            dX = originalX - motionEvent.getRawX();//used for moving icon
                            dY = originalY - motionEvent.getRawY();//used for moving icon

                            downX = motionEvent.getX();
                            downY = motionEvent.getY();

                            //detect long click
                            longClick = true;
                            dragMode = false;
                            startCountDown(Parameter.AliveIcon_LongClick_duration); //changes longClick to true

                            timePressDown = System.currentTimeMillis();
                            //Log.d("GESTURE","Action was DOWN");
                            return true;

                        case (MotionEvent.ACTION_MOVE):
                            longClick = false;

                            if (dragMode) {
                                //Log.d("GESTURE", "In Drag Mode!");
                                view.animate()  //used for moving icon
                                        .x(motionEvent.getRawX() + dX)
                                        .y(motionEvent.getRawY() + dY)
                                        .setDuration(0)
                                        .start();
                            }
                            else {

                                //Log every touchpoint
                                //if(testID>0) {//Do not log in instruction-Mode
                                    logMovement_Timestamp.add(System.currentTimeMillis());
                                    logMovement_Coordinate_X.add(motionEvent.getX());
                                    logMovement_Coordinate_Y.add(motionEvent.getY());

                                System.out.println("Timestamps:"+logMovement_Timestamp.toString());
                                System.out.println("X: "+logMovement_Coordinate_X.toString());
                                System.out.println("Y: "+logMovement_Coordinate_Y.toString());
                                //}

                                double diffX = motionEvent.getX() - downX;
                                double diffY = motionEvent.getY() - downY;

                                currentAlpha = calcAngle(diffX, diffY);

                                //Show hint
                                if (abs(diffX) > Parameter.popUp_threshold || abs(diffY) > Parameter.popUp_threshold) {
                                    tV_PopUp.setVisibility(View.VISIBLE);
                                }

                                //only react if either blind mode is enabled or popUp threshold is exceeded
                                if ((abs(diffX) > Parameter.popUp_threshold || abs(diffY) > Parameter.popUp_threshold) || (Parameter.enableBlindMode && testID != 1)) //testID!=1 because in this test, the Blind-Mode always needs to be disabled
                                {
                                    if (abs(diffX) > Parameter.cancel_threshold || abs(diffY) > Parameter.cancel_threshold) {
                                        tV_PopUp.setText("Cancel");
                                        selectedOption = -1; //Cancel
                                    } else {
                                        selectedOption = AngleToDirection(currentAlpha, Parameter.number_of_Items_Alive, iconID);

                                        if (testID == 1)
                                            selectedOption = itemMap[selectedOption];

                                        tV_PopUp.setText(Parameter.Items[selectedOption]);
                                    }
                                }

                                tV_PopUp.animate()  //used for moving PopUp
                                        .x(motionEvent.getRawX() + dX - 40)
                                        .y(motionEvent.getRawY() + dY - 30)
                                        .setDuration(0)
                                        .start();
                            }
                            return true;

                        case (MotionEvent.ACTION_UP):
                            tV_PopUp.setVisibility(View.INVISIBLE);
                            longClick = false;
                            touched = false;

                            if (selectedOption != -2) { //needed to disable blind-mode

                                String selection;
                                if (selectedOption == -1)
                                    selection = "Cancel";
                                else
                                    selection = Parameter.Items[selectedOption];
                                tV_Selection.setVisibility(View.VISIBLE);
                                tV_Selection.setText(selection+" selected");

                                switch (testID) {
                                    case 0:
                                        actionUpInstructionMode();
                                        break;
                                    case 1://Test1A novice users
                                    case 3://Test2A expert users
                                    case 5://Test3A learning
                                     //   System.out.println("Timestamps:"+logMovement_Timestamp.toString());
                                     //   System.out.println("X: "+logMovement_Coordinate_X.toString());
                                     //   System.out.println("Y: "+logMovement_Coordinate_Y.toString());

                                        actionUpTestMode(motionEvent,selectedOption,downX,downY,logMovement_Timestamp,logMovement_Coordinate_X,logMovement_Coordinate_Y);
                                        break;
                                }
                            }

                            //Move Icon back to original position
                            new Thread() {
                                public void run() {

                                    if (dragMode) {
                                        try {
                                            TimeUnit.SECONDS.sleep(Parameter.AliveIcon_MoveIconBack_Delay);  //time delay
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    if (!touched) {
                                        moveIconToOriginalPosition(view);
                                    }
                                }
                            }.start();
                            //Log.d("GESTURE","Action was UP");
                            return true;


                        default:
                            return true;
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
                            //Log.d("GESTURE", "LongClick Detected");
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
        return gestureService;
    }

    private void actionUpTestMode(MotionEvent motionEvent, int selectedOption, double downX, double downY, ArrayList<Long> logMovement_Timestamp, ArrayList<Float>logMovement_Coordinate_X, ArrayList<Float> logMovement_Coordinate_Y){
        long time_wait = timePressDown - timeStart;
        long time_move = System.currentTimeMillis() - timePressDown;

        double upX = motionEvent.getX();
        double upY = motionEvent.getY();

        tV_Target.setVisibility(View.INVISIBLE);
        tV_Target_Heading.setVisibility(View.INVISIBLE);

        bt_Icon0.setVisibility(View.INVISIBLE);
        tV_label0.setVisibility(View.INVISIBLE);

        bt_Icon1.setVisibility(View.INVISIBLE);
        bt_Icon2.setVisibility(View.INVISIBLE);
        bt_Icon3.setVisibility(View.INVISIBLE);
        bt_Icon4.setVisibility(View.INVISIBLE);

        tV_label1.setVisibility(View.INVISIBLE);
        tV_label2.setVisibility(View.INVISIBLE);
        tV_label3.setVisibility(View.INVISIBLE);
        tV_label4.setVisibility(View.INVISIBLE);

        testService.onAnswer(selectedOption, AliveActivity.this,
                            time_wait, time_move,
                            downX, downY, upX, upY,
                            logMovement_Timestamp,logMovement_Coordinate_X,logMovement_Coordinate_Y);
    }

    private void actionUpInstructionMode(){
        Thread thread = new Thread(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                try {
                    Thread.sleep(Parameter.nextActivity_Delay);
                    tV_Selection.setVisibility(View.INVISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }


}