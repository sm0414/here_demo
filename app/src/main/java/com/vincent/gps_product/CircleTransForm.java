package com.vincent.gps_product;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.squareup.picasso.Transformation;

public class CircleTransForm implements Transformation {


    @Override
    public Bitmap transform(Bitmap source) {
        int size = Math.min(source.getWidth(), source.getHeight());
        int x = (source.getWidth() - size) / 2;
        int y = (source.getHeight() - size) / 2;
        Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
        if (squaredBitmap != source) {
            source.recycle();
        }
        Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        BitmapShader shader = new BitmapShader(squaredBitmap,
                BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setAntiAlias(true);


        //绘制边框
        //Paint mBorderPaint = new Paint();
        //mBorderPaint.setStyle(Paint.Style.STROKE);
        //mBorderPaint.setStrokeWidth(40);
        //mBorderPaint.setColor(Color.WHITE);
        //mBorderPaint.setStrokeCap(Paint.Cap.ROUND);
        //mBorderPaint.setAntiAlias(true);



        float r = size / 2f;
        //float r1 = (size-2*40) / 2f;
        //canvas.drawCircle(r, r, r1, mBorderPaint);
        canvas.drawCircle(r, r, r, paint);

        squaredBitmap.recycle();
        return bitmap;
    }
    @Override
    public String key() {
        return "circle";
    }
}