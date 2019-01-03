package com.amazonaws.demo.androidpubsubwebsocket;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.logging.LogRecord;

public class MyHandler extends Handler {
    private static final String TAG = "MyHandler";
    static final int ACTION_SUBSCRIBE = 0;
    public static final int ACTION_PUBLISH = 1;
    static final int ACTION_GET_CONNECTION_STATUS = 2;
    static final int ACTION_CONNECT =3;
    static final int ACTION_DISCONNECT =4;

    public MyHandler(MainActivity activity) {
        this.activityWeakReference = new WeakReference<>(activity) ;
    }

    WeakReference<MainActivity> activityWeakReference;
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (activityWeakReference.get() == null){

            return;
        }
        MainActivity activity = activityWeakReference.get();
        AwsManager awsManager = activity.awsManager;
        if (awsManager == null) {
            activity.notifyUser("An error accured, Panic!!!");
            return;
        }
        Bundle b = msg.getData();
        switch (msg.what){
            case ACTION_SUBSCRIBE:
                String topic = b.getString("topic");
                awsManager.subscribe(topic);
                break;
            case ACTION_PUBLISH:
                String mTopic = b.getString("topic");
                String message = b.getString("msg");
                Log.i(TAG, "topic: " + mTopic + ", msg: " + message);
                awsManager.publish(mTopic, message);
                break;
            case ACTION_GET_CONNECTION_STATUS:
                activity.updateFragmentStatus();
                break;
            case ACTION_CONNECT:
                awsManager.connect();
                break;
            case ACTION_DISCONNECT:
                awsManager.disconnect();
                break;

        }
    }
}
