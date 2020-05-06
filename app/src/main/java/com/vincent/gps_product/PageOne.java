package com.vincent.gps_product;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PageOne extends PageView{

    private String name,uname,image,sex;
    private DBHelper dbHelper;
    private ListView listData;
    private TextView myId,addFri,noFri;
    private ImageView iv;
    private Context ctx;
    private Activity context;
    private ArrayList<ListItem> listallData = new ArrayList<>();

    public PageOne(final Context ctx, final Activity context, final ViewPager viewPager) {
        super(context);
        this.ctx = ctx;
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.page_one, null);

        listData = view.findViewById(R.id.listData);
        iv = view.findViewById(R.id.myImg);
        myId = view.findViewById(R.id.myId);
        noFri = view.findViewById(R.id.noFri);
        addFri = view.findViewById(R.id.addFri);
        addFri.setOnClickListener(new TextView.OnClickListener(){
            public void onClick(View view){

                LayoutInflater inflater = context.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_friend_item,null);
                AlertDialog.Builder d = new AlertDialog.Builder(ctx);
                d.setView(dialogView);
                d.setPositiveButton("加入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText FriName = (EditText)dialogView.findViewById(R.id.Friname);
                        String Friname = FriName.getText().toString();
                        if (Friname.equals(name)){
                            Toast.makeText(ctx,"不能加入自己哦!",Toast.LENGTH_SHORT).show();
                            FriName.setText("");
                        }else{
                            if (checkFri(Friname).equals("存在")){
                                Toast.makeText(ctx,"已經是您的好友囉!",Toast.LENGTH_SHORT).show();
                                FriName.setText("");
                            }else if (checkFri(Friname).equals("滿")){
                                Toast.makeText(ctx,"好友人數已達上限!",Toast.LENGTH_SHORT).show();
                            }else{
                                FriName.setText("");

                                String type = "latlngUpdate";
                                BackgroundTask backgroundTask = new BackgroundTask(ctx,context,"0","0");
                                backgroundTask.execute(type,Friname);
                            }
                    }
                    }
                });
                d.setNegativeButton("取消",null);
                d.show();
            }
        });

        SharedPreferences info = ctx.getSharedPreferences("Info",0);
        name = info.getString("Name","0");
        image = info.getString("Image","0");
        sex = info.getString("Sex","0");
        myId.setText(name);

        if (sex.equals("0")){
            String type = "findMy";
            BackgroundTask2 backgroundTask2 = new BackgroundTask2(ctx,context,0,0);
            backgroundTask2.execute(type,name);
        }else{
            if (image.equals("none")){
                if (sex.equals("1")){
                    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.boy);
                    RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.boy));
                    circleDrawable.getPaint().setAntiAlias(true);
                    circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                    iv.setImageDrawable(circleDrawable);
                }
                else{
                    Bitmap bitmap = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.girl);
                    RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(ctx.getResources(), BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.girl));
                    circleDrawable.getPaint().setAntiAlias(true);
                    circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                    iv.setImageDrawable(circleDrawable);
                }
            }else{
                Uri uri = Uri.parse(image);
                Picasso.with(ctx).load(uri).transform(new CircleTransForm()).into(iv);
            }
        }


        dbHelper = new DBHelper(ctx);

        showList();

        addView(view);
    }

    private void dialog(){
        AlertDialog.Builder d = new AlertDialog.Builder(ctx); //刪除好友
        d.setTitle("通知").setMessage("確定要刪除 "+uname+" 嗎?");
        d.setPositiveButton("刪除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] args = {uname};

                SQLiteDatabase db = dbHelper.getWritableDatabase();
                db.delete("friends","name=?",args);
                updateFri(); //把sqlite好友重新加到mysql
                showList();
            }
        });
        d.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        d.show();
    }

    private void updateFri(){ //把sqlite好友重新加到mysql
        int i=0;
        Cursor cursor = getCursor();
        while(cursor.moveToNext()){
            i++;
        }
        String[] friendName = new String[i+2];
        int x = 2;
        Cursor cursor2 = getCursor();
        friendName[0] = "updateFri";
        friendName[1] = name;
        while(cursor2.moveToNext()) {
            friendName[x] = cursor2.getString(1);
            x++;
        }
        Log.i("TAK",friendName.toString());

        BackgroundTask backgroundTask = new BackgroundTask(ctx,context,"0","0");
        backgroundTask.execute(friendName);
    }

    private Cursor getCursor(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"_id","name","lat","lng","checkImg","sex","image","time"};
        Cursor cursor = db.query("friends",columns,null,null,null,null,null);
        return cursor;
    }


    private void showList(){
        Log.i("TAK","1111111111111");
        listallData.clear();

        int i = 0;
        Cursor cursor = getCursor();
        while(cursor.moveToNext()) {
            i = 1;
            ListItem item = new ListItem();

            String friId = cursor.getString(0);
            String friName = cursor.getString(1);
            String friSex = cursor.getString(4);
            String friCheck = cursor.getString(5);
            String friImage = cursor.getString(6);
            String friTime = cursor.getString(7);

            Log.i("TAK",friName);

            item.setCheckImg(friCheck);
            item.setId(friId);
            item.setFriName(friName);
            item.setSex(friSex);
            item.setImage(friImage);
            item.setCheckImg(friCheck);
            item.setTime(friTime);

            listallData.add(item);
            CustomAdapter customAdapter = new CustomAdapter(ctx,listallData);
            //customAdapter.notifyDataSetChanged();
            listData.setAdapter(customAdapter);

            listener();
        }
        if (i == 0){
            listData.setAdapter(new CustomAdapter(ctx,listallData));
            noFri.setVisibility(View.VISIBLE);
        }
        else
            noFri.setVisibility(View.INVISIBLE);
    }

    private void listener(){
        listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tname = view.findViewById(R.id.friendId);
                uname = tname.getText().toString();
                dialog();
            }
        });
    }

    private String checkFri(String Friname){
        int i=0;
        Cursor cursor = getCursor();
        while(cursor.moveToNext()){
            String friendName = cursor.getString(1);
            i++;
            if (Friname.equals(friendName))
                return "存在";
        }
        if (i==10){
            return "滿";
        }
        return "OK";
    }
}
