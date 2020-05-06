package com.vincent.gps_product;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private String name,Check;
    private boolean RUN = true;
    private ImageButton btnFri;
    private LatLng myLocation, myLastLocation;
    private LatLng nullLocation = new LatLng(23.738609, 120.952061);
    private Marker marker2;
    private Marker[] s;

    private int a = 1;
    double lat1;
    double lng1;

    LocationManager locationManager;
    MyLocationListener myLocationListener;
    private BackgroundTask2 backgroundTask2;
    private BackgroundTask backgroundTask;
    private DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        findView();
        btnFri.setEnabled(false);

        s = new Marker[10];

        SharedPreferences info = getSharedPreferences("Info", 0);
        name = info.getString("Name", "0");
        Check = info.getString("Check","0");

        if(Check.equals("one")){
            Log.i("TAK","000");
            dbHelper = new DBHelper(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.delete("friends", null, null);

            String type = "check";
            backgroundTask2 = new BackgroundTask2(getApplicationContext(), MapsActivity.this, 0, 0);
            backgroundTask2.execute(type, name);

            info.edit().putString("Check","two").commit();
        }



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        myLocationListener = new MyLocationListener();

        //mMap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(),location.getLongitude())).title(name).icon(bitmapDescriptor));

    }



    private Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            try {
                while (RUN) {
                    Thread.sleep(200);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            updatelatlng();
                            updateFri();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };

    private Cursor getCursor(){
        dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] columns = {"_id","name","lat","lng","checkImg","sex","image","time"};
        Cursor cursor = db.query("friends",columns,null,null,null,null,null);
        return cursor;
    }


    private void updateFri(){ //把好友latlng紮針

        for (int i=0;i<10;i++){
            try{
                s[i].remove();
            }catch (Exception e){ }
        }
        int x=0;

        Cursor cursor = getCursor();

        while(cursor.moveToNext()){

            try{
                double friLat = Double.valueOf(cursor.getString(2));
                double friLng = Double.valueOf(cursor.getString(3));

                String friendName = cursor.getString(1);
                String sex = cursor.getString(5);
                String image = cursor.getString(6);

                if (image.equals("none")){

                    if(sex.equals("1")){
                        LayoutInflater inflater = getLayoutInflater();
                        View view=inflater.inflate(R.layout.custom_marker,null);
                        TextView txt = (TextView) view.findViewById(R.id.friName);
                        txt.setText(friendName);
                        ImageView iv = (ImageView) view.findViewById(R.id.marker_image);
                        Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), R.mipmap.boy);
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), BitmapFactory.decodeResource(view.getResources(), R.mipmap.boy));
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                        iv.setImageDrawable(circleDrawable);
                        Bitmap viewBitmap = getViewBitmap(view);

                        s[x] = mMap.addMarker(new MarkerOptions().position(new LatLng(friLat,friLng)).title(friendName).icon(BitmapDescriptorFactory.fromBitmap(viewBitmap)));
                    }
                    else{
                        LayoutInflater inflater = getLayoutInflater();
                        View view=inflater.inflate(R.layout.custom_marker,null);
                        TextView txt = (TextView) view.findViewById(R.id.friName);
                        txt.setText(friendName);
                        ImageView iv = (ImageView) view.findViewById(R.id.marker_image);
                        Bitmap bitmap = BitmapFactory.decodeResource(view.getResources(), R.mipmap.girl);
                        RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(view.getResources(), BitmapFactory.decodeResource(view.getResources(), R.mipmap.boy));
                        circleDrawable.getPaint().setAntiAlias(true);
                        circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                        iv.setImageDrawable(circleDrawable);
                        Bitmap viewBitmap = getViewBitmap(view);

                        s[x] = mMap.addMarker(new MarkerOptions().position(new LatLng(friLat,friLng)).title(friendName).icon(BitmapDescriptorFactory.fromBitmap(viewBitmap)));
                    }
                }else{

                    LayoutInflater inflater = getLayoutInflater();
                    View view=inflater.inflate(R.layout.custom_marker,null);
                    TextView txt = (TextView) view.findViewById(R.id.friName);
                    txt.setText(friendName);
                    ImageView iv = (ImageView) view.findViewById(R.id.marker_image);
                    Picasso.with(getApplicationContext()).load(image).transform(new CircleTransForm()).into(iv);
                    Bitmap viewBitmap = getViewBitmap(view);

                    s[x] = mMap.addMarker(new MarkerOptions().position(new LatLng(friLat,friLng)).title(friendName).icon(BitmapDescriptorFactory.fromBitmap(viewBitmap)));
                }

                x++;
            }catch(Exception e){
                e.printStackTrace();
            }
        }

        btnFri.setEnabled(true);
    }

    private Bitmap getViewBitmap(View addViewContent) {

        addViewContent.setDrawingCacheEnabled(true);

        addViewContent.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        addViewContent.layout(0, 0,
                addViewContent.getMeasuredWidth(),
                addViewContent.getMeasuredHeight());

        addViewContent.buildDrawingCache();
        Bitmap cacheBitmap = addViewContent.getDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        return bitmap;
    }

    private void updatelatlng(){ //不斷更新好友latlng至sqlite
        //Toast.makeText(getApplicationContext(),"updatelatlng()",Toast.LENGTH_SHORT).show();

        Cursor cursor = getCursor();
        while (cursor.moveToNext()){
            String friendName = cursor.getString(1);
            String type = "findFri";

            backgroundTask2 = new BackgroundTask2(getApplicationContext(),MapsActivity.this,0,0);
            backgroundTask2.execute(type,friendName);
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,1000,0,myLocationListener);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,0,myLocationListener);

    }

    protected void onResume() {
        super.onResume();
        //Toast.makeText(getApplicationContext(),"come",Toast.LENGTH_SHORT).show();
        RUN = true;
        Thread t2 = new Thread(runnable2);
        t2.start();

        getCurrentLocation();
    }


    protected void onPause(){
        RUN = false;
        locationManager.removeUpdates(myLocationListener);
        dbHelper.close();
        super.onPause();
    }

    protected void onDestroy(){
        //RUN = false;

        //locationManager.removeUpdates(myLocationListener);

        //dbHelper.close();

        super.onDestroy();
    }

    @Override
    protected void onRestart() {


        super.onRestart();
    }

    private class MyLocationListener implements LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                if (a == 1){
                    try{
                        marker2.remove();
                    }catch (Exception e){ }
                    myLocation = new LatLng(lat,lng);
                    marker2 = mMap.addMarker(new MarkerOptions().position(myLocation).title(name).icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker)));
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(myLocation,15);
                    mMap.moveCamera(cameraUpdate);
                    lat1 = lat;
                    lng1 = lng;
                    a = 2;
                    String type = "location";
                    backgroundTask = new BackgroundTask(getApplicationContext(),getParent(),"0","0");
                    backgroundTask.execute(type,Double.toString(lat1),Double.toString(lng1),getTime(),name);
                }

                if (lat != lat1 || lng != lng1){
                    marker2.remove();
                    lat1 = lat;
                    lng1 = lng;
                    myLocation = new LatLng(lat,lng);
                    marker2 = mMap.addMarker(new MarkerOptions().position(myLocation).title(name).icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker)));

                    String type = "location";
                    backgroundTask = new BackgroundTask(getApplicationContext(),getParent(),"0","0");
                    backgroundTask.execute(type,Double.toString(lat1),Double.toString(lng1),getTime(),name);
                }


            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {

            //getCurrentLocation();

        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(),"請開啟GPS定位",Toast.LENGTH_SHORT).show();
            try{
                marker2.remove();
            }catch (Exception e){ }

        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        //marker1 = mMap.addMarker(new MarkerOptions().position(myLastLocation).title(name).icon(bitmapDescriptor));

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(nullLocation,8);
        mMap.moveCamera(cameraUpdate);

    }

    private void findView(){
        btnFri = findViewById(R.id.btnFri);
        btnFri.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Intent it = new Intent(getApplicationContext(),Friend.class);
                startActivity(it);
            }
        });
    }

    private String getTime(){
        String Strminute;
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);

        int month = calendar.get(Calendar.MONTH)+1;

        int day = calendar.get(Calendar.DAY_OF_MONTH);

        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        int minute = calendar.get(Calendar.MINUTE);

        if(minute/10 <1){
            Strminute = "0"+minute;
        }else
            Strminute = Integer.toString(minute);


        String time = month+"月"+day+"日  "+hour+":"+Strminute;

        return time;
    }

    private static Boolean isQuit = false;
    Timer timer = new Timer();
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isQuit == false) {
                isQuit = true;
                Toast.makeText(getApplicationContext(), "再按一次以退出", Toast.LENGTH_SHORT).show();
                TimerTask task = null;
                task = new TimerTask() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                };
                timer.schedule(task, 1500);
            } else {
                finish();
            }
        }
        return false;
    }
}