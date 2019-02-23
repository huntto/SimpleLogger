package com.ihuntto.aop_simplelogger;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SimpleLog.d();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SimpleLog.d();
    }

    @Override
    protected void onStart() {
        super.onStart();
        SimpleLog.d();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SimpleLog.d();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SimpleLog.d();
    }

    @Override
    protected void onStop() {
        super.onStop();
        SimpleLog.d();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SimpleLog.d();
    }
}
