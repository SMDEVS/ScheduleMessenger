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
        Toast.makeText(this, "Hello from inside the ForegroundService!", Toast.LENGTH_SHORT).show();

        Notification notification = new NotificationCompat.Builder(this, "Whatsapp Channel")
                .setContentTitle("Sending WhatsApp Intent")
                .setContentText(whatsappServiceIntent.getStringExtra("MESSAGE"))
                .setSmallIcon(R.drawable.ic_whatsapp)
                .build();

        startForeground(1, notification);

        try {
            Log.e("WhatsappForeground", "In try");
            Toast.makeText(this, "Hello from inside the ForegroundService!", Toast.LENGTH_SHORT).show();
            String url ="https://api.whatsapp.com/send?phone=911111111111&text=" + URLEncoder.encode("message", "UTF-8");
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setPackage("com.whatsapp");
            intent.setData(Uri.parse(url));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(intent.resolveActivity(this.getPackageManager())!=null){
                this.startActivity(intent);
                Thread.sleep(5000);
            }
        } catch (UnsupportedEncodingException | InterruptedException e) {
            e.printStackTrace();
        } catch(Exception e) {
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
