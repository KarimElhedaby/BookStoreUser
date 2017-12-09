package com.hamza.alif.bookstoreuser.Ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.hamza.alif.bookstoreuser.R;


public class SplachActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach);

        //wait 3000 mSecs  --------------------------------
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               Intent in = new Intent(SplachActivity.this,MainActivity.class);
               startActivity(in);
                finish();
            }
        }, 3000);

    }
}