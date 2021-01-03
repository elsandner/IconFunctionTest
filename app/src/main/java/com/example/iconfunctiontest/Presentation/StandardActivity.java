package com.example.iconfunctiontest.Presentation;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class StandardActivity extends AppCompatActivity {
    private View mContentView;

    private static TextView tV_fullscreenContent;
    private static SoundPool soundPool;
    private static int sound_success, sound_error;

    private TextView tV_Target_Heading, tV_Target, tV_Trial, tV_Selection;
    private ConstraintLayout cL_Icons;
    private Button bt_Icon0, bt_Icon1, bt_Icon2, bt_Icon3, bt_Icon4, bt_Continue ;
    private LinearLayout L_PopUp0, L_PopUp1, L_PopUp2,L_PopUp3,L_PopUp4;
    private TextView tV_label0, tV_label1, tV_label2, tV_label3, tV_label4;

    private TestService testService;
    private long timeStart, timePressDown;
    private Bundle bundle;//used to receive parameters from other activity
    private int testID;   //depending on this variable, the code executes different logic according to the tests
    //0..Alive-Icon (no test), 1..Test1A, 2..Test1B, 3..Test2A, 4..Test2B, 5..Test3A, 6..Test3B
    private boolean inTest=false;   //this variable is false when instruction screen (with continue button) or feedback screen (red/green) is shown. If the icon itself is shown this variable is true.
    private boolean popUpOpen=false; //when one of the popUp menues is shown, this variable is true

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standard_icon);

        activateFullscreen();
        initializeUI_Elements();
        setTextAndVisibility();
        createSoundPool();

        testService = TestService.getInstance();

        //Dynamically Add Items to Pop-Up Menu
        ArrayList<TextView> buffer=new ArrayList<TextView>();
        TextView[] textViewBuffer= new TextView[Parameter.number_of_Items_Standard];

        int number_of_Items=Parameter.number_of_Items_Standard;

        if(testID==6)//in this test four icons are on the screen
            number_of_Items=4*number_of_Items;



        for (int i = 0; i < number_of_Items; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2, 5, 2, 5);

            TextView currentTV = (TextView) getLayoutInflater().inflate(R.layout.text_view_item, null);
            currentTV.setLayoutParams(params);
            currentTV.setText(Parameter.Items[i]);

            final int selectedOption = i;
            currentTV.setOnClickListener(new TextView.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickItem(selectedOption, view);
                }
            });
            textViewBuffer[i]=currentTV;
        }

        if(testID==2){
            int[] posMapping = testService.shufflePosition(Parameter.number_of_Items_Standard);

            TextView[] memoryArray = new TextView[Parameter.number_of_Items_Standard];

            for(int i =0; i<textViewBuffer.length;i++){
                L_PopUp0.addView(textViewBuffer[posMapping[i]]);
            }
        }
        else if(testID==4){
            for(int i =0; i<textViewBuffer.length;i++){
                L_PopUp0.addView(textViewBuffer[i]);
            }
        }
        else if(testID==6){
            for(int i=0; i<Parameter.number_of_Items_Standard;i++){
                L_PopUp1.addView(buffer.get(i));
                L_PopUp2.addView(buffer.get(i+Parameter.number_of_Items_Standard));
                L_PopUp3.addView(buffer.get(i+Parameter.number_of_Items_Standard*2));
                L_PopUp4.addView(buffer.get(i+Parameter.number_of_Items_Standard*3));
            }
        }

    }

    //SoundPool is needed to play positive and negative sound as feedback
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

    //This Method initializes all needed UI Elements and also contains the clickListeners and longClickListeners for all Icons
    private void initializeUI_Elements(){
        tV_Trial = findViewById(R.id.tV_Trial);
        tV_Target_Heading = findViewById(R.id.tV_Target_Heading);
        tV_Target = findViewById(R.id.tV_Target);
        tV_Selection =findViewById(R.id.tV_Selection);

        bt_Continue=findViewById(R.id.bt_Continue);

        bt_Icon0 = findViewById(R.id.bt_Icon0);
        tV_label0 = findViewById(R.id.tV_label0);
        L_PopUp0 = findViewById(R.id.L_PopUp0);

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

        tV_fullscreenContent=findViewById(R.id.fullscreen_content);

        View.OnClickListener clickListener =new View.OnClickListener(){
            @Override
            public void onClick(View view){
                executeOnClick(view);
            }
        };

        bt_Icon0.setOnClickListener(clickListener);
        bt_Icon1.setOnClickListener(clickListener);
        bt_Icon2.setOnClickListener(clickListener);
        bt_Icon3.setOnClickListener(clickListener);
        bt_Icon4.setOnClickListener(clickListener);

        bt_Icon0.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return executeOnLongClick(0, view);
                    }
                }
        );

        bt_Icon1.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return executeOnLongClick(1, view);
                    }
                }
        );

        bt_Icon2.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return executeOnLongClick(2, view);
                    }
                }
        );

        bt_Icon3.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return executeOnLongClick(3, view);
                    }
                }
        );

        bt_Icon4.setOnLongClickListener(
                new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        return executeOnLongClick(4, view);
                    }
                }
        );

    }

    //Clicking an Icon when PopUp is shown corresponds to canceling the selection
    private void executeOnClick(View view){
        if(popUpOpen) {
            popUpOpen=false;
            clickItem(-1, view);

            if(testID==0) {
                L_PopUp0.setVisibility(View.INVISIBLE);
                tV_Selection.setVisibility(View.VISIBLE);
                tV_Selection.setText("Selection Canceled");
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
        }
    }

    //Long Click on Icon opens the popUp Menu
    private boolean executeOnLongClick(int iconNumber, View view){

        vibrate(Parameter.LongClick_Vibration_time);

        L_PopUp1.setVisibility(View.INVISIBLE);
        L_PopUp2.setVisibility(View.INVISIBLE);
        L_PopUp3.setVisibility(View.INVISIBLE);
        L_PopUp4.setVisibility(View.INVISIBLE);

        switch(iconNumber){
            case 0:
                L_PopUp0.setVisibility(View.VISIBLE);
                break;
            case 1:
                L_PopUp1.setVisibility(View.VISIBLE);
                break;
            case 2:
                L_PopUp2.setVisibility(View.VISIBLE);
                break;
            case 3:
                L_PopUp3.setVisibility(View.VISIBLE);
                break;
            case 4:
                L_PopUp4.setVisibility(View.VISIBLE);
                break;
        }

        timePressDown = System.currentTimeMillis();
        popUpOpen=true;
        return true;

    }

    //Default visibility when acitivity is opened
    private void setTextAndVisibility(){
        bundle = getIntent().getExtras();

        if(bundle!=null) {
            testID=bundle.getInt("testID");
            tV_Trial.setText("Trial "+bundle.getString("TRIAL"));

            if(testID==0) { //instruction Mode
                tV_Target_Heading.setText("Standard Icon");
                tV_Target.setVisibility(View.INVISIBLE);
                bt_Continue.setVisibility(View.INVISIBLE);
                cL_Icons.setVisibility(View.INVISIBLE); //All Icons
                bt_Icon0.setVisibility(View.VISIBLE);
            }

            else { //Test Mode
                tV_Target.setText(Parameter.Items[bundle.getInt("TARGET")]);
                tV_Target.setVisibility(View.VISIBLE);
                bt_Continue.setVisibility(View.VISIBLE);

                cL_Icons.setVisibility(View.INVISIBLE); //All Icons
                bt_Icon0.setVisibility(View.INVISIBLE);
                tV_label0.setVisibility(View.INVISIBLE);
            }
        }
    }

    //This method changes the visibility of UI Elements during the test
    public void onClick_Continue(View view) {

        inTest=true;
        bt_Continue.setVisibility(View.INVISIBLE);

        if(Parameter.hide_Target_In_Test){
            tV_Target_Heading.setVisibility(View.INVISIBLE);
            tV_Target.setVisibility(View.INVISIBLE);
        }

        if (testID != 6) {  //Test1B, Test2B - using 1 Icon
            bt_Icon0.setVisibility(View.VISIBLE);
            tV_label0.setVisibility(View.VISIBLE);
        }

        else{ //Test3B - using 4 Icons
            cL_Icons.setVisibility(View.VISIBLE);
        }

        timeStart = System.currentTimeMillis();
    }

    //This method is executed when element in pop up menu is clicked
    private void clickItem(int selectedOption, View view){

        inTest=false;
        popUpOpen=false;
        long time_wait = timePressDown - timeStart -500; //the 500ms which the longClick takes are part of time_move
        long time_move = System.currentTimeMillis() - timePressDown + 500;

        tV_Target.setVisibility(View.INVISIBLE);
        tV_Selection.setVisibility(View.VISIBLE);

        if(selectedOption==-1)
            tV_Selection.setText("Selection Canceled");
        else
            tV_Selection.setText(Parameter.Items[selectedOption]+" selected");

        switch(testID){
            case 0: //Standard Icon (No Test)
                L_PopUp0.setVisibility(View.INVISIBLE);

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

                break;
            case 2: //Test1B
            case 4: //Test2B
                tV_Target_Heading.setVisibility(View.INVISIBLE);
                L_PopUp0.setVisibility(View.INVISIBLE);
                bt_Icon0.setVisibility(View.INVISIBLE);
                tV_label0.setVisibility(View.INVISIBLE);
                testService.onAnswer(selectedOption, StandardActivity.this,
                        time_wait, time_move,
                        Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,
                        null,null,null,null);
                break;
            case 6://Test3B
                tV_Target_Heading.setVisibility(View.INVISIBLE);
                cL_Icons.setVisibility(View.INVISIBLE);
                testService.onAnswer(selectedOption, StandardActivity.this,
                        time_wait, time_move,
                        Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,Double.MAX_VALUE,
                        null,null,null, null);
                break;
        }

    }

    public void onClickScreen(View view) {
        if(inTest||testID==0)
            executeOnClick(view);
    }

    //This method is called after firing an selection (or cancel the selection) - it gives audio and visual feedback
    public static void feedbackOnAnswer(boolean answer){

        if(answer){
            soundPool.play(sound_success, 1, 1, 0, 0, 1);
            tV_fullscreenContent.setBackgroundResource(android.R.color.holo_green_light);

        }
        else {
            soundPool.play(sound_error, 1, 1, 0, 0, 1);
            tV_fullscreenContent.setBackgroundResource(android.R.color.holo_red_light);
        }
    }

    //Used as feedback when doing a longClick
    private void vibrate(int miliseconds){
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(miliseconds);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

}