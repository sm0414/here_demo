package com.vincent.gps_product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class Friend extends AppCompatActivity {

    private ViewPager viewPager;
    private List<PageView> pageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);


    }

    protected void onResume(){
        super.onResume();

        findViews();

    }

    private void initData(){
        pageList = new ArrayList<>();
        pageList.add(new PageOne(this,this,viewPager));
        //pageList.add(new PageTwo(this,this,viewPager));
    }

    private void findViews(){
        viewPager = findViewById(R.id.pager);

        initData();

        viewPager.setAdapter(new SimplePageAdapter());
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private class SimplePageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }
        @Override
        public Object instantiateItem(ViewGroup container,int position){
            container.addView(pageList.get(position));
            return pageList.get(position);
        }

    }


}
