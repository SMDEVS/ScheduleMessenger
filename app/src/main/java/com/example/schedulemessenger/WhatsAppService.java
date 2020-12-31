package com.example.schedulemessenger;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

public class WhatsAppService extends AccessibilityService {
    public WhatsAppService() {
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Log.d("HHH","onAccessibilityservice");
        if(getRootInActiveWindow() == null)
        {     Log.d("HHH","onAccessibilityservice1");
            return;
        }

        AccessibilityNodeInfoCompat rootNodeInfo = AccessibilityNodeInfoCompat.wrap(getRootInActiveWindow());

        List<AccessibilityNodeInfoCompat> MessageNode = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/entry");
        if(MessageNode == null)
        {     Log.d("HHH","onAccessibilityservice2");
            return;
        }

        AccessibilityNodeInfoCompat suffix = MessageNode.get(0);
        if(suffix == null)
        {     Log.d("HHH","onAccessibilityservice3");
            return;
        }


        List<AccessibilityNodeInfoCompat> SendNode = rootNodeInfo.findAccessibilityNodeInfosByViewId("com.whatsapp:id/send");
        if(SendNode == null)
        {     Log.d("HHH","onAccessibilityservice4");
            return;
        }

        AccessibilityNodeInfoCompat send = SendNode.get(0);
        if(!send.isVisibleToUser())
        {     Log.d("HHH","onAccessibilityservice5");
            return;
        }
        send.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

        try {
            Thread.sleep(2000);
            performGlobalAction(GLOBAL_ACTION_BACK);
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        performGlobalAction(GLOBAL_ACTION_BACK);
    }

    @Override
    public void onInterrupt() {

    }
}