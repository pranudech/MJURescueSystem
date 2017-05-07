package itsci.mju.com.mjurescue.ViewNews;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import itsci.mju.com.mjurescue.R;

public class ViewImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        Intent intent = getIntent();
        String url = (String) intent.getStringExtra("img");

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        Glide.with(this).load(url).centerCrop().placeholder(R.mipmap.ic_launcher).crossFade().into(imageView);
    }
}
