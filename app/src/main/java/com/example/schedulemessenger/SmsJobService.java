package com.example.schedulemessenger;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class SmsJobService extends JobService {

    private static final String TAG = "In SmsJobService";
    private boolean isJobCancelled = false;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.v(TAG, "Job has been started.");
        backgroundSmsNotificationWork(params);
        return true;
    }

    private void backgroundSmsNotificationWork(final JobParameters params) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if(isJobCancelled)
                    return;
                sendSms();
                Log.v(TAG, "Job has been finished.");
                jobFinished(params, false); //false, as we don't want any rescheduling
            }
        }).start();
    }

    private void sendNotification() {
        createNotificationChannel();
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(),
                "notifyAboutSms")
                .setSmallIcon(R.drawable.ic_sms)
                .setContentTitle("Message sent!")
                .setContentText("Message has been sent to the intended recipient.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(200, notificationBuilder.build());
    }

    private void sendSms() {
        Intent intent = new Intent(this, SmsJobService.class);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, 0);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("9865155665", null, "Hello!",
                pendingIntent, null);
        sendNotification();
    }

    private void createNotificationChannel() {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "SmsNotificationChannel";
            String description = "Notification Channel for SMS";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel smsNotificationChannel = new NotificationChannel(
                    "notifyAboutSms", name, importance);
            smsNotificationChannel.setDescription(description);

            NotificationManager smsNotificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            smsNotificationManager.createNotificationChannel(smsNotificationChannel);

        }
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.v(TAG, "Job was cancelled before its completion.");
        isJobCancelled = true; //true, as job has indeed been cancelled
        return true;
    }

}
