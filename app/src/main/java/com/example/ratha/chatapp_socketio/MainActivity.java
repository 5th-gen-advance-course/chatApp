package com.example.ratha.chatapp_socketio;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.example.ratha.chatapp_socketio.adapter.MessageAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static final String NEW_MESSAGE = "message";
    private static final String USER_JOIN = "join";
    private static final String USER_TYPING = "typing";
    private static final String USER_STOP_TYPING = "stop typing";

    RecyclerView messageRecyclerView;
    EditText etMessage;
    Socket mSocket ;
    MessageAdapter messageAdapter;
    List<Message> mMessages=new ArrayList<>();

    private boolean mTyping = false;
    private String mUserName="AYA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageRecyclerView=findViewById(R.id.messageRecyclerView);
        etMessage=findViewById(R.id.etMessage);
        App app=(App) getApplication();
        //Socket
        mSocket=app.getSocket();
        mSocket.on(Socket.EVENT_CONNECT,onSocketConnect);
        mSocket.on(Socket.EVENT_DISCONNECT,onSocketDisconnect);
        mSocket.on(USER_JOIN,onUserJoin);
        //mSocket.on(NEW_MESSAGE,onNewMessage);
        mSocket.connect();

        //setup message recycler view
        setupMessageRecyclerView();

    }

    private void setupMessageRecyclerView() {
        messageRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageAdapter=new MessageAdapter(this,mMessages);
        messageRecyclerView.setAdapter(messageAdapter);
    }

    public void onSendMessage(View view) {
        this.send();
    }

    public void send(){
        mTyping=false;

        String message=etMessage.getText().toString().trim();
        //Log.e("message->",message);
        if(TextUtils.isEmpty(message)){
            etMessage.requestFocus();
            return;
        }

        etMessage.setText("");
        addMessage(mUserName,message);
        //Log.e(TAG, "send: socket emit");
        // perform the sending message attempt.
        mSocket.emit("new message",message);
    }

    private void addMessage(String userName,String message){
        this.mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
        .username(userName).message(message).build());

        messageAdapter.notifyItemInserted(this.mMessages.size()-1);
        scrollToBottom();
    }
    private void scrollToBottom() {
        messageRecyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);
    }

    private Emitter.Listener onNewMessage=new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data= (JSONObject) args[0];
                    String userName="";
                    String message="";
                   /* try{
                        userName=data.getString("username");
                        message=data.getString("message");
                    }catch (JSONException e){
                        Log.e(TAG,e.getMessage());
                    }*/

                    addMessage(userName,message);
                }
            });
        }
    };
    private Emitter.Listener onUserJoin=new Emitter.Listener() {
        @Override
        public void call(Object... args) {

        }
    };
    private Emitter.Listener onSocketConnect=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "call: socket connected");
        }
    };

    private Emitter.Listener onSocketDisconnect=new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "call: socket disconnected" );
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off(NEW_MESSAGE,onNewMessage);
    }
}
