package com.example.iconfunctiontest.Presentation;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.example.iconfunctiontest.Services.TestService;

import java.util.ArrayList;
import java.util.Collections;


public class StandardActivity extends AppCompatActivity {

    private View mContentView;

    Button bt_TestIcon, bt_Icon1, bt_Icon2, bt_Icon3 ;
    LinearLayout L_PopUp;
    private TextView tV_Target, tV_Trial;

    private TestService testService;
    private long timeStart;
    private Bundle bundle;
    private int testID;   //depending on this variable, the code executes different logic according to the tests
                        //0..Alive-Icon (no test), 1..Test1A, 2..Test1B, 3..Test2A, 4..Test2B, 5..Test3A, 6..Test3B


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_original_mode);

        /////// FULLSCREEN /////////////////
        mContentView = findViewById(R.id.fullscreen_content);
        mContentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        /////////////////////////////////////


        bt_TestIcon = findViewById(R.id.bt_TestIcon);
        bt_Icon1=findViewById(R.id.bt_Icon1);
        bt_Icon2=findViewById(R.id.bt_Icon2);
        bt_Icon3=findViewById(R.id.bt_Icon3);
        L_PopUp = findViewById(R.id.L_PopUp);
        tV_Trial = findViewById(R.id.tV_Trial);
        tV_Target = findViewById(R.id.tV_Target);

        testService = TestService.getInstance();

        //Adopt Text for each Test
        bundle = getIntent().getExtras();

        if(bundle!=null) {
            testID=bundle.getInt("testID");
            tV_Trial.setText("Trial "+bundle.getString("TRIAL"));
            if(bundle.getInt("TARGET")==-1)
                tV_Target.setText("Standard Icon");
            else
                tV_Target.setText("Target: "+Parameter.Items[bundle.getInt("TARGET")]);
        }



        //Dynamically Add Items to Pop-Up Menu
        ArrayList<TextView> buffer=new ArrayList<TextView>();

        for(int i=0;i<Parameter.number_of_Items_Standard;i++){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2,5,2,5);

            TextView currentTV = (TextView)getLayoutInflater().inflate(R.layout.text_view_item, null);
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

        if(testID==2)
            Collections.shuffle(buffer);

        for(TextView TV : buffer){
            L_PopUp.addView(TV);
        }



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
    }
    ///////////////////////////////////////////////////////////////////////////////////


    private void longClickTestIcon(){
        vibrate(Parameter.LongClick_Vibration_time);

        L_PopUp.setVisibility(View.VISIBLE);
        bt_Icon1.setVisibility(View.INVISIBLE);
        bt_Icon2.setVisibility(View.INVISIBLE);
        bt_Icon3.setVisibility(View.INVISIBLE);

        timeStart = System.currentTimeMillis();
    }

    private void clickItem(int selectedOption, View view){
        //Toast.makeText(getApplicationContext(), "option "+Parameter.Items[selectedOption]+" selected", Toast.LENGTH_SHORT).show();
        onClickScreen(view);

        long time = System.currentTimeMillis() - timeStart + 500; //+500ms -> time of longclick
        switch(testID){
            case 0: //Standard Icon (No Test)
                break;
            case 2: //Test1B
            case 4: //Test2B
                testService.onAnswer(selectedOption, StandardActivity.this, time);
                break;
            case 6://Test3B
                break;
        }


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


}