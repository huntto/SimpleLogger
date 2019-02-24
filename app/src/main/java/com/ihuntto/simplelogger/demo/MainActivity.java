package com.ihuntto.simplelogger.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ihuntto.simplelogger.annotations.DebugTrace;


public class MainActivity extends AppCompatActivity {

    @Override
    @DebugTrace
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    @DebugTrace
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    @DebugTrace
    protected void onStart() {
        super.onStart();
    }

    @Override
    @DebugTrace
    protected void onResume() {
        super.onResume();
    }

    @Override
    @DebugTrace
    protected void onPause() {
        super.onPause();
    }

    @Override
    @DebugTrace
    protected void onStop() {
        super.onStop();
    }

    @Override
    @DebugTrace
    protected void onDestroy() {
        super.onDestroy();
    }
}
