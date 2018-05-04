package com.example.android.redcross1;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.ArrayList;
import java.util.List;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * Created by ujan on 13/12/16.
 */

public class GeofenceTransitionsIntentService extends IntentService {

    static String notification;
    String ujan="Ujan";

    public GeofenceTransitionsIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.e(TAG, Integer.toString(geofencingEvent.getErrorCode()));
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {

            List triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            // Get the transition details as a String
            String geofenceTransitionDetails = getGeofenceTransitionDetails(
                    geofenceTransition,
                    triggeringGeofences
            );

            sendNotification(geofenceTransitionDetails);

            Log.i(TAG, geofenceTransitionDetails);
        } else {
            // Log the error.
            Log.e(TAG, "Transition type invalid");
        }

    }

    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        ArrayList triggeringGeofencesIdsList = new ArrayList();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ",  triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                notification="entered";
                return "Geofence Entered";
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                notification="exited";
                return "Geofence Exited";
            default:
                return "Unknown transition";
        }
    }

    private void sendNotification(String notificationDetails){

       // send("1", "lat", "long");
        Log.e(ujan, "geofence");
        Intent notificationIntent = new Intent(getApplicationContext(), MapsActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntent(notificationIntent);
        PendingIntent notificationPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

        builder.setSmallIcon(R.drawable.mr_ic_play_dark)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.mr_ic_play_dark))
                .setColor(Color.RED)
                .setContentTitle(notificationDetails)
                .setContentText(notificationDetails)
                .setContentIntent(notificationPendingIntent);

        builder.setAutoCancel(true);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, builder.build());

        Intent intent=new Intent(this, SendDataIntentService.class);
        startService(intent);
       /** while(GeofenceTransitionsIntentService.notification!="exited"){
            Toast.makeText(GeofenceTransitionsIntentService.this, "Geofence Hello", Toast.LENGTH_SHORT).show();
        }
**/
    }

}
