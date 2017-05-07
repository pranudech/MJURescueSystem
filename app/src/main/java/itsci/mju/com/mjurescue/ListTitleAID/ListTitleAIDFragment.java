package itsci.mju.com.mjurescue.ListTitleAID;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import itsci.mju.com.mjurescue.R;
import itsci.mju.com.mjurescue.ViewDataAID.ViewDataAIDActivity;

import static android.content.ContentValues.TAG;
import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;

public class ListTitleAIDFragment extends ListFragment implements CallAIDListService {

    private String url_ListTitleAIDServlet = URL_SERVLET+"/ListTitleAIDMobileServlet";
    private ListAIDAdapter listAIDAdapter;
    private ListView listViewNews;
    ArrayList<AIDBean> newsModelArrayList = null;

    public ListTitleAIDFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_aid, container, false);

        new JSONCallAIDListServiceAsyncTask((CallAIDListService) this).execute(url_ListTitleAIDServlet);
        listViewNews = (ListView) view.findViewById(android.R.id.list);
        newsModelArrayList = new ArrayList<AIDBean>();
        listViewNews.setAdapter(listAIDAdapter = new ListAIDAdapter());

        return view ;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this.getActivity(), ViewDataAIDActivity.class);
        String selectedByAidId = newsModelArrayList.get(position).getAidId();
        intent.putExtra("selectedByAidId",selectedByAidId);
        startActivity(intent);
    }

    @Override
    public void onPreCallService() {
        this.showProgressDialog();
    }

    @Override
    public void onCallService() {

    }

    @Override
    public void onRequestCompleteListener(ArrayList<AIDBean> newsModelArrayList) {
        if (newsModelArrayList != null && newsModelArrayList.size() > 0) {
            Log.i("Check data", "" + newsModelArrayList.size());
            this.newsModelArrayList = newsModelArrayList;
            listAIDAdapter.notifyDataSetChanged();
        }
        this.dismissProgressDialog();
    }

    @Override
    public void onRequestFailed(String result) {
        this.dismissProgressDialog();
    }

    public void dismissProgressDialog() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    ProgressDialog alertDialog = null;
    public void showProgressDialog(){
        alertDialog = new ProgressDialog(getActivity());
        alertDialog.setMessage("Loading...");
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    class ListAIDAdapter extends BaseAdapter {

        public void setDataNotifyData(){
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return newsModelArrayList.size();
        }

        @Override
        public Object getItem(int position) {
            return newsModelArrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View currentView = getActivity().getLayoutInflater().inflate(R.layout.activity_aid_item, parent, false);
            AIDBean aidBean = (AIDBean) getItem(position);

            TextView txtTitle = (TextView) currentView.findViewById(R.id.txtTitle);
            txtTitle.setText(Html.fromHtml(aidBean.getTitle()));
            ImageView img = (ImageView) currentView.findViewById(R.id.imgCover);

            if(aidBean.getImage().equals("-")) {
                Log.d(TAG,"Image At "+(position+1)+" = null");
            }else {
                img.setMaxHeight(125);
                img.setMaxWidth(125);
                Picasso.with(getActivity()).load(aidBean.getImage()).into(img);
            }

            return currentView;
        }


    }

}