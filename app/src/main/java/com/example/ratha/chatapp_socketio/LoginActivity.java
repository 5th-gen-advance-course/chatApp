package com.example.ratha.chatapp_socketio;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LoginActivity extends AppCompatActivity {

    final static String EVENT_LOGIN="join";
    private static final String TAG ="LoginActivity" ;
    EditText tvUserName;
    Socket mSocket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvUserName=findViewById(R.id.username);
        App app=(App) getApplication() ;
        mSocket=app.getSocket();
        //mSocket.on(EVENT_LOGIN,onUserLogin);
        //mSocket.connect();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.off(EVENT_LOGIN,onUserLogin);
    }

    public void onLogin(View view) {
        Log.e(TAG, "onLogin: clicked");
        login();
        Log.e(TAG, "onLogin: after clicked");
        Intent intent =new Intent();
        intent.putExtra("username",mUserName);
        setResult(RESULT_OK,intent);
        finish();
        Log.e(TAG, "onLogin: after clicked");
    }

    private String mUserName;
    private void login() {
        String username=tvUserName.getText().toString().trim();
        if(!TextUtils.isEmpty(username)){
            tvUserName.requestFocus();
            return;
        }

        mUserName=username;
        mSocket.emit(EVENT_LOGIN,mUserName);

    }

    private Emitter.Listener onUserLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.e(TAG, "call: Login");
        }
    };

}
