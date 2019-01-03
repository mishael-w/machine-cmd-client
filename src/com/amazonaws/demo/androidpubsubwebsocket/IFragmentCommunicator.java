package com.amazonaws.demo.androidpubsubwebsocket;

public interface IFragmentCommunicator {
    void connect();
    void disconnect();
    void subscribe(String topic);
    void publish(String topic, String msg);
}
