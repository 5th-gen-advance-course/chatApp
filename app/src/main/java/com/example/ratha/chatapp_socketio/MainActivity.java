package com.example.ratha.chatapp_socketio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView messageRecyclerView;
    EditText etMessage;
    Socket mSocket ;
    List<Message> mMessages=new ArrayList<>();

    {
        mSocket=getConnect();
    }

    public Socket getConnect(){
        try {
            return IO.socket("https://socket-io-chat.now.sh/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageRecyclerView=findViewById(R.id.messageRecylerView);
        etMessage=findViewById(R.id.etMessage);

        //Socket
        mSocket.connect();

    }

    public void onSendMessage(View view) {

    }

    public void emitMessage(){
        String message=etMessage.getText().toString().trim();
        if(TextUtils.isEmpty(message)){
            etMessage.requestFocus();
            return;
        }
        etMessage.setText("");
        mSocket.emit("new message",message);
    }

    private void addMessage(String userName,String message){
        mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
        .username(userName).message(message).build());
        
    }
}
