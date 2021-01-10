package com.example.schedulemessenger;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MyBroadcastReceiver extends BroadcastReceiver {

    private int iconId = -1;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Hello, from my Broadcast Receiver!", Toast.LENGTH_SHORT).show();

        int id = intent.getIntExtra("ID", -1);
        String phoneNumber = intent.getStringExtra("PHONE");
        String messageText = intent.getStringExtra("TEXT");
        int messageType = intent.getIntExtra("TYPE", -1);

        if (messageType == 1) {
            sendSMS(phoneNumber, messageText);
            iconId = R.drawable.ic_sms;
        } else if (messageType == 2) {
            String timeString = intent.getStringExtra("TIME_STRING");
            SendWA(context, phoneNumber, messageText, timeString);
            iconId = R.drawable.ic_whatsapp;
        } else if (messageType == 3) {
            String timeString = intent.getStringExtra("TIME_STRING");
            String subject = intent.getStringExtra("SUBJECT");
            sendEmail(context, phoneNumber, subject, messageText, timeString);
            iconId = R.drawable.ic_email;
        }

        createNotificationChannel(context);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context,
                "notifyAboutSms")
                .setSmallIcon(iconId)
                .setContentTitle("Message sent to " + phoneNumber + "!")
                .setContentText("Message has been sent to the intended recipient.")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify(id, notificationBuilder.build());

    }

    private void sendEmail(final Context context, String emailId, String subject,
                           String emailBody, String timeString) {

        Toast.makeText(context, "HELLO from Broadcast", Toast.LENGTH_SHORT).show();


        /**

         Intent intent = new Intent(Intent.ACTION_SEND);
         intent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailId});
         intent.putExtra(Intent.EXTRA_SUBJECT, subject);
         intent.putExtra(Intent.EXTRA_TEXT, emailBody);
         intent.setType("message/rfc822");
         Intent chooserIntent = Intent.createChooser(intent, "Choose app for email: ");
         chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
         if (intent.resolveActivity(context.getPackageManager()) != null) {
         context.getApplicationContext().startActivity(chooserIntent);
         try {
         Thread.sleep(5000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         }
         */

        Intent emailIntent = new Intent(context, WhatsappForegroundService.class);
        emailIntent.putExtra("TYPE", 3);
        emailIntent.putExtra("PHONE", emailId);
        emailIntent.putExtra("TEXT", emailBody);
        emailIntent.putExtra("TIME_STRING", timeString);
        emailIntent.putExtra("SUBJECT", subject);
        ContextCompat.startForegroundService(context, emailIntent);

    }

    private void SendIG(Context context) {
        Intent instaintent = context.getApplicationContext().getPackageManager()
                .getLaunchIntentForPackage("com.instagram.android");
        instaintent.setComponent(new ComponentName("com.instagram.android", "com.instagram.android.activity.UrlHandlerActivity"));
        instaintent.setData(Uri.parse("https://www.instagram.com/_u/arka_pal_99"));
        instaintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(instaintent);
    }

    private void SendWA(Context context, String phoneNumber, String messageText, String timeString) {

        Intent whatsappIntent = new Intent(context, WhatsappForegroundService.class);
        whatsappIntent.putExtra("TYPE", 2);
        whatsappIntent.putExtra("PHONE", phoneNumber);
        whatsappIntent.putExtra("TEXT", messageText);
        whatsappIntent.putExtra("TIME_STRING", timeString);
        ContextCompat.startForegroundService(context, whatsappIntent);

    }

    private void sendSMS(String phoneNumber, String messageText) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, messageText,
                null, null);
    }

    private void createNotificationChannel(Context context) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
