package com.vincent.gps_product;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class StartView extends AppCompatActivity {

    private static final int GOTO_MAIN_ACTIVITY = 0;
    private static boolean check = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_view);
        Log.i("TAK","0");
        if(check == false)
            startActivityForResult(new Intent(getApplicationContext(),Login.class),GOTO_MAIN_ACTIVITY);
        else
            mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 400);
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:
                    startActivityForResult(new Intent(getApplicationContext(),Login.class),GOTO_MAIN_ACTIVITY);
                    break;

                default:
                    break;
            }
        }

    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case GOTO_MAIN_ACTIVITY:
                check = false;
                finish();
        }
    }
    protected void onResume(){
        super.onResume();
        Log.i("TAK","1");
    }


}

