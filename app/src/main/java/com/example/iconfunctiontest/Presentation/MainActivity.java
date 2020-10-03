package com.example.iconfunctiontest.Presentation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.iconfunctiontest.R;
import com.example.iconfunctiontest.Services.Parameter;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private Bundle bundle;
    TextInputEditText userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userID = findViewById(R.id.input_ID);

        /* was used when return to main-menu after test was finished
        bundle = getIntent().getExtras();

        if(bundle!=null){
            userID.setText(bundle.getString("USERID"));
        }

         */
    }

    public void onClickBt_Standard(View view) {
        Intent i = new Intent(MainActivity.this, StandardActivity.class);
        i.putExtra("TRIAL", "");
        i.putExtra("TARGET",-1);
        i.putExtra("testID",0);
        startActivity(i);
        Animatoo.animateDiagonal(MainActivity.this);
    }

    public void onClickBt_Alive(View view){
        Intent i = new Intent(MainActivity.this, AliveActivity.class);
        i.putExtra("TRIAL", "");
        i.putExtra("TARGET",-1);
        i.putExtra("testID",0);
        startActivity(i);
        Animatoo.animateDiagonal(MainActivity.this);

    }

    public void onClickBt_Test(View view){
        String username = userID.getText().toString();
        if(username.matches("")){
            Toast.makeText(getApplicationContext(), "Enter  your ID first!", Toast.LENGTH_LONG).show();
        }
        else{
            Parameter.setUserID(username);
            Intent i = new Intent(MainActivity.this, TestMenuActivity.class);
            startActivity(i);
            Animatoo.animateSlideUp(MainActivity.this);

            //Ask user for permission to write csv file
            if (Build.VERSION.SDK_INT >= 23) {
                int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }
            }
        }

    }

}