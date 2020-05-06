package com.vincent.gps_product;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Time;
import java.util.Calendar;

public class Login extends AppCompatActivity {

    private EditText myId,myPw;
    private ImageButton Login1;
    private String name;
    private TextView call;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        myId = findViewById(R.id.myId);
        myPw = findViewById(R.id.myPw);
        Login1 = findViewById(R.id.Login1);
        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it =new Intent(Intent.ACTION_VIEW, Uri.parse("tel:0922992514"));
                startActivity(it);
            }
        });
        Login1.setOnClickListener(Login1Listener);
    }

    protected void onResume(){
        super.onResume();
        Log.i("TAK","come");

        try{
            SharedPreferences info = getApplicationContext().getSharedPreferences("Info",0);
            String check_login = info.getString("Name","0");
            if (check_login.equals("0")){
                info.edit().putString("Check","one").commit();
            }else{
                startActivity(new Intent(this,MapsActivity.class));
                finish();
            }
        }catch (Exception e){}

    }

    public void btnRegister(View view){
        startActivityForResult(new Intent(this,Register.class),1010);
        finish();
    }



    private ImageButton.OnClickListener Login1Listener = new ImageButton.OnClickListener(){
        public void onClick(View v){
            ProgressDialogUtil.showProgressDialog(Login.this,"               登入中...               ");
            Login1.setEnabled(false);
            String type = "login";
            BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext(),Login.this,"0","0");
            backgroundTask.execute(type,myId.getText().toString(),myPw.getText().toString());
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (permissions[0]) {
            case Manifest.permission.ACCESS_FINE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                }
                break;
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED){
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                    }
                }
                break;
            default:
                break;
        }
    }



}
