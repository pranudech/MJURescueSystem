package itsci.mju.com.mjurescue.ListRequests;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ListFragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import itsci.mju.com.mjurescue.R;
import itsci.mju.com.mjurescue.ViewRequestData.ViewRequestDataActivity;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;


public class ListRequestsFragment extends ListFragment implements CallNotificationListService {

    private String url_ListRequestsServlet = URL_SERVLET+"/ListRequestsMobileServlet";
    private NewsAdapter adapter;
    private ListView listViewNews;
    ArrayList<NotificationBean> newsModelArrayList = null;

    public ListRequestsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_requests, container, false);

        new JSONCallNotificationListServiceAsyncTask((CallNotificationListService) this).execute(url_ListRequestsServlet);
        listViewNews = (ListView) view.findViewById(android.R.id.list);
        newsModelArrayList = new ArrayList<NotificationBean>();
        listViewNews.setAdapter(adapter = new NewsAdapter());

        Button btnRefresh = (Button) view.findViewById(R.id.btnRefresh);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "List Update !", Toast.LENGTH_SHORT).show();
                new Handler().post(new Runnable() {

                    @Override
                    public void run()
                    {
                        // Refresh Activity if list update
                        Intent intent = getActivity().getIntent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().finish();

                        getActivity().overridePendingTransition(0, 0);
                        startActivity(intent);
                    }
                });
            }
        });

        return view ;

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(this.getActivity(), ViewRequestDataActivity.class);
        String selectedId = newsModelArrayList.get(position).getNotificationID();
        String latitude = Double.toString(newsModelArrayList.get(position).getLatitude());
        String longitude = Double.toString(newsModelArrayList.get(position).getLongitude());
        intent.putExtra("selectedId",selectedId);
        intent.putExtra("latitude",latitude);
        intent.putExtra("longitude",longitude);
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
    public void onRequestCompleteListener(ArrayList<NotificationBean> newsModelArrayList) {
        if (newsModelArrayList != null && newsModelArrayList.size() > 0) {
            Log.i("##### ListRequest Check", "" + newsModelArrayList.size());
            this.newsModelArrayList = newsModelArrayList;
            adapter.notifyDataSetChanged();
            adapter.setDataNotifyData();
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
            View currentView = getActivity().getLayoutInflater().inflate(R.layout.activity_list_request_item, parent, false);
            NotificationBean notificationBean = (NotificationBean) getItem(position);

            TextView txtStatusWorking = (TextView) currentView.findViewById(R.id.txtStatusWorking);
            TextView txtRequest = (TextView) currentView.findViewById(R.id.txtRequest);
            TextView txtRequestDate = (TextView) currentView.findViewById(R.id.txtRequestDate);
            ImageView imgRequest = (ImageView) currentView.findViewById(R.id.imgRequest);

            txtStatusWorking.setText(Html.fromHtml("อุบัติเหตุที่ : "+notificationBean.getNotificationID()+" "+notificationBean.getFormStaffBean().getStatusCase()));

            if(notificationBean.getFormStaffBean().getStatusCase().equals("Yellow")) {

                txtStatusWorking.setTextColor(Color.parseColor("#FFCC33"));
                imgRequest.setImageResource(R.drawable.request_yellow);

            }else if(notificationBean.getFormStaffBean().getStatusCase().equals("Green")){

                txtStatusWorking.setTextColor(Color.parseColor("#009900"));
                imgRequest.setImageResource(R.drawable.request_green);

            }else if(notificationBean.getFormStaffBean().getStatusCase().equals("Red")){

                txtStatusWorking.setTextColor(Color.parseColor("#CC0000"));
                imgRequest.setImageResource(R.drawable.request_red);
            }

            imgRequest.setMaxHeight(90);
            imgRequest.setMaxWidth(90);

            if(notificationBean.getFormStaffBean().getStaffID() != null) {
                txtRequest.setText("- รับแจ้งเหตุแล้ว -");

            }else{
                txtRequest.setText("- รอการช่วยเหลือ -");
            }

            txtRequestDate.setText(Html.fromHtml("เวลา : "+notificationBean.getDate()));

            return currentView;
        }


    }

}