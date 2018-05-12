package com.example.android.redcross1;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.vision.text.Text;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.ContentValues.TAG;

import static android.Manifest.permission.SEND_SMS;

/**
 * Created by Ruchira on 14/04/18.
 */

public class SendDataIntentService extends IntentService {



    String TAG1="R";

    LatLng source;

    String place="R";

    public SendDataIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if(MapsActivity.posn==null){
            MapsActivity.posn=new LatLng(22.502020, 88.357732);
        }
        source=MapsActivity.s;
        //parseToString(source);
      // GeofenceTransitionsIntentService.notification="exited"; //for checking when geofence exiting

         /**   String phone="9051130126";
            String message = "Ambulance nearing";
             sendSMS(phone,message);**/
       // SmsManager.getDefault().sendTextMessage(phone, null, message, null,null);


       Toast.makeText(this,"Hello",Toast.LENGTH_LONG).show();
        while(GeofenceTransitionsIntentService.notification!="exited") {
            if (place != MapsActivity.name) {
                place = MapsActivity.name;
                String src = parselatlong(source);
                String dest = parselatlong(MapsActivity.posn);
                String curr = parselatlong(MapsActivity.s);
                String traffic_id = "1";
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(getApplicationContext(), "Data sent", Toast.LENGTH_SHORT).show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(SendDataIntentService.this);
                                builder.setMessage("Data not sent")
                                        .setNegativeButton("Retry", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                SendRequest sendRequest = new SendRequest(traffic_id, src, dest, curr, responseListener);
                RequestQueue queue = Volley.newRequestQueue(SendDataIntentService.this);
                queue.add(sendRequest);
            }

        }
        while(GeofenceTransitionsIntentService.notification=="exited"){
            if (place != MapsActivity.name) {
                place = MapsActivity.name;

                String traffic_id = "1";
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                               // Toast.makeText(getApplicationContext(), "Exit request sent", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                ExitRequest exitRequest = new ExitRequest(traffic_id,responseListener);
                RequestQueue queue = Volley.newRequestQueue(SendDataIntentService.this);
                queue.add(exitRequest);
            }


        }

        /**  com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
          String url = "http://illuminati.esy.es/traffic.php?traffic_id=" + 1 +
                  "&source=" + parseToString(source) + "&destination=" + parseToString(MapsActivity.posn) +
                  "&current=" + parseToString(MapsActivity.s);   //source, destination, current location


          Log.e(TAG1, parseToString(source) + " " + parseToString(MapsActivity.posn) + " " + parseToString(MapsActivity.s));
          StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                  new Response.Listener<String>() {
                      @Override
                      public void onResponse(String response) {
                          System.out.println(response);
                      }
                  }, new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                  System.out.println(error.getMessage());
              }
          });
          queue.add(stringRequest);
          try {
              Thread.sleep(1000); //increase duration
          } catch (InterruptedException e) {
              e.printStackTrace();
          }**/


    }



        /*com.android.volley.RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://illuminati.esy.es/traffic.php?traffic_id=" + 1 +
                "&source=" + "a" + "&destination=" + "a" +
                "&current=" + "exited";   //source, destination, current location

        //Log.e(TAG1, parseToString(source) + " " + parseToString(MapsActivity.posn) + " " + parseToString(MapsActivity.s));
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.getMessage());
            }
        });
        queue.add(stringRequest);*/



    private void sendSMS(String phoneNumber, String message)
    {

        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, SendDataIntentService.class), 0);
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, pi, null);

    }
    String parselatlong(LatLng l){

        Double l1=l.latitude;
        Double l2=l.longitude;
        String coordl1 = l1.toString();
        String coordl2 = l2.toString();
        l1 = Double.parseDouble(coordl1);
        l2 = Double.parseDouble(coordl2);
        return l1+","+l2;
    }
    String parseToString(LatLng l){

        String a[]=l.toString().split(" ");
        String b=a[1].replace("(", "");
        String c[]=b.split(",");
        double dbl1 = Double.parseDouble(c[0]);
        String d=c[1].replace(")", "");
        double dbl2 = Double.parseDouble(d);
        return dbl1+"a"+dbl2;

    }

}
