package com.vincent.gps_product;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


public class PageTwo extends PageView{

    private Context ctx;
    private Activity context;
    private String name,uname;
    private EditText friName;
    private DBHelper dbHelper;
    private ListView listData;
    private BackgroundTask backgroundTask;
    private Button btnFri;
    private ViewPager viewPager;

    public PageTwo(Context ctx, Activity context,ViewPager viewPager) {
        super(context);
        this.ctx = ctx;
        this.context = context;
        this.viewPager = viewPager;
        View view = LayoutInflater.from(context).inflate(R.layout.page_two, null);

        friName = view.findViewById(R.id.friName);
        btnFri = view.findViewById(R.id.btnFri);
        btnFri.setOnClickListener(btnFriLis);
        listData = findViewById(R.id.listData);

        dbHelper = new DBHelper(ctx);

        SharedPreferences info = ctx.getSharedPreferences("Info",0);
        name = info.getString("Name","0");

        addView(view);
    }

    Button.OnClickListener btnFriLis = new Button.OnClickListener(){ //加新好友
        public void onClick(View view){
            String friname = friName.getText().toString();
            if (friname.equals(name)){
                Toast.makeText(ctx,"不能加入自己哦!",Toast.LENGTH_SHORT).show();
                friName.setText("");
            }else{
                if (checkFri().equals("存在")){
                    Toast.makeText(ctx,"已經是您的好友囉!",Toast.LENGTH_SHORT).show();
                    friName.setText("");
                }else if (checkFri().equals("滿")){
                    Toast.makeText(ctx,"好友人數已達上限!",Toast.LENGTH_SHORT).show();
                }else{
                    Adddialog();
                }
            }
        }
    };

    private void Adddialog(){
        String friname = friName.getText().toString();
        AlertDialog.Builder d = new AlertDialog.Builder(ctx); //都一樣
        d.setTitle("通知").setMessage("確定要加入 "+friname+" 嗎?").setCancelable(false);
        d.setPositiveButton("加入", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String friname = friName.getText().toString();
                friName.setText("");

                String type = "latlngUpdate";
                backgroundTask = new BackgroundTask(ctx,context,"0","0");
                backgroundTask.execute(type,friname);

                viewPager.setCurrentItem(0);
            }
        });
        d.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        d.show();
    }


    private Cursor getCursor(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"_id","name","lat","lng","checkImg","sex"};
        Cursor cursor = db.query("friends",columns,null,null,null,null,null);
        return cursor;
    }



    private String checkFri(){
        int i=0;
        Cursor cursor = getCursor();
        while(cursor.moveToNext()){
            String friendName = cursor.getString(1);
            i++;
            if (friName.getText().toString().equals(friendName))
                return "存在";
        }
        if (i==10){
            return "滿";
        }
        return "OK";
    }
}
