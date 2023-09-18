package com.yusufekremunlu.easyway.utils;

import static android.content.ContentValues.TAG;
import android.util.Log;
import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class HuaweiMessageService extends HmsMessageService {
    @Override
    public void onMessageReceived(RemoteMessage message) {
        Log.i(TAG, "onMessageReceived is called");

        // Check whether the message is empty.
        if (message == null) {
            Log.e(TAG, "Received message entity is null!");
            return;
        }

        // Obtain the message content.
        Log.i(TAG, "get Data: " + message.getData()
                + "\n getFrom: " + message.getFrom()
                + "\n getTo: " + message.getTo()
                + "\n getMessageId: " + message.getMessageId()
                + "\n getSentTime: " + message.getSentTime()
                + "\n getDataMap: " + message.getDataOfMap()
                + "\n getMessageType: " + message.getMessageType()
                + "\n getTtl: " + message.getTtl()
                + "\n getToken: " + message.getToken());

        Boolean judgeWhetherIn10s = false;
        // If the message is not processed within 10 seconds, create a job to process it.
        if (judgeWhetherIn10s) {
            startWorkManagerJob(message);
        } else {
            // Process the message within 10 seconds.
            processWithin10s(message);
        }
    }

    private void startWorkManagerJob(RemoteMessage message) {
        Log.d(TAG, "Start new job processing.");
    }

    private void processWithin10s(RemoteMessage message) {
        Log.d(TAG, "Processing now.");
    }
}
