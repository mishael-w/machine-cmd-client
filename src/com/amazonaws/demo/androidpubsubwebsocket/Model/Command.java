package com.amazonaws.demo.androidpubsubwebsocket.Model;

public class Command {
    String name;
    String content;

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public Command(String name, String content) {

        this.name = name;
        this.content = content;
    }

    @Override
    public String toString() {
        return name;
    }
}
