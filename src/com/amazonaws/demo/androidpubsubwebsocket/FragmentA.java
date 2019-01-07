package com.amazonaws.demo.androidpubsubwebsocket;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.demo.androidpubsubwebsocket.Model.Device;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


public class FragmentA extends Fragment {
    private final static String TAG = "FragmentA";

    private DeviceListSynchronizer deviceListSynchronizer = new DeviceListSynchronizer();
    private ListView deviceListView;
    public static ArrayList<Device> deviceList;


    /**
     *  2. start threads here but not UI stuff
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate");
        deviceList = new ArrayList<>();
    }

    /**
     *  3. this is where you inflate your own layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView");
        View v = inflater.inflate(R.layout.fragment_a, container, false);
        deviceListView = v.findViewById(R.id.device_list);
        deviceListView.setAdapter(new ImproveAdapter(getContext(), 0, deviceList));
        deviceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!((Device)deviceListView.getItemAtPosition(i)).status){
                    if (getContext() != null)
                        Toast.makeText(getContext(),"Device is offline", Toast.LENGTH_SHORT ).show();
                    return;
                }
                DialogFragment newFragment = new DeviceDialog();
                Bundle args = new Bundle();
                Device device = (Device) deviceListView.getItemAtPosition(i);
                args.putString("deviceName", device.name); //The first parameter is the key that will be used to retrieve the value, which is the second parameter.
                newFragment.setArguments(args);
                newFragment.setStyle(R.style.CustomDialogTheme, 0);
                FragmentManager fm = getActivity().getSupportFragmentManager();
                if (fm != null)
                    newFragment.show(fm , "add_a_member");
            }
        });
        deviceListSynchronizer.start();
        return v;
    }

    /**
     *  4. ui actions should take place here
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }



    @Override
    public void onDestroy() {
        deviceListSynchronizer.stop();
        super.onDestroy();
        Log.v(TAG,"onDestroy");
    }

    public void refreshDeviceList(){
        if (deviceListView == null)
            return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ((ImproveAdapter)deviceListView.getAdapter()).notifyDataSetChanged();
            }
        });
    }


    public class ImproveAdapter extends ArrayAdapter<Device> {
        ArrayList<Device> deviceArrayList;
        Context context;

        public ImproveAdapter(@NonNull Context mContext, int resource, ArrayList<Device> devices) {
            super(mContext, resource);
            Log.v(TAG,"deviceList.size = " + devices.size());
            context = mContext;
            this.deviceArrayList = devices;
        }



        @Override
        public int getCount() {
            return deviceArrayList.size();
        }

        @Nullable
        @Override
        public Device getItem(int position) {
            return deviceArrayList.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            convertView =((Activity)context).getLayoutInflater().inflate(R.layout.list_view_item, parent, false);
            TextView name = convertView.findViewById(R.id.name);
            ImageView image = convertView.findViewById(R.id.status_indicator);

            Device device = deviceArrayList.get(position);

            name.setText(device.name);
            Log.i(TAG, "putting name = " + deviceArrayList.get(position).name);
            Log.i(TAG, "putting status = " +deviceArrayList.get(position).status);
            if (device.status)
                image.setImageResource(R.drawable.status_available);
            else
                image.setImageResource(R.drawable.status_unavailable);
            return convertView;

        }
    }


    private class DeviceListSynchronizer {
        private boolean started = false;
        private Handler handler = new Handler();

        private Runnable runnable = new Runnable() {
            @Override
            public void run() {
                syncList();
                if(started) {
                    start();
                }
            }
        };

        public void stop() {
            started = false;
            handler.removeCallbacks(runnable);
        }

        public void start() {
            started = true;
            handler.postDelayed(runnable, 5000);
        }

        private void syncList(){
            for (Device item : deviceList){
                if (System.currentTimeMillis() - item.lastStatus.getTime() > 30000)
                    item.status = false;
                else
                    item.status = true;
            }
            refreshDeviceList();
        }
    }
}