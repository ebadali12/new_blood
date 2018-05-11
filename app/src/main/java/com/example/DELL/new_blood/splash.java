package com.example.DELL.new_blood;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkUser();


    }
    public void checkUser(){

        Boolean Check = Boolean.valueOf(UtilsClipCodes.readSharedSetting(splash.this, "ClipCodes", "true"));

        Intent introIntent = new Intent(splash.this, DRLoginActivity.class);
        introIntent.putExtra("ClipCodes", Check);

        if (Check) {
            startActivity(introIntent);
        }
        else{
             startActivity(new Intent(splash.this,drawer.class));
        }


    }

}
