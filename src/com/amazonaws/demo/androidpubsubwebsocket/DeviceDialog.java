package com.amazonaws.demo.androidpubsubwebsocket;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.amazonaws.demo.androidpubsubwebsocket.Model.Command;
import com.amazonaws.demo.androidpubsubwebsocket.Model.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static com.amazonaws.demo.androidpubsubwebsocket.MyHandler.ACTION_PUBLISH;

public class DeviceDialog extends DialogFragment {
    private final String TAG = "DeviceDialog";
    private final String CMD_FILE_NAME = "cmds.txt";


    ArrayList<Command> commandsList;
    Spinner cmdSpinner;
    Button showCmdOption;
    Button addCmd;
    Button executeCmd;
    Button cancelBtn;
    TextView deviceImeiTitle;
    TextView enterCmdTitle;
    EditText cmdContent;
    EditText cmdName;
    TextView cmdNameTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // This removes black background below corners.
        setStyle(R.style.CustomDialogTheme, 0);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.device_dialog, container, false);
        showCmdOption =  v.findViewById(R.id.show_add_cmd_options);
        cmdSpinner =  v.findViewById(R.id.cmd_spinner);
        deviceImeiTitle =  v.findViewById(R.id.title_device_name);
        cmdContent =  v.findViewById(R.id.cmd_content_tv);
        enterCmdTitle =  v.findViewById(R.id.enter_cmd_title);
        addCmd =  v.findViewById(R.id.add_cmd_btn);
        cmdNameTitle = v.findViewById(R.id.enter_cmd_name_title);
        cmdName = v.findViewById(R.id.cmd_name_edit_text);
        executeCmd = v.findViewById(R.id.execute_cmd);
        cancelBtn = v.findViewById(R.id.cancel_btn);
        hideCmdOptions();
        populateSpinner();

        showCmdOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cmdName.setText("");
                cmdContent.setText("");
                refreshSpinner();
                activateCmdOptions();
            }
        });

        Bundle data = getArguments();
        deviceImeiTitle.setText(data.getString("deviceName"));
        addCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cmdNameValue = cmdName.getText().toString();
                String cmdContentValue = cmdContent.getText().toString();
                addCmdToFile(new Command(cmdNameValue, cmdContentValue));
                if (commandsList == null){
                    commandsList = new ArrayList<>();
                }
                commandsList.add(new Command(cmdNameValue, cmdContentValue));
                cmdName.setText("");
                cmdContent.setText("");
                refreshSpinner();
                hideCmdOptions();
            }
        });


        executeCmd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Command chosenCommand = (Command) cmdSpinner.getSelectedItem();
                Log.i(TAG, "calling MainActivity.publish");
                MainActivity activity = (MainActivity)getActivity();
                Post postToPublish = new Post(Post.POST_TYPE_PERSONAL, activity.awsManager.clientId, chosenCommand.getContent() );
                Bundle bundle = new Bundle();
                bundle.putString("msg", postToPublish.toJSon());
                bundle.putString("topic", deviceImeiTitle.getText().toString());
                Message msg = new Message();
                msg.what = ACTION_PUBLISH;
                msg.setData(bundle);
                MainActivity.myHandler.sendMessage(msg);
                hideCmdOptions();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideCmdOptions();
            }
        });



        return v;
    }


    private void hideCmdOptions(){
        enterCmdTitle.setVisibility(View.INVISIBLE);
        enterCmdTitle.setEnabled(false);
        cmdContent.setVisibility(View.INVISIBLE);
        cmdContent.setEnabled(false);
        addCmd.setVisibility(View.INVISIBLE);
        addCmd.setEnabled(false);
        cmdName.setVisibility(View.INVISIBLE); ;
        cmdName.setEnabled(false);
        cmdNameTitle.setVisibility(View.INVISIBLE); ;
        cmdNameTitle.setEnabled(false);
        cancelBtn.setVisibility(View.INVISIBLE);
        cancelBtn.setEnabled(false);
        showCmdOption.setEnabled(true);
        cmdSpinner.setEnabled(true);
    }

    private void activateCmdOptions(){
        enterCmdTitle.setVisibility(View.VISIBLE);
        enterCmdTitle.setEnabled(true);
        cmdContent.setVisibility(View.VISIBLE);
        cmdContent.setEnabled(true);
        addCmd.setVisibility(View.VISIBLE);
        addCmd.setEnabled(true);
        cmdName.setEnabled(true);
        cmdName.setVisibility(View.VISIBLE);
        cmdNameTitle.setEnabled(true);
        cmdNameTitle.setVisibility(View.VISIBLE);
        cancelBtn.setVisibility(View.VISIBLE);
        cancelBtn.setEnabled(true);
        showCmdOption.setEnabled(false);
        cmdSpinner.setEnabled(false);
    }

    private void refreshSpinner(){
        ArrayAdapter<Command> adapter = new ArrayAdapter<Command>(getContext(), android.R.layout.simple_spinner_item, commandsList){
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View listItem = convertView;
                if(listItem == null)
                    listItem = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_item,parent,false);

                TextView tv = listItem.findViewById(android.R.id.text1);
                String name = getItem(position).getName();
                tv.setText(name);
                return listItem;
            }
        };
        cmdSpinner.setAdapter(adapter);
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        Log.i(TAG, "exec command should be: " + String.valueOf(commandsList != null && commandsList.size() > 0));
        executeCmd.setEnabled(commandsList != null && commandsList.size() > 0);
    }

    private void populateSpinner() {
        if (commandsList == null)
            commandsList = new ArrayList<>();
        String cmdFileContent = getCmdFileContent();
        if (cmdFileContent == null || cmdFileContent.isEmpty()){
            refreshSpinner();
            return;
        }

        try {
            JSONObject root = new JSONObject(cmdFileContent);

            if (!root.has("commands"))
                return;

            JSONArray commandsArray = root.getJSONArray("commands");

            for (int i = 0; i < commandsArray.length(); i++) {
                JSONObject object = commandsArray.getJSONObject(i);
                String commandName = object.getString("name");
                String commandContent = object.getString("content");
                Command tempCommand = new Command(commandName, commandContent);
                commandsList.add(tempCommand);
            }
        } catch (JSONException e) {
            Log.i(TAG, "populateSpinner --> JSONException");
            e.printStackTrace();
        }

        refreshSpinner();

    }

    private String getCmdFileContent(){
        StringBuffer buffer = null;
        FileInputStream fis = null;// throws FileNotFoundException
        try {
            fis = getContext().openFileInput(CMD_FILE_NAME);
            int readData = -1;

            buffer = new StringBuffer();
            while ((readData = fis.read()) != -1) {// throws IOException
                buffer.append((char) readData);
            }

        } catch (FileNotFoundException e) {
            Log.v(TAG, "getCmdFileContent --> FileNotFoundException");
            createCmdsFile();
        }catch(IOException e){
            Log.v(TAG, "getCmdFileContent --> IOException");
            e.printStackTrace();
        }finally{
            try {
                if (fis != null)
                    fis.close();
            } catch (IOException e) {
                Log.v(TAG, "getCmdFileContent --> IOException in finally");
                e.printStackTrace();
            }

        }
        if (buffer == null) {
            Log.v(TAG, "getCmdFileContent --> returnning null");
            return null;
        }
        Log.v(TAG, "getCmdFileContent --> returnning: " + buffer.toString());
        return buffer.toString();
    }

    private void createCmdsFile(){
        FileOutputStream fos = null;
        try {
            fos = getContext().openFileOutput(CMD_FILE_NAME, Context.MODE_PRIVATE);
            byte[] buffer = new byte[]{};

            fos.write(buffer);
            Log.v(TAG, "createCmdsFile --> created file");
        } catch (FileNotFoundException e) {
            Log.v(TAG, "createCmdsFile --> FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG, "createCmdsFile --> IOException");
            e.printStackTrace();
        }
    }

    private void addCmdToFile(Command command){
        String cmdFileContent = getCmdFileContent();
        if (cmdFileContent == null) {
            Log.v(TAG, "addCmdToFile --> IOException");
            return;
        }

        try {
            //
            JSONObject commandAsJson = new JSONObject();
            commandAsJson.put("name", command.getName());
            commandAsJson.put("content", command.getContent());

            JSONObject root;
            if (cmdFileContent != null && !cmdFileContent.isEmpty())
                root = new JSONObject(cmdFileContent);
            else
                root = new JSONObject();

            JSONArray commandsArray;
            if (root.has("commands"))
                commandsArray = root.getJSONArray("commands");
            else
                commandsArray = new JSONArray();

            commandsArray.put(commandAsJson);
            root.put("commands", commandsArray);
            Log.v(TAG, "addCmdToFile --> JSONException");
            writeToFile(root.toString());
        } catch (JSONException e) {
            Log.v(TAG, "addCmdToFile --> JSONException");
            e.printStackTrace();
        }
    }

    private void writeToFile(String data){
        FileOutputStream fos = null;
        try {
            fos = getContext().openFileOutput(CMD_FILE_NAME, Context.MODE_PRIVATE);
            byte[] buffer = data.getBytes();
            fos.write(buffer);
            Log.v(TAG, "writeToFile --> wrote to file!!!");
        } catch (FileNotFoundException e) {
            Log.v(TAG, "writeToFile --> FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.v(TAG, "writeToFile --> IOException");
            e.printStackTrace();
        }
    }

}
