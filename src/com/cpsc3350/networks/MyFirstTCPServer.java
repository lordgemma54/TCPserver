package com.cpsc3350.networks;

import java.io.IOException;
import java.net.ServerSocket;

public class MyFirstTCPServer {

//    create parent socket
//    bind port
//    adjust queue limit if needed
//    wait for an incoming request
//    accept that incoming request and create child socket
//    establish input and output streams
//    read and write
//    close child socket
    

    public void methodName() throws IOException{
        try {
            ServerSocket server = new ServerSocket();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
