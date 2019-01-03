package com.amazonaws.demo.androidpubsubwebsocket;

public interface IAwsCallback {
    void setStatusState(String state);
    void notifyUser(String msg);
    void proccessCommand(String msg);

}
