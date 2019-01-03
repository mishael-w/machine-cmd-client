package com.amazonaws.demo.androidpubsubwebsocket.Model;

import org.json.JSONException;
import org.json.JSONObject;

public class CommandTranslator {



   public static Post translateCommand(String command){
       try {
           JSONObject cmd = new JSONObject(command);
           int type = cmd.getInt("type");
           String senderName = cmd.getString("sender");
           if (cmd.has("data"))
               return new Post(type , senderName, cmd.getString("data"));
           return new Post(type , senderName);
       } catch (JSONException e) {
           e.printStackTrace();
           return new Post(-1 , "unknown", command);
       }
   }
}
