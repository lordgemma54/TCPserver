package com.cpsc3350.networks;

import java.io.IOException;
import java.net.ServerSocket;

public class MyFirstTCPServer {

    public void methodName() throws IOException{
        try {
            ServerSocket server = new ServerSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
