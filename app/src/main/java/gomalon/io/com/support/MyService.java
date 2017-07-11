package gomalon.io.com.support;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import gson.GsonRequestJson;
import query.Queries;
import query.Query;
import query.SuccessModel;
import util.Constants;
import util.NetworkConstants;


/**
 * Created by nagesh on 8/7/17.
 */

public class MyService extends Service {

    AlertDialog alert;
    EditText userName;
    HashMap<String, String> headers;
    JsonObject jsonObject;
    String tag_json_obj = "json_obj_req";
    RadioGroup rg;
    LinearLayout linearLayout;
    private static Date callEndTime;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        rg = new RadioGroup(this);
        AlertDialogView();
        Toast.makeText(getApplicationContext(),"good",Toast.LENGTH_SHORT).show();

//        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplication(), R.style.dialog));
//
//        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
//        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_signin,null);
//
//        builder.setView(dialogView);
//
//        alert = builder.create();
//        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//        alert.show();

        stopSelf();
        return START_STICKY;

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    public void AlertDialogView(){

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(getApplication(), R.style.dialog));

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View dialogView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_signin,null);
        linearLayout = (LinearLayout) dialogView.findViewById(R.id.addlayout);
        Button submit = (Button) dialogView.findViewById(R.id.chooseBtn);
        userName = (EditText) dialogView.findViewById(R.id.editText);
//        userName.setText(Constants.userName);
        fetchData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
//                if(userName.getText().toString().length() > 0){
//                    sendDatatoServer(rg.getCheckedRadioButtonId(),userName.getText().toString());
//                }else{
//                    userName.setError("PLease enter user name");
//                }
//                alert.dismiss();
//                Toast.makeText(getApplicationContext(), "Button Clicked", Toast.LENGTH_LONG).show();


            }
        });

        builder.setView(dialogView);

        alert = builder.create();
        alert.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alert.show();

    }

    private void fetchData(){

        headers = new HashMap<String, String>();
        headers.put("X-CLIENT-ID", "MAPI");
        headers.put("X-CLIENT-USER", "gomalon-apis");
        headers.put("X-CLIENT-KEY", "Z29tYWxvbi1hcGlz");
        headers.put("Content-Type", "application/json");

        jsonObject = new JsonObject();

        jsonObject.addProperty("user_id","3a4f8519d3d8aca632119b705cc5c0bc");

        GsonRequestJson gsonRequestJson = new GsonRequestJson(Request.Method.POST,
                NetworkConstants.User_Quaries_URL, Query.class, headers, jsonObject, new Response.Listener<Query>() {
            @Override
            public void onResponse(Query response) {

                Log.d("Sucess ","");
                createRadioButton(response.getQueries());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("your Error"+error.getMessage());

                Log.d("Error ", "" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(gsonRequestJson,tag_json_obj);

    }

    private void createRadioButton(Queries data) {
        final RadioButton[] rb = new RadioButton[data.getList().size()];
        rg.setOrientation(RadioGroup.VERTICAL);//or RadioGroup.VERTICAL
        for(int i=0; i<data.getList().size(); i++){
            rb[i]  = new RadioButton(this);
            rb[i].setText(" " +data.getList().get(i).getQueryDescription());
            rb[i].setId(Integer.parseInt(data.getList().get(i).getQueryId()));
            rb[i].setPadding(16,16,16,16);
            rg.addView(rb[i]);

            if(i==0){
                rb[i].setChecked(true);
            }
        }
        linearLayout.addView(rg);//you add the whole RadioGroup to the layout

    }

    private void sendDatatoServer(int selectedId,String userName){

//        final ProgressDialog dialog = new ProgressDialog(getApplicationContext());
//        dialog.setCancelable(true);
//        dialog.setMessage("Please wait");
//        dialog.show();



        headers = new HashMap<>();
        headers.put("X-CLIENT-ID", "MAPI");
        headers.put("X-CLIENT-USER", "gomalon-apis");
        headers.put("X-CLIENT-KEY", "Z29tYWxvbi1hcGlz");
        headers.put("Content-Type", "application/json");
        callEndTime = new Date();

        jsonObject = new JsonObject();

        jsonObject.addProperty("user_id","ebbaa021a82afd13c662803e9e163995");
        jsonObject.addProperty("caller_id",Constants.calledID);
        jsonObject.addProperty("phone",Constants.mobileNumber);
        jsonObject.addProperty("action","completed");
        jsonObject.addProperty("call_time",toNOrmalDate(String.valueOf(callEndTime.getTime())));
        jsonObject.addProperty("caller_name",userName);
        jsonObject.addProperty("query_id",selectedId);


        Log.d("JSON Object",""+ jsonObject);

        GsonRequestJson gsonRequestJson = new GsonRequestJson(Request.Method.POST,
                NetworkConstants.Update_Call_Reason_URL, SuccessModel.class, headers, jsonObject, new Response.Listener<SuccessModel>() {
            @Override
            public void onResponse(SuccessModel response) {
                //   calledID = "";
//                    System.out.println("Success"+ response.getMessage());
//                    System.out.println("userName"+ response.getUsername());
//                    System.out.println("code"+ response.getCode());


//                MainActivity.this.finish();
//                android.os.Process.killProcess(android.os.Process.myPid());
//                System.exit(0);



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.print("your Error"+error.getMessage());

                Log.d("Error ", "" + error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(gsonRequestJson, tag_json_obj);

    }

    private String toNOrmalDate(String time){

        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        long milliSeconds= Long.parseLong(time);
        System.out.println(milliSeconds);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        System.out.println(formatter.format(calendar.getTime()));


        return formatter.format(calendar.getTime());
    }

}
