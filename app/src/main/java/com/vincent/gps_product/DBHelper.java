package com.vincent.gps_product;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context,"contact.db",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table friends(  _id integer primary key autoincrement,name varchar(11),lat varchar(30),lng varchar(30),checkImg varchar(2),sex varchar(2),image varchar(300),time varchar(20));";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
