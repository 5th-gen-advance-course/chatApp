package com.example.ratha.chatapp_socketio;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

/**
 * Created by ratha on 12/21/2017.
 */

public class App extends Application {
    private Socket socket;
    {
        try {
            socket= IO.socket("http://192.168.178.118:3000/chat");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket(){
        return socket;
    }
}
