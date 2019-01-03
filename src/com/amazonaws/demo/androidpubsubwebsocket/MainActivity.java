package com.amazonaws.demo.androidpubsubwebsocket;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.demo.androidpubsubwebsocket.Model.Post;
import com.amazonaws.demo.androidpubsubwebsocket.Model.CommandTranslator;
import com.amazonaws.demo.androidpubsubwebsocket.Model.Device;
import com.amazonaws.demo.androidpubsubwebsocket.Model.MessageEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements IAwsCallback, IFragmentCommunicator {
    static final String LOG_TAG = MainActivity.class.getCanonicalName();
    FragmentPagerAdapter adapterViewPager;

    public AwsManager awsManager;

    public static String awsState;

    public static MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager);
        if (awsManager == null)
            awsManager = new AwsManager(getApplicationContext(), this);

        myHandler = new MyHandler(this);
        ViewPager vpPager = findViewById(R.id.vpPager);
        adapterViewPager = new PagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        EventBus.getDefault().register(this);
        awsManager.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(LOG_TAG, "Destroying activity");
        if (awsManager != null)
            awsManager.disconnect();
        awsManager = null;
    }

    @Override
    public void setStatusState(final String state) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new MessageEvent(state));
            }
        });

        awsState = state;
        Log.i(LOG_TAG, "setStatusState: " + state);
        if (state.equalsIgnoreCase("connected")){
            Log.i(LOG_TAG, "subscribing to ava");
            awsManager.subscribe("ava");
        }
    }

    @Override
    public void notifyUser(String msg) {
        Toast.makeText(this, msg , Toast.LENGTH_SHORT).show();
        Log.i(LOG_TAG, "notifyUser: " + msg);
    }

    @Override
    public void proccessCommand(String cmd) {
        Log.i(LOG_TAG, "proccessCommand: " + cmd);
        final Post command =  CommandTranslator.translateCommand(cmd);
        switch (command.type){
            case Post.POST_TYPE_HEARTBEAT:
                addDevice(new Device(command.sender,true , new Date() ));
                break;
            case Post.POST_TYPE_REPORT:
                addDevice(new Device(command.sender,true , new Date() ));
                fragmentC.publishReport(command.toString());
                break;
            default:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (fragmentB !=null)
                            fragmentB.setReceivedMsg(command.printCommandValue());
                    }
                });
        }
    }

    private void addDevice(Device mDevice){
        for (Device device : FragmentA.deviceList)
            if (device.name.equals(mDevice.name)) {
                device.status = true;
                device.lastStatus = new Date();
                return;
            }
        Log.i(LOG_TAG, "adding device to list");
        FragmentA.deviceList.add(mDevice);
        if (fragmentA != null)
            fragmentA.refreshDeviceList();
    }

    public void updateFragmentStatus(){
        if(fragmentB == null){
            notifyUser("fragment MQTT manager is not active");
            return;
        }
        fragmentB.setTvStatusText("asd");

    }

    FragmentA fragmentA;
    FragmentB fragmentB;
    FragmentC fragmentC;

    @Override
    public void connect() {
        awsManager.connect();
    }

    @Override
    public void disconnect() {
        awsManager.disconnect();
    }

    @Override
    public void subscribe(String topic) {
        awsManager.subscribe(topic);
    }

    @Override
    public void publish(String topic, String msg) {
        Log.i(LOG_TAG, "Publishing to topic: " + topic + ", msg: " + msg);
        awsManager.publish(topic, msg);
    }


    public class PagerAdapter extends FragmentPagerAdapter {
        private final static int NUM_ITEMS = 3;

        public PagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    if (MainActivity.this.fragmentA == null)
                        MainActivity.this.fragmentA = new FragmentA();
                    return MainActivity.this.fragmentA;
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    if (MainActivity.this.fragmentC == null)
                        MainActivity.this.fragmentC = new FragmentC();
                    return MainActivity.this.fragmentC;
                case 2:
                    if (MainActivity.this.fragmentB == null)
                        MainActivity.this.fragmentB = new FragmentB();
                        MainActivity.this.fragmentB.setFragmentCommunicator(MainActivity.this);
                    return MainActivity.this.fragmentB;
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            String title = new String();
            switch (position){
                case 0:
                    title = "Device List";
                    break;
                case 1:
                    title = "Report Wall";
                    break;
                case 2:
                    title = "MQTT Manager";
                    break;

            }
            return title;
        }

    }
}
