package com.example.DELL.new_blood;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by DELL on 2/24/2018.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        String str = remoteMessage.getData().get("status");
        String event = remoteMessage.getData().get("event");
        if(str != null && !str.isEmpty()){

            showNotification2(remoteMessage.getData().get("message"), remoteMessage.getData().get("name"),remoteMessage.getData().get("address"),remoteMessage.getData().get("blood"),remoteMessage.getData().get("phone"),remoteMessage.getData().get("image"),remoteMessage.getData().get("status"),remoteMessage.getData().get("req_id"),remoteMessage.getData().get("user_id"));

        }else if(event != null && !event.isEmpty()){
            showNotification3(remoteMessage.getData().get("message"),remoteMessage.getData().get("name"),remoteMessage.getData().get("address"),remoteMessage.getData().get("msg"));
        }
        else{
            showNotification(remoteMessage.getData().get("message"), remoteMessage.getData().get("name"),remoteMessage.getData().get("address"),remoteMessage.getData().get("blood"),remoteMessage.getData().get("message"),remoteMessage.getData().get("token"),remoteMessage.getData().get("req_id"));

        }

    }

    private void showNotification(String message, String name, String address, String blood, String msg,String token,String req_id) {

        Intent i = new Intent(this,notification_activity.class);
        i.putExtra("notif", message);
        i.putExtra("name", name);
        i.putExtra("request_id", req_id);
        String[] latlng = address.split(":");

        String latt = latlng[0].toString();
        String lngg = latlng[1].toString();

        i.putExtra("lat", latt);
        i.putExtra("lng", lngg);

        i.putExtra("blood", blood);
        i.putExtra("message", message);
        i.putExtra("token", token);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Emergency")
                .setContentText(message)
                .setSmallIcon(R.drawable.b4)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());
    }

    void showNotification2(String message, String name, String address, String blood, String phone,String image,String status,String req_id,String user_id){
        Intent i = new Intent(this,Active_donor.class);
        i.putExtra("notif", message);
        i.putExtra("req_id", req_id);
        i.putExtra("name", name);
        i.putExtra("phone",  phone);
        i.putExtra("image",  image);
        i.putExtra("status",  status);
        i.putExtra("user_id",  user_id);
        String[] latlng = address.split(":");

        String latt = latlng[0].toString();
        String lngg = latlng[1].toString();

        i.putExtra("lat", latt);
        i.putExtra("lng", lngg);

        i.putExtra("blood", blood);
        i.putExtra("message", message);

        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Donor Alert")
                .setContentText(message)
                .setSmallIcon(R.drawable.b4)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());

    }

    void showNotification3(String message, String name, String address, String msg){

        Intent i = new Intent(this, event_activity.class);

        i.putExtra("notif", message);
        i.putExtra("name", name);
        i.putExtra("address", address);
        i.putExtra("msg", msg);



        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,i,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setAutoCancel(true)
                .setContentTitle("Event Alert")
                .setContentText(message)
                .setSmallIcon(R.drawable.b4)
                .setContentIntent(pendingIntent);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        manager.notify(0,builder.build());


    }


}
