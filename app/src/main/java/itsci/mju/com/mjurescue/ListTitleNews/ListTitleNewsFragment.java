package itsci.mju.com.mjurescue.ListTitleNews;

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
import itsci.mju.com.mjurescue.ViewNews.ViewNewsActivity;

import static android.content.ContentValues.TAG;
import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;


public class ListTitleNewsFragment extends ListFragment implements CallNewsFeedListService {

    private String url_json = URL_SERVLET+"/ListTitleNewsMobileServlet";
    private NewsAdapter adapter;
    private ListView listViewNews;
    ArrayList<NewsFeedBean> newsModelArrayList = null;

    public ListTitleNewsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_title_news, container, false);

        new JSONCallNewsFeedListServiceAsyncTask((CallNewsFeedListService) this).execute(url_json);
        listViewNews = (ListView) view.findViewById(android.R.id.list);
        newsModelArrayList = new ArrayList<NewsFeedBean>();
        listViewNews.setAdapter(adapter = new NewsAdapter());

        return view ;

    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this.getActivity(), ViewNewsActivity.class);
        String selectedByImage = newsModelArrayList.get(position).getImage();
        intent.putExtra("selectedByImage",selectedByImage);
        startActivity(intent);
    }

    @Override
    public void onPreCallService() {

    }

    @Override
    public void onCallService() {

    }

    @Override
    public void onRequestCompleteListener(ArrayList<NewsFeedBean> newsModelArrayList) {
        if (newsModelArrayList != null && newsModelArrayList.size() > 0) {
            Log.i("Check data", "" + newsModelArrayList.size());
            this.newsModelArrayList = newsModelArrayList;
            adapter.notifyDataSetChanged();
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

    class NewsAdapter extends BaseAdapter {

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
            View currentView = getActivity().getLayoutInflater().inflate(R.layout.activity_newsfeed_item, parent, false);
            NewsFeedBean newsFeedBean = (NewsFeedBean) getItem(position);

            TextView txtTitle = (TextView) currentView.findViewById(R.id.txtTitle);
            TextView txtContent = (TextView) currentView.findViewById(R.id.txtContent);
            TextView txtDate = (TextView) currentView.findViewById(R.id.txtDate);

            txtTitle.setText(Html.fromHtml(newsFeedBean.getTitle()));
            txtContent.setText(Html.fromHtml(newsFeedBean.getContent()));
            txtDate.setText(Html.fromHtml(newsFeedBean.getDate()));

            ImageView img = (ImageView) currentView.findViewById(R.id.imgCover);

            String image = newsFeedBean.getImage().toString();
            String arr[] = image.split("##");

            if(newsFeedBean.getImage().equals("-")) {
                Log.d(TAG,"Image At "+(position+1)+" = null");
            }else {
                img.setMaxHeight(125);
                img.setMaxWidth(125);
                Picasso.with(getActivity()).load(arr[0]).into(img);
            }

            return currentView;
        }


    }
}
