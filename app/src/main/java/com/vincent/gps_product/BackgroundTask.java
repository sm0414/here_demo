package com.vincent.gps_product;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class BackgroundTask extends AsyncTask<String,Void,String> {

    Context ctx;
    Activity context;
    private String x,checkImg;
    private String myId,Strname,Strlat,Strlng,StrcheckImg,Strsex,StrImage,Strtime,fri1,fri2,fri3,fri4,fri5,fri6,fri7,fri8,fri9,fri10,uname;
    private DBHelper dbHelper;
    private ArrayList<ListItem> listallData = new ArrayList<>();
    private ListView listData;
    private TextView noFri;

    BackgroundTask(Context ctx, Activity context,String x,String checkImg)
    {
        this.ctx = ctx;
        this.context = context;
        this.x = x;
        this.checkImg = checkImg;
        SharedPreferences info = ctx.getSharedPreferences("Info",0);
        myId = info.getString("Name","0");
        dbHelper = new DBHelper(ctx);
    }


    protected String doInBackground(String... params) {

        String register_url="https://esodemo01.000webhostapp.com/register.php"; 
        String register01_url="https://esodemo01.000webhostapp.com/register01.php";
        String login_url="https://esodemo01.000webhostapp.com/login.php";
        String latlng_url="https://esodemo01.000webhostapp.com/latlng.php";
        String search_url="https://esodemo01.000webhostapp.com/search.php";
        String updataFri_url="https://esodemo01.000webhostapp.com/updateFri.php";
        String latlngUpdate_url="https://esodemo01.000webhostapp.com/latlngUpdate.php";
        String findFri_url="https://esodemo01.000webhostapp.com/findFri.php";
        String type = params[0];

        Log.i("TAG",ctx+type);

        if(type.equals("register")) {
            String name = params[1];
            String password = params[2];
            String checkImg = params[3];
            String sex = params[4];

            try {
                URL url = new URL(register_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                        URLEncoder.encode("checkImg","UTF-8")+"="+URLEncoder.encode(checkImg,"UTF-8")+"&"+
                        URLEncoder.encode("sex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                System.out.println(1);
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("register01")) {
            String name = params[1];
            String password = params[2];
            String checkImg = params[3];
            String sex = params[4];
            String image = params[5];

            try {
                URL url = new URL(register01_url);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8")+"&"+
                        URLEncoder.encode("checkImg","UTF-8")+"="+URLEncoder.encode(checkImg,"UTF-8")+"&"+
                        URLEncoder.encode("sex","UTF-8")+"="+URLEncoder.encode(sex,"UTF-8")+"&"+
                        URLEncoder.encode("image","UTF-8")+"="+URLEncoder.encode(image,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();
                System.out.println(1);
                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("login")){
            String name = params[1];
            String password = params[2];

            try {
                URL url=new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if (type.equals("location")){
            String lat = params[1];
            String lng = params[2];
            String time = params[3];
            String name = params[4];
            try {
                URL url = new URL(latlng_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("lat","UTF-8")+"="+URLEncoder.encode(lat,"UTF-8")+"&"+
                        URLEncoder.encode("lng","UTF-8")+"="+URLEncoder.encode(lng,"UTF-8")+"&"+
                        URLEncoder.encode("time","UTF-8")+"="+URLEncoder.encode(time,"UTF-8")+"&"+
                        URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("check")){
            String name = params[1];
            System.out.println(name);
            try {
                URL url=new URL(search_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");


                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("updateFri")){

            try {
                URL url=new URL(updataFri_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                StringBuilder data = new StringBuilder();

                for(int i=1;i<params.length;i++){
                    if (i == 1){
                        String t1 = URLEncoder.encode("name","UTF-8");
                        String t2 = URLEncoder.encode(params[i],"UTF-8");
                        data.append(t1+"="+t2+"&");
                    }else{
                        String t1 = URLEncoder.encode("friend"+(i-1),"UTF-8");
                        String t2 = URLEncoder.encode(params[i],"UTF-8");
                        if (i != (params.length-1)){
                            data.append(t1+"="+t2+"&");
                        }else if(i == (params.length-1)){
                            data.append(t1+"="+t2);
                        }
                    }

                }


                bufferedWriter.write(data.toString());
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("latlngUpdate")){
            String name = params[1];

            try {
                URL url=new URL(latlngUpdate_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(type.equals("findFri")){
            String name = params[1];

            try {
                URL url=new URL(findFri_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();
                OutputStream os = httpURLConnection.getOutputStream();

                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));

                String data= URLEncoder.encode("name","UTF-8")+"="+URLEncoder.encode(name,"UTF-8");

                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
                String result = "";
                String line = "";

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                is.close();
                bufferedReader.close();
                httpURLConnection.disconnect();

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void getJson(String result) {
        try {
            JSONArray arrdata = new JSONArray(result);

            for (int i=0;i<arrdata.length();i++){
                JSONObject data = arrdata.getJSONObject(i);

                if(data.length() == 7){ //加新好友進sqlite
                    Strname = data.getString("name");
                    StrcheckImg = data.getString("checkImg");
                    Strsex = data.getString("sex");
                    Strlat = data.getString("lat");
                    Strlng = data.getString("lng");
                    StrImage = data.getString("image");
                    Strtime = data.getString("time");

                    add();
                    updateFri(); //把sqlite好友重新加到mysql
                    showList();
                }
            }
        } catch (JSONException e) {
            Log.i("TAG","9999");
            e.printStackTrace();
        }
    }


    private void add(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name",Strname);
        values.put("lat",Strlat);
        values.put("lng",Strlng);
        values.put("checkImg",StrcheckImg);
        values.put("sex",Strsex);
        values.put("image",StrImage);
        values.put("time",Strtime);


        long res = db.insert("friends",null,values);
        if (res == -1)
            Toast.makeText(ctx, "新增失敗", Toast.LENGTH_SHORT).show();
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
        friendName[1] = myId;
        while(cursor2.moveToNext()) {
            friendName[x] = cursor2.getString(1);
            x++;
        }
        Log.i("TAG",friendName.toString());

        BackgroundTask backgroundTask = new BackgroundTask(ctx,context,"0","0");
        backgroundTask.execute(friendName);
    }

    private void showList(){
        listData = context.findViewById(R.id.listData);
        listallData.clear();
        noFri = context.findViewById(R.id.noFri);
        int i = 0;
        //listData.setAdapter(null);
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

            item.setCheckImg(friCheck);
            item.setId(friId);
            item.setFriName(friName);
            item.setSex(friSex);
            item.setImage(friImage);
            item.setCheckImg(friCheck);
            item.setTime(friTime);


            listallData.add(item);

            listData.setAdapter(new CustomAdapter(ctx,listallData));

            listData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView tname = view.findViewById(R.id.friendId);
                    uname = tname.getText().toString();
                    dialog();
                }
            });
        }
        if (i == 0){
            listData.setAdapter(new CustomAdapter(ctx,listallData));
            noFri.setVisibility(View.VISIBLE);
        }

        else
            noFri.setVisibility(View.INVISIBLE);
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

    private Cursor getCursor(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"_id","name","lat","lng","checkImg","sex","image","time"};
        Cursor cursor = db.query("friends",columns,null,null,null,null,null);
        return cursor;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.equals("註冊成功")){
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
            TextView myId = (TextView) context.findViewById(R.id.myId);
            String name = myId.getText().toString();
            SharedPreferences info = context.getSharedPreferences("Info",0);
            if(checkImg.equals("0"))
                info.edit().putString("Name",name).putString("Sex",x).putString("Image","none").commit();
            else
                info.edit().putString("Name",name).putString("Sex",x).commit();
            Intent it = new Intent(ctx,MapsActivity.class);
            context.startActivity(it);
            context.finish();
            ProgressDialogUtil.dismiss();
        }else if (result.equals("註冊失敗")){
            ProgressDialogUtil.dismiss();
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }else if(result.equals("該暱稱已存在")){
            ProgressDialogUtil.dismiss();
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }else if(result.equals("登入成功")){
            Log.i("TAG",result);
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
            TextView myId = (TextView) context.findViewById(R.id.myId);
            String name = myId.getText().toString();
            SharedPreferences info = context.getSharedPreferences("Info",0);
            info.edit().putString("Name",name).commit();
            Intent it = new Intent(ctx,MapsActivity.class);
            context.startActivity(it);
            context.finish();
            ImageButton login = context.findViewById(R.id.Login1);
            login.setEnabled(true);
            ProgressDialogUtil.dismiss();
        }else if(result.equals("暱稱或密碼輸入錯誤")){
            ProgressDialogUtil.dismiss();
            ImageButton login = context.findViewById(R.id.Login1);
            login.setEnabled(true);
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }else if(result.equals("找不到該用戶")){
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }else if(result.equals("成功")){
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }else if(result.equals("加入失敗")){
            Toast.makeText(ctx,result,Toast.LENGTH_SHORT).show();
        }
        else{
            getJson(result);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}