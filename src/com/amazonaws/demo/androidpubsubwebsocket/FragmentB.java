package com.amazonaws.demo.androidpubsubwebsocket;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.demo.androidpubsubwebsocket.Model.MessageEvent;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;




public class FragmentB extends Fragment {
    private final static String TAG = "FragmentB";

    static final String LOG_TAG = PubSubActivity.class.getCanonicalName();

    public void setFragmentCommunicator(IFragmentCommunicator fragmentCommunicator) {
        this.fragmentCommunicator = fragmentCommunicator;
    }

    IFragmentCommunicator fragmentCommunicator;

    AwsManager manager;

    EditText txtSubscribe;
    EditText txtTopic;
    EditText txtMessage;

    TextView tvLastMessage;
    TextView tvClientId;
    TextView tvStatus;
    Button btnConnect;
    Button btnDisconnect;
    Button btnPublish;

    /**
     * 1. called when the fragment is linked to the activity
     */
    @Override
    public void onAttach(Context context) {
        Log.v(TAG,"onAttach");
        super.onAttach(context);
    }

    /**
     *  2. start threads here but not UI stuff
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v(TAG,"onCreate");
    }

    /**
     *  3. this is where you inflate your own layout
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.v(TAG,"onCreateView");
        View v = inflater.inflate(R.layout.fragment_b, container, false);
        txtSubscribe = v.findViewById(R.id.txtSubscribe);
        txtTopic = v.findViewById(R.id.txtTopic);
        txtMessage = v.findViewById(R.id.txtMessage);

        tvLastMessage = v.findViewById(R.id.tvLastMessage);
        tvClientId = v.findViewById(R.id.tvClientId);
        tvStatus = v.findViewById(R.id.tvStatus);

        btnConnect = v.findViewById(R.id.btnConnect);
//        btnConnect.setEnabled(false);
        Button btnDisconnect = v.findViewById(R.id.btnDisconnect);
        Button btnSubscribe= v.findViewById(R.id.btnSubscribe);
        Button btnPublish= v.findViewById(R.id.btnPublish);

        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCommunicator.connect();
            }
        });

        btnDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCommunicator.disconnect();
            }
        });

        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCommunicator.subscribe(txtSubscribe.getText().toString());
            }
        });
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragmentCommunicator.publish(txtTopic.getText().toString(),txtMessage.getText().toString() );
            }
        });

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**
     *  4. ui actions should take place here
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvStatus.setText(MainActivity.awsState);
        super.onViewCreated(view, savedInstanceState);
    }

    @Subscribe
    public void onEvent(MessageEvent event){
        tvStatus.setText(event.getMessage());
    }

    /**
     *  this is called when activity's onCreate function is done
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.v(TAG,"onActivityCreated");
    }

    public void setManager(AwsManager mManager){
        manager = mManager;
    }

    public void setTvStatusText(String status){
        tvStatus.setText(status);
    }

    public void setReceivedMsg(String string){
        tvLastMessage.setText(string);
    }
}
