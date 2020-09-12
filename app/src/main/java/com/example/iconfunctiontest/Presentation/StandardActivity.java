package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

import java.util.ArrayList;
import java.util.Collections;


public class StandardActivity extends AppCompatActivity {

    private View mContentView;


    private static TextView tV_fullscreenContent;
    private static SoundPool soundPool;
    private static int sound_success, sound_error;

    private TextView tV_Target_Heading, tV_Target, tV_Trial;

    private ConstraintLayout cL_Icons;
    private Button bt_Icon0, bt_Icon1, bt_Icon2, bt_Icon3, bt_Icon4, bt_Continue ;
    private LinearLayout L_PopUp0, L_PopUp1, L_PopUp2,L_PopUp3,L_PopUp4;
    private TextView tV_label0, tV_label1, tV_label2, tV_label3, tV_label4;

    private TestService testService;
    private long timeStart, timePressDown;
    private Bundle bundle;
    private int testID;   //depending on this variable, the code executes different logic according to the tests
    //0..Alive-Icon (no test), 1..Test1A, 2..Test1B, 3..Test2A, 4..Test2B, 5..Test3A, 6..Test3B


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_icon);

        activateFullscreen();
        initializeUI_Elements();
        setTextAndVisibility();

        testService = TestService.getInstance();

        //Dynamically Add Items to Pop-Up Menu
        ArrayList<TextView> buffer=new ArrayList<TextView>();

        int number_of_Items=Parameter.number_of_Items_Standard;
        if(testID==6)
            number_of_Items=4*number_of_Items;

        for (int i = 0; i < number_of_Items; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2, 5, 2, 5);

            TextView currentTV = (TextView) getLayoutInflater().inflate(R.layout.text_view_item, null);
            currentTV.setLayoutParams(params);
            currentTV.setText(Parameter.Items[i]);

            final int finalIndex = i;
            currentTV.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItem(finalIndex, view);
                }
            });
            buffer.add(currentTV);
        }

        if(testID!=6) { //Test1B, Test2B
            if (testID == 2)
                Collections.shuffle(buffer);

            for(TextView TV : buffer){
                L_PopUp0.addView(TV);
            }
        }
        else{   //Test3B
            for(int i=0; i<Parameter.number_of_Items_Standard;i++){
                L_PopUp1.addView(buffer.get(i));
                L_PopUp2.addView(buffer.get(i+Parameter.number_of_Items_Standard));
                L_PopUp3.addView(buffer.get(i+Parameter.number_of_Items_Standard*2));
                L_PopUp4.addView(buffer.get(i+Parameter.number_of_Items_Standard*3));
            }
        }


        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        sound_success = soundPool.load(this, R.raw.success, 1);
        sound_error = soundPool.load(this, R.raw.error, 1);
    }

    private void initializeUI_Elements(){
        tV_Trial = findViewById(R.id.tV_Trial);
        tV_Target_Heading = findViewById(R.id.tV_Target_Heading);
        tV_Target = findViewById(R.id.tV_Target);

        bt_Continue=findViewById(R.id.bt_Continue);
        cL_Icons = findViewById(R.id.cL_Icons);

        bt_Icon1=findViewById(R.id.bt_Icon1);
        bt_Icon2=findViewById(R.id.bt_Icon2);
        bt_Icon3=findViewById(R.id.bt_Icon3);
        bt_Icon4=findViewById(R.id.bt_Icon4);

        tV_label1=findViewById(R.id.tV_label1);
        tV_label2=findViewById(R.id.tV_label2);
        tV_label3=findViewById(R.id.tV_label3);
        tV_label4=findViewById(R.id.tV_label4);

        L_PopUp1 = findViewById(R.id.L_PopUp1);
        L_PopUp2 = findViewById(R.id.L_PopUp2);
        L_PopUp3 = findViewById(R.id.L_PopUp3);
        L_PopUp4 = findViewById(R.id.L_PopUp4);

        bt_Icon0 = findViewById(R.id.bt_Icon0);
        L_PopUp0 = findViewById(R.id.L_PopUp0);
        tV_label0 = findViewById(R.id.tV_label0);

        tV_fullscreenContent=findViewById(R.id.fullscreen_content);


        bt_Icon0.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        vibrate(Parameter.LongClick_Vibration_time);

                        L_PopUp0.setVisibility(View.VISIBLE);

                        timePressDown = System.currentTimeMillis();
                        return false;
                    }
                }
        );

        bt_Icon1.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        vibrate(Parameter.LongClick_Vibration_time);

                        L_PopUp1.setVisibility(View.VISIBLE);

                        timePressDown = System.currentTimeMillis();
                        return false;
                    }
                }
        );

        bt_Icon2.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        vibrate(Parameter.LongClick_Vibration_time);

                        L_PopUp2.setVisibility(View.VISIBLE);

                        timePressDown = System.currentTimeMillis();
                        return false;
                    }
                }
        );

        bt_Icon3.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        vibrate(Parameter.LongClick_Vibration_time);

                        L_PopUp3.setVisibility(View.VISIBLE);

                        timePressDown = System.currentTimeMillis();
                        return false;
                    }
                }
        );

        bt_Icon4.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        vibrate(Parameter.LongClick_Vibration_time);

                        L_PopUp4.setVisibility(View.VISIBLE);

                        timePressDown = System.currentTimeMillis();
                        return false;
                    }
                }
        );
    }

    public void onClick_Continue(View view) {

        tV_Target_Heading.setVisibility(View.INVISIBLE);
        tV_Target.setVisibility(View.INVISIBLE);
        bt_Continue.setVisibility(View.INVISIBLE);

        if (testID != 6) {  //Test1B, Test2B - using 1 Icon
            bt_Icon0.setVisibility(View.VISIBLE);
            tV_label0.setVisibility(View.VISIBLE);
        }

        else{ //Test3B - using 4 Icons
            cL_Icons.setVisibility(View.VISIBLE);
        }

        timeStart = System.currentTimeMillis();//TODO: correct times

    }

    //This method is executed when element in pop up menu is clicked
    private void clickItem(int selectedOption, View view){
        Toast.makeText(getApplicationContext(), Parameter.Items[selectedOption]+" selected", Toast.LENGTH_SHORT).show();
        L_PopUp0.setVisibility(View.INVISIBLE);



        long time_wait = System.currentTimeMillis() - timeStart -500; //the 500ms which the longClick takes are part of time_move
        long time_move = System.currentTimeMillis() - timeStart + 500;
        switch(testID){
            case 0: //Standard Icon (No Test)
                break;
            case 2: //Test1B
            case 4: //Test2B
                testService.onAnswer(selectedOption, StandardActivity.this, time_wait, time_move); //TODO: add second time stemp
                break;
            case 6://Test3B
                break;
        }


    }




    public void onClickBt_Icon(View view) {
        System.out.println("Wrong Icon clicked");
    }

    public void onClickBt_TestIcon(View view) {
        System.out.println("Test Icon clicked");
    }

    public void onClickScreen(View view) {
        L_PopUp0.setVisibility(View.INVISIBLE);
        L_PopUp1.setVisibility(View.INVISIBLE);
        L_PopUp2.setVisibility(View.INVISIBLE);
        L_PopUp3.setVisibility(View.INVISIBLE);
        L_PopUp4.setVisibility(View.INVISIBLE);
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

    private void setTextAndVisibility(){
        bundle = getIntent().getExtras();

        if(bundle!=null) {
            testID=bundle.getInt("testID");
            tV_Trial.setText("Trial "+bundle.getString("TRIAL"));

            if(bundle.getInt("testID")==0) { //instruction Mode
                tV_Target_Heading.setText("Standard Icon");
                tV_Target.setVisibility(View.INVISIBLE);
                bt_Continue.setVisibility(View.INVISIBLE);
                cL_Icons.setVisibility(View.INVISIBLE); //All Icons
                bt_Icon0.setVisibility(View.VISIBLE);

            }

            else { //Test cases
                tV_Target.setText(Parameter.Items[bundle.getInt("TARGET")]);
                tV_Target.setVisibility(View.VISIBLE);
                bt_Continue.setVisibility(View.VISIBLE);

                cL_Icons.setVisibility(View.INVISIBLE); //All Icons
                bt_Icon0.setVisibility(View.INVISIBLE);
                tV_label0.setVisibility(View.INVISIBLE);
            }

        }
    }

    public static void changeBackgroundColor(boolean answer){
        if(answer){
            soundPool.play(sound_success, 1, 1, 0, 0, 1);
            tV_fullscreenContent.setBackgroundResource(android.R.color.holo_green_light);
        }
        else {
            soundPool.play(sound_error, 1, 1, 0, 0, 1);
            tV_fullscreenContent.setBackgroundResource(android.R.color.holo_red_light);
        }
    }

    private void vibrate(int miliseconds){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(miliseconds);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        soundPool.release();
        soundPool=null;
    }




}