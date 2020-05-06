package com.vincent.gps_product;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageButton;
import android.widget.ImageView;

import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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

public class BackgroundTask2 extends AsyncTask<String,Void,String> {

    Context ctx;
    Activity context;
    private int x,checkImg,checkBtn = 0;
    private String myId,Strname,Strlat,Strlng,StrcheckImg,Strsex,StrImage,Strpassword,Strtime,fri1,fri2,fri3,fri4,fri5,fri6,fri7,fri8,fri9,fri10;
    private DBHelper dbHelper;

    BackgroundTask2(Context ctx, Activity context,int x,int checkImg)
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

        //聯成IP: 192.168.1.136  家裡192.168.0.103  https://esodemo01.000webhostapp.com
        String search_url="https://esodemo01.000webhostapp.com/search.php";
        String updataFri_url="https://esodemo01.000webhostapp.com/updateFri.php";
        String latlngUpdate_url="https://esodemo01.000webhostapp.com/latlngUpdate.php";
        String findFri_url="https://esodemo01.000webhostapp.com/findFri.php";
        String findMy_url="https://esodemo01.000webhostapp.com/findMy.php";
        String type = params[0];


       if(type.equals("check")){
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
       }else if(type.equals("findMy")){
           String name = params[1];

           try {
               URL url=new URL(findMy_url);
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

                if (data.length() == 13){ //去找自己mysql的好友抓下來
                    StrcheckImg = data.getString("checkImg");
                    Strsex = data.getString("sex");
                    SharedPreferences info = context.getSharedPreferences("Info",0);
                    info.edit().putString("checkImg",StrcheckImg).commit();
                    info.edit().putString("sex",Strsex).commit();
                    String[] friend = new String[10];
                    fri1 = data.getString("friend1");
                    friend[0] = fri1;
                    fri2 = data.getString("friend2");
                    friend[1] = fri2;
                    fri3 = data.getString("friend3");
                    friend[2] = fri3;
                    fri4 = data.getString("friend4");
                    friend[3] = fri4;
                    fri5 = data.getString("friend5");
                    friend[4] = fri5;
                    fri6 = data.getString("friend6");
                    friend[5] = fri6;
                    fri7 = data.getString("friend7");
                    friend[6] = fri7;
                    fri8 = data.getString("friend8");
                    friend[7] = fri8;
                    fri9 = data.getString("friend9");
                    friend[8] = fri9;
                    fri10 = data.getString("friend10");
                    friend[9] = fri10;
                    for (int x=0;x<10;x++){
                        if (friend[x].equals("")){
                        }else{
                            String type = "latlngUpdate"; //抓到好友之後，送出找個別資料
                            BackgroundTask2 backgroundTask2 = new BackgroundTask2(ctx,context,0,0);
                            backgroundTask2.execute(type,friend[x]);
                        }
                    }
                }else if(data.length() == 7){ //找到個別資料後一一存進sqlite
                    checkImg = arrdata.length();
                    checkBtn++;
                    Strname = data.getString("name");
                    StrcheckImg = data.getString("checkImg");
                    Strsex = data.getString("sex");
                    Strlat = data.getString("lat");
                    Strlng = data.getString("lng");
                    StrImage = data.getString("image");
                    Strtime = data.getString("time");

                    add();
                }else if(data.length() == 6){ //update好友latlng
                    Strname = data.getString("name");
                    StrcheckImg = data.getString("checkImg");
                    Strlat = data.getString("lat");
                    Strlng = data.getString("lng");
                    StrImage = data.getString("image");
                    Strtime = data.getString("time");

                    update();
                }else if(data.length() == 3){
                    Strsex = data.getString("sex");
                    StrImage = data.getString("image");

                    ImageView iv = context.findViewById(R.id.myImg);
                    try{
                        if (StrImage.equals("none")){
                            if (Strsex.equals("1")){
                                SharedPreferences info = context.getSharedPreferences("Info",0);
                                info.edit().putString("Sex",Strsex).commit();
                                info.edit().putString("Image",StrImage).commit();
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy);
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy));
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                                iv.setImageDrawable(circleDrawable);
                            }
                            else{
                                SharedPreferences info = context.getSharedPreferences("Info",0);
                                info.edit().putString("Sex",Strsex).commit();
                                info.edit().putString("Image",StrImage).commit();
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.girl);
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy));
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                                iv.setImageDrawable(circleDrawable);
                            }
                        }else{
                            SharedPreferences info = context.getSharedPreferences("Info",0);
                            info.edit().putString("Sex",Strsex).commit();
                            info.edit().putString("Image",StrImage).commit();
                            Uri uri = Uri.parse(StrImage);
                            Picasso.with(ctx).load(uri).transform(new CircleTransForm()).into(iv);
                        }
                    }catch (Exception e){
                        if (StrImage.equals("none")){
                            if (Strsex.equals("1")){
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy);
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy));
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                                iv.setImageDrawable(circleDrawable);
                            }
                            else{
                                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.girl);
                                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), BitmapFactory.decodeResource(context.getResources(), R.mipmap.boy));
                                circleDrawable.getPaint().setAntiAlias(true);
                                circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                                iv.setImageDrawable(circleDrawable);
                            }
                        }else{
                            Uri uri = Uri.parse(StrImage);
                            Picasso.with(ctx).load(uri).transform(new CircleTransForm()).into(iv);
                        }
                    }
                }
            }


        } catch (Exception e) {
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

    private void update(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("checkImg",StrcheckImg);
        values.put("lat",Strlat);
        values.put("lng",Strlng);
        values.put("image",StrImage);
        values.put("time",Strtime);

        String[] args = {Strname};
        db.update("friends",values,"name=?",args);
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        getJson(result);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}