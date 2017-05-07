package itsci.mju.com.mjurescue.ViewNews;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.R;

public class GridViewAdapter extends BaseAdapter {

    private final Context context; //Activity ที่เราจะจัดการ
    private final List<String> urls = new ArrayList<String>(); //ArrayList urls

    public GridViewAdapter(Context context,String image) {
        this.context = context;

        String arr[] = image.split("##");
        for (int i = 0; i < arr.length; i++) {
            urls.add(arr[i]);
        }
    }



    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public String getItem(int position) {
        //คืนค่า urls ออกไปทีละตำแหน่ง
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convertView คือแต่ละช่องทั้งช่อง , parent ทั้ง GridView
        ImageView imageView = (ImageView) convertView;

        //ไม่ต้องสร้างมันใหม่ ที่มันสร้างมาแล้ว สกอขึ้นก็ให้รียุสอันเดิม
        if(imageView == null){
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(550,550));
        }
        String url = this.getItem(position);
        Glide.with(context).load(url).centerCrop().placeholder(R.mipmap.ic_launcher).crossFade().into(imageView);
        return imageView;
    }
}
