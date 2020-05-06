package com.vincent.gps_product;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;


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

public class BackgroundTask3 extends AsyncTask<String,Void,String> {

    Context ctx;
    Activity context;
    private String myId;

    BackgroundTask3(Context ctx, Activity context)
    {
        this.ctx = ctx;
        this.context = context;
        SharedPreferences info = ctx.getSharedPreferences("Info",0);
        myId = info.getString("Name","0");
    }


    protected String doInBackground(String... params) {

        //聯成IP: 192.168.1.136  家裡192.168.0.103
        String res_url="http://192.168.0.103/verify_res.php";
        String fri_url="http://192.168.0.103/verify_fri.php";
        String type = params[0];

        Log.i("TAK",ctx+type);

        if(type.equals("verify_res")){
            String name = params[1];
            Log.i("TAK",name);
            try {
                URL url=new URL(res_url);
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


            }
        } catch (JSONException e) {
            Log.i("TAG","9999");
            e.printStackTrace();
        }
    }





    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
        //getJson(result);

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}