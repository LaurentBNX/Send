package com.dentasoft.testsend.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.dentasoft.testsend.SmsService;

import java.io.IOException;

public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            String content = intent.getStringExtra("content");
            Log.i("ALARM","Received intent, with content "+content);
            new SmsService().sendMessage(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
