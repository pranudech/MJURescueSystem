package itsci.mju.com.mjurescue.RequestAssistance;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import itsci.mju.com.mjurescue.R;
import itsci.mju.com.mjurescue.Util.JSONParser;

import static itsci.mju.com.mjurescue.UrlServlet.URL_SERVLET;


public class DialogStudentFragment extends DialogFragment {

    private String url_Notification = URL_SERVLET + "/RequestAssistanceMobileServlet";
    private JSONParser jParser = new JSONParser();
    private JSONObject json;
    private String status = "";
    private String studentID;
    public double longitude;
    public double latitude;

    public DialogStudentFragment(String status,Double latitude,Double longitude) {
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View dialog = inflater.inflate(R.layout.fragment_dialog_student, container,
                false);
        Button btnOK = (Button) dialog.findViewById(R.id.btnOKYou);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtStudent = (EditText) dialog.findViewById(R.id.edtStudentIDYou);
                studentID = txtStudent.getText().toString();
                if (!"".equals(studentID.trim())) {
                    Toast.makeText(getActivity(), "แจ้งสำเร็จ", Toast.LENGTH_SHORT).show();
                    new RequestAssistanceToServletAtID(status,studentID,latitude,longitude).execute();
                    dismiss();
                }
            }
        });
        return dialog;
    }

    private class RequestAssistanceToServletAtID extends AsyncTask<String, String, String> {

        private String status;
        private String studentID;
        private Double latitude;
        private Double longitude;

        public RequestAssistanceToServletAtID(String status,String studentID, Double latitude, Double longitude) {
            this.status = status;
            this.studentID = studentID;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... args) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("token", FirebaseInstanceId.getInstance().getToken()));
            params.add(new BasicNameValuePair("body",status));
            params.add(new BasicNameValuePair("latitude",Double.toString(latitude)));
            params.add(new BasicNameValuePair("longitude",Double.toString(longitude)));
            params.add(new BasicNameValuePair("studentID",studentID));
            json = jParser.makeHttpRequest(url_Notification, "GET", params);

            return null;
        }

    }
}
