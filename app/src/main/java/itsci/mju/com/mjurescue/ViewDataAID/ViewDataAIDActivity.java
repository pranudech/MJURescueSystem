package itsci.mju.com.mjurescue.ViewDataAID;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import itsci.mju.com.mjurescue.ListTitleAID.AIDBean;
import itsci.mju.com.mjurescue.ListTitleAID.CallAIDListService;
import itsci.mju.com.mjurescue.ListTitleAID.JSONCallAIDListServiceAsyncTask;
import itsci.mju.com.mjurescue.R;

import static android.content.ContentValues.TAG;
import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;

public class ViewDataAIDActivity extends AppCompatActivity implements CallAIDListService {

    private String selectedByAidId = null;
    private String url_ViewDataAIDServlet = null;
    ArrayList<AIDBean> newsModelArrayList = null;
    TextView txtTitleAid;
    TextView txtDetailAid;
    ImageView txtImageAid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data_aid);

        Intent intent = getIntent();
        selectedByAidId = intent.getStringExtra("selectedByAidId");
        url_ViewDataAIDServlet = URL_SERVLET+"/ViewDataAIDMobileServlet?selectedByAidId="+selectedByAidId;
        new JSONCallAIDListServiceAsyncTask((CallAIDListService) this).execute(url_ViewDataAIDServlet);
        txtTitleAid = (TextView) findViewById(R.id.txtTitleAid);
        txtDetailAid = (TextView) findViewById(R.id.txtDetailAid);
        txtImageAid = (ImageView) findViewById(R.id.imageAID);

        
    }

    @Override
    public void onPreCallService() {
    }

    @Override
    public void onCallService() {

    }

    @Override
    public void onRequestCompleteListener(ArrayList<AIDBean> newsModelArrayList) {
        if (newsModelArrayList != null && newsModelArrayList.size() > 0) {
            Log.i("Check data", "" + newsModelArrayList.size());
            this.newsModelArrayList = newsModelArrayList;
            for(int i = 0; i<newsModelArrayList.size(); i++) {
                txtTitleAid.setText(newsModelArrayList.get(i).getTitle());
                txtDetailAid.setText(newsModelArrayList.get(i).getDetail());
                if(newsModelArrayList.get(i).getImage().equals("")){
                    Log.d(TAG,"ViewDataAIDActivity *** newsModelArrayList.get(i).getImage() = null");
                }else{
                    txtImageAid.setMaxWidth(180);
                    txtImageAid.setMaxHeight(180);
                    Picasso.with(ViewDataAIDActivity.this).load(newsModelArrayList.get(i).getImage()).into(txtImageAid);
                }
            }
        }
    }

    @Override
    public void onRequestFailed(String result) {

    }
}


