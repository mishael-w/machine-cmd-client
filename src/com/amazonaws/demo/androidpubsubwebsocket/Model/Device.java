package com.amazonaws.demo.androidpubsubwebsocket.Model;


import java.util.Date;

public class Device {
    public String name;
    public boolean status;
    public Date lastStatus;

    public Device(String name, boolean status, Date lastStatus) {
        this.name = name;
        this.status = status;
        this.lastStatus = lastStatus;
    }
}
