package com.vincent.gps_product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter{
    private LayoutInflater layoutInflater; //客製化layout
    private ArrayList<ListItem> listdata;
    private Context ctx;

    public CustomAdapter(Context context, ArrayList<ListItem> listdata){
        ctx = context;
        this.listdata = listdata;
        layoutInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        return listdata.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = layoutInflater.inflate(R.layout.friend_item,parent,false);

        ListItem item = listdata.get(position);

        TextView name = (TextView)convertView.findViewById(R.id.friendId);
        name.setText(item.getFriName());
        TextView time = (TextView)convertView.findViewById(R.id.friendTime);
        time.setText(item.getTime());
        ImageView iv = (ImageView)convertView.findViewById(R.id.friendImg);
        String image = item.getImage();

        if (image.equals("none")){
            String sex = item.getSex();
            if (sex.equals("1")){
                Bitmap bitmap = BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.girl);
                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(convertView.getResources(), BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.boy));
                circleDrawable.getPaint().setAntiAlias(true);
                circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                iv.setImageDrawable(circleDrawable);
            }
            else{
                Bitmap bitmap = BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.girl);
                RoundedBitmapDrawable circleDrawable = RoundedBitmapDrawableFactory.create(convertView.getResources(), BitmapFactory.decodeResource(convertView.getResources(), R.mipmap.boy));
                circleDrawable.getPaint().setAntiAlias(true);
                circleDrawable.setCornerRadius(Math.max(bitmap.getWidth(), bitmap.getHeight()));
                iv.setImageDrawable(circleDrawable);
            }
        }else{
            Uri uri = Uri.parse(image);
            Picasso.with(ctx).load(uri).transform(new CircleTransForm()).into(iv);
        }

        return convertView;
    }


}
