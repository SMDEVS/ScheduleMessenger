package com.example.schedulemessenger;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.example.schedulemessenger.View.MainActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class WhatsappForegroundService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent whatsappServiceIntent, int flags, int startId) {

        Log.e("WhatsappForeground", "In onStartCommand");

        int type = whatsappServiceIntent.getIntExtra("TYPE", -1);
        String phoneNumber = whatsappServiceIntent.getStringExtra("PHONE");
        String messageText = whatsappServiceIntent.getStringExtra("TEXT");
        String timeString = whatsappServiceIntent.getStringExtra("TIME_STRING");

        createNotificationChannel(this);

        Notification notification = new NotificationCompat.Builder(this, "notifyAboutSms")
                .setContentTitle("Sending WhatsApp Intent")
                .setContentText(whatsappServiceIntent.getStringExtra("TEXT"))
                .setSmallIcon(R.drawable.ic_whatsapp)
                .build();

        int hashCode = (phoneNumber + messageText + timeString).hashCode();
        if (hashCode == 0)
            hashCode--;

        startForeground(hashCode, notification);

        try {
            Log.e("WhatsappForeground", "In try");
            Toast.makeText(this, "Hello from inside the ForegroundService!", Toast.LENGTH_SHORT).show();
            if (type == 2) {
                String url = "https://api.whatsapp.com/send?phone=91" + phoneNumber +
                        "&text=" + URLEncoder.encode(messageText, "UTF-8");
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setPackage("com.whatsapp");
                intent.setData(Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (intent.resolveActivity(this.getPackageManager()) != null) {
                    this.startActivity(intent);
                    Thread.sleep(5000);
                }
            } else if (type == 3) {

                SharedPreferences sharedPref = getApplication().getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE);
                String emailId = sharedPref.getString("EMAIL_ID", "");
                String password = sharedPref.getString("PASSWORD", "");

                Toast.makeText(this, "In the correct email block!", Toast.LENGTH_SHORT).show();
                String subject = whatsappServiceIntent.getStringExtra("SUBJECT");
                BackgroundMail.newBuilder(getApplication())
                        .withUsername(emailId) // Hard code your gmail id here
                        .withPassword(password) // Hard code the password for above gmail id here
                        .withMailTo(phoneNumber)
                        .withType(BackgroundMail.TYPE_PLAIN)
                        .withSubject(subject)
                        .withBody(messageText)
                        .withOnSuccessCallback(new BackgroundMail.OnSendingCallback() {
                            @Override
                            public void onSuccess() {
                                Toast.makeText(getApplicationContext(), "SUCCESS!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFail(Exception e) {
                                Toast.makeText(getApplicationContext(), "FAIL", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .send();

            }

        } catch (UnsupportedEncodingException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        Toast.makeText(this, "About to stop the service now", Toast.LENGTH_SHORT).show();
        stopSelf();

        String currentString = phoneNumber + messageText + timeString;
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(),
                currentString.hashCode(), intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.cancel(pendingIntent);

        return START_NOT_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.i("onTaskRemoved", "Indicates when task removed");
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
