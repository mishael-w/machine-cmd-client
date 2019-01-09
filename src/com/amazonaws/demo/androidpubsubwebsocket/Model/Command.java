package com.amazonaws.demo.androidpubsubwebsocket.Model;

public class Command {
    int destination; // 1=hsl0, 2=hsl1

    String name;
    String content;
    public Command(int destination, String name, String content) {
        this.destination = destination;
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public int getDestination() {
        return destination;
    }
//
//    public Command(String name, String content) {
//
//        this.name = name;
//        this.content = content;
//    }

    @Override
    public String toString() {
        return name;
    }
}
