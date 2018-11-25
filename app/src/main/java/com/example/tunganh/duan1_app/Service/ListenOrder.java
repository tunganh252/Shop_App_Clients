package com.example.tunganh.duan1_app.Service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.example.tunganh.duan1_app.General.General;
import com.example.tunganh.duan1_app.Model.Order_Details;
import com.example.tunganh.duan1_app.Order_Status.Order_Status;
import com.example.tunganh.duan1_app.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

@TargetApi(Build.VERSION_CODES.O)
public class ListenOrder extends Service implements ChildEventListener {

    FirebaseDatabase db;
    DatabaseReference order_details;


    public ListenOrder() {
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;

    }

    @Override
    public void onCreate() {
        super.onCreate();
        db = FirebaseDatabase.getInstance();
        order_details = db.getReference("Order_Details");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ////// Turn on/off notification

        order_details.addChildEventListener(this);

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Order_Details order_details = dataSnapshot.getValue(Order_Details.class);
        showNotification(dataSnapshot.getKey(), order_details);
    }

    /////////////////////////////////////
    private void showNotification(String key, Order_Details order_details) {

        Intent intent = new Intent(getBaseContext(), Order_Status.class);
        intent.putExtra("userName", order_details.getName());
        PendingIntent contentIntent = PendingIntent.getActivity(getBaseContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getBaseContext());

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setTicker("NTA DEV")
                .setContentInfo("Your order was update")
                .setContentText("Order #" + key + ": " + General.convertCodeToStatus(order_details.getStatus()))
                .setContentIntent(contentIntent)
                .setContentInfo("Info")
                .setSmallIcon(R.drawable.ic_notifications_black_24dp);

        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());
    }

    //////////////////////////////////////////
    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}

