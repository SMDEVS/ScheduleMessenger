package com.example.schedulemessenger;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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

        String phoneNumber = whatsappServiceIntent.getStringExtra("PHONE");
        String messageText = whatsappServiceIntent.getStringExtra("TEXT");
        String timeString = whatsappServiceIntent.getStringExtra("TIME_STRING");

        Notification notification = new NotificationCompat.Builder(this, "Whatsapp Channel")
                .setContentTitle("Sending WhatsApp Intent")
                .setContentText(whatsappServiceIntent.getStringExtra("MESSAGE"))
                .setSmallIcon(R.drawable.ic_whatsapp)
                .build();

        int hashCode = (phoneNumber + messageText + timeString).hashCode();
        if (hashCode == 0)
            hashCode--;

        startForeground(hashCode, notification);

        try {
            Log.e("WhatsappForeground", "In try");
            Toast.makeText(this, "Hello from inside the ForegroundService!", Toast.LENGTH_SHORT).show();
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
        } catch (UnsupportedEncodingException | InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
        }

        stopSelf();

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
}
