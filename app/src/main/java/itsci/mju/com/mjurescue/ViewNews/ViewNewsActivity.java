package itsci.mju.com.mjurescue.ViewNews;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import itsci.mju.com.mjurescue.R;

public class ViewNewsActivity extends AppCompatActivity{

    private String selectedByImage = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_view_news);

        Intent intent = getIntent();
        selectedByImage = intent.getStringExtra("selectedByImage");


        GridView gridView = (GridView) findViewById(R.id.gridViewNewsFeed);
        gridView.setAdapter(new GridViewAdapter(this,selectedByImage));

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            gridView.setNumColumns(4);
        }else{
            gridView.setNumColumns(2);
        }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ViewNewsActivity.this,ViewImageActivity.class);
                intent.putExtra("img", new GridViewAdapter(ViewNewsActivity.this,selectedByImage).getItem(position));
                startActivity(intent);
            }
        });
    }
}

