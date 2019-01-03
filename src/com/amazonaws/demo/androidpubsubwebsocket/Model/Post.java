package com.amazonaws.demo.androidpubsubwebsocket.Model;


import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    public final static int POST_TYPE_HEARTBEAT = 0;
    public final static int POST_TYPE_REPORT = 1;
    public final static int POST_TYPE_PERSONAL = 2;

    public int type;
    public String sender;
    public String data;

    public Post(int type, String sender, String data) {
        this.type = type;
        this.sender = sender;
        this.data = data;
    }

    public Post(int type, String sender) {
        this.type = type;
        this.sender = sender;
    }

    @Override
    public String toString() {
        return sender + ":\n" + data + "\n";
    }

    public String printCommandValue(){
        return sender + ":\ntype: "+ type +"\ndata:" + data + "\n";
    }

    public String toJSon(){
        JSONObject root = new JSONObject();
        try {
            root.put("type", type);
            root.put("sender", sender);
            root.put("data", data);
        } catch (JSONException e) {
            e.printStackTrace();
            return "{" +
                    "\"type\": " + type + ",\n" +
                    "\"sender\": \"" + sender + "\",\n" +
                    "\"data\": \"" + data + "\"\n" +
                    "}" ;
        }
        return root.toString();

    }
}
