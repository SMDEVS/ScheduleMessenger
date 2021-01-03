package com.example.schedulemessenger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.schedulemessenger.Model.Message;
import com.example.schedulemessenger.Repository.MessageRepository;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Hello, from my Broadcast Receiver!", Toast.LENGTH_SHORT).show();
        //SendWA(context);

        int id = intent.getIntExtra("ID", -1);
        String phoneNumber = intent.getStringExtra("PHONE");
        String messageText = intent.getStringExtra("TEXT");

        sendSMS(phoneNumber, messageText);

        createNotificationChannel(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                "notifyAboutSms")
                .setSmallIcon(R.drawable.ic_sms)
                .setContentTitle("Message sent to " + phoneNumber + "!")
                .setContentText("Message has been sent to the intended recipient.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notificationBuilder.build());

    }

    private void SendWA(Context context)
    {
        try {
            String url ="https://api.whatsapp.com/send?phone=919698039438&text=" + URLEncoder.encode("message", "UTF-8");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(intent.resolveActivity(context.getPackageManager())!=null){
                context.startActivity(intent);
                Thread.sleep(5000);
            }
        } catch (UnsupportedEncodingException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void sendSMS(String phoneNumber, String messageText)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, messageText,
                null, null);
    }

    private void createNotificationChannel(Context context) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SmsNotificationChannel";
            String description = "Notification Channel for SMS";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel smsNotificationChannel = new NotificationChannel(
                    "notifyAboutSms", name, importance);
            smsNotificationChannel.setDescription(description);

            NotificationManager smsNotificationManager = context.getSystemService(NotificationManager.class);
            smsNotificationManager.createNotificationChannel(smsNotificationChannel);

        }
    }
}
