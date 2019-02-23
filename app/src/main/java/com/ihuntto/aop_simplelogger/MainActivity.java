package com.ihuntto.aop_simplelogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    @Logcat
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    @Logcat
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    @Logcat
    protected void onStart() {
        super.onStart();
    }

    @Override
    @Logcat
    protected void onResume() {
        super.onResume();
    }

    @Override
    @Logcat
    protected void onPause() {
        super.onPause();
    }

    @Override
    @Logcat
    protected void onStop() {
        super.onStop();
    }

    @Override
    @Logcat
    protected void onDestroy() {
        super.onDestroy();
    }
}
