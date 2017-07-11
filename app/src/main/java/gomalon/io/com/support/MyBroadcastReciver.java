package gomalon.io.com.support;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
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
import query.SuccessModel;
import util.Constants;
import util.NetworkConstants;

/**
 * Created by nagesh on 27/5/17.
 */

public class MyBroadcastReciver extends BroadcastReceiver {

    private static Date callStartTime;
    private static String savedNumber;
    Context context1;

    HashMap<String, String> headers;
    JsonObject jsonObject;
    String tag_json_obj = "json_obj_req";


    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("eno ondu",""+ Constants.recievedTheCall);
        context1 = context;
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL")) {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");

        }
        else{
            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);

            Log.d("State ",""+ stateStr);

            Log.d("Calling state change ",""+ stateStr);
            onCallStateChanged(context, stateStr, number);
        }

    }



    public void onCallStateChanged(Context context, String state, String number) {


        switch (state) {
            case "RINGING":

                Constants.recievedTheCall = "RINGING";
                callStartTime = new Date();
                savedNumber = number;

                Toast.makeText(context, "Incoming Call Ringing" , Toast.LENGTH_SHORT).show();
                break;

            case "OFFHOOK":

                if(savedNumber.length() > 10){
                    savedNumber = savedNumber.substring(3);
                }

                Constants.mobileNumber = savedNumber;

                if(Constants.recievedTheCall.equalsIgnoreCase("RINGING")){

                    callStartTime = new Date();

                    try {

                        jsonObject = new JsonObject();
                        jsonObject.addProperty("user_id","ebbaa021a82afd13c662803e9e163995");
                        jsonObject.addProperty("caller_id","");
                        jsonObject.addProperty("phone",savedNumber);
                        jsonObject.addProperty("action","received");
                        jsonObject.addProperty("call_time",toNOrmalDate(String.valueOf(callStartTime.getTime())));
                        jsonObject.addProperty("caller_name","");
                        jsonObject.addProperty("query_id","");

                        sendDatatoServer(jsonObject,"recived",context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context.getApplicationContext(),"Recived", Toast.LENGTH_SHORT).show();

                }else if (Constants.recievedTheCall.equalsIgnoreCase("IDLE")){

                    try {


                        jsonObject = new JsonObject();
                        jsonObject.addProperty("user_id","ebbaa021a82afd13c662803e9e163995");
                        jsonObject.addProperty("caller_id","");
                        jsonObject.addProperty("phone",savedNumber);
                        jsonObject.addProperty("action","dialed");
                        jsonObject.addProperty("call_time",toNOrmalDate(String.valueOf(callStartTime.getTime())));
                        jsonObject.addProperty("caller_name","");
                        jsonObject.addProperty("query_id","");

                        sendDatatoServer(jsonObject,"dialed",context);


                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context.getApplicationContext(),"Dialed", Toast.LENGTH_SHORT).show();

                }else {
                    //do nothing
                }

                Constants.recievedTheCall = "OFFHOOK";


                break;
            case "IDLE":

                if(savedNumber.length() > 10){
                    savedNumber = savedNumber.substring(3);
                }

                Constants.mobileNumber = savedNumber;

                if(Constants.recievedTheCall.equalsIgnoreCase("RINGING")){
                    callStartTime = new Date();

                    try {
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("user_id","ebbaa021a82afd13c662803e9e163995");
                        jsonObject.addProperty("caller_id","");
                        jsonObject.addProperty("phone",Constants.mobileNumber);
                        jsonObject.addProperty("action","missed");
                        jsonObject.addProperty("call_time",toNOrmalDate(String.valueOf(callStartTime.getTime())));
                        jsonObject.addProperty("caller_name","");
                        jsonObject.addProperty("query_id","");

                        sendDatatoServer(jsonObject,"missed",context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Toast.makeText(context.getApplicationContext(),"missed", Toast.LENGTH_SHORT).show();

                }else if (Constants.recievedTheCall.equalsIgnoreCase("IDLE")){
                    try {
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("user_id","ebbaa021a82afd13c662803e9e163995");
                        jsonObject.addProperty("caller_id","");
                        jsonObject.addProperty("phone",Constants.mobileNumber);
                        jsonObject.addProperty("action","Not received");
                        jsonObject.addProperty("call_time",toNOrmalDate(String.valueOf(callStartTime.getTime())));
                        jsonObject.addProperty("caller_name","");
                        jsonObject.addProperty("query_id","");

                        sendDatatoServer(jsonObject,"Not received",context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                else {

                    try {
                        jsonObject = new JsonObject();
                        jsonObject.addProperty("user_id","ebbaa021a82afd13c662803e9e163995");
                        jsonObject.addProperty("caller_id","");
                        jsonObject.addProperty("phone",Constants.mobileNumber);
                        jsonObject.addProperty("action","received");
                        jsonObject.addProperty("call_time",toNOrmalDate(String.valueOf(callStartTime.getTime())));
                        jsonObject.addProperty("caller_name","");
                        jsonObject.addProperty("query_id","");

                        sendDatatoServer(jsonObject,"received",context);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                  //  Toast.makeText(context.getApplicationContext(),"disconnected",Toast.LENGTH_SHORT).show();

                }

                Constants.recievedTheCall = "IDLE";

                break;
        }
    }


    private void sendDatatoServer(JsonObject jsonObject, final String callType, final Context context){

        headers = new HashMap<>();
        headers.put("X-CLIENT-ID", "MAPI");
        headers.put("X-CLIENT-USER", "gomalon-apis");
        headers.put("X-CLIENT-KEY", "Z29tYWxvbi1hcGlz");
        headers.put("Content-Type", "application/json");

       System.out.print(jsonObject);

        GsonRequestJson gsonRequestJson = new GsonRequestJson(Request.Method.POST,
                NetworkConstants.Update_Call_Reason_URL, SuccessModel.class, headers, jsonObject, new Response.Listener<SuccessModel>() {
            @Override
            public void onResponse(SuccessModel response) {

                System.out.println("Success "+ response.getMessage());

                Constants.calledID = response.getCaller_id();
                Constants.userName = response.getUsername();

                System.out.println("the user name  :"+response.getUsername());
                System.out.print("the caller_id"+response.getCaller_id());

                if(callType.equalsIgnoreCase("received")){

                    Intent i = new Intent();
                    i.setClassName("gomalon.io.com.support", "gomalon.io.com.support.MyService");
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startService(i);

                }


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

