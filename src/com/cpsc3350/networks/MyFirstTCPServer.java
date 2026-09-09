package com.cpsc3350.networks;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MyFirstTCPServer {
//    create parent socket -------
//    bind port ------
//    adjust queue limit if needed
//    wait for an incoming request
//    accept that incoming request and create child socket
//    establish input and output streams
//    read and write
//    close child socket

    private static final int BUFFSIZE = 32;

    public static void main(String[] args){

        int portNum = -1;

//      arg length
        if(args.length != 1) {
            throw new IllegalArgumentException("Please enter the correct port");
        }

//        store the correct port number
        if (Integer.parseInt(args[0]) != 10015) {
            System.out.println("port number invalid: " + portNum);
        } else {
            portNum = Integer.parseInt(args[0]);
        }

//      try setting up the server
//      wait for incoming request
        try {
            System.out.println("Initializng port on port number: " + portNum);
            ServerSocket serverSocket = new ServerSocket(portNum);

            byte[] byteBuffer = new byte[BUFFSIZE];
            int incMsgSize;

            for(;;) {
                Socket clientSocket = serverSocket.accept();

                System.out.println("Client socket open at: " +
                        clientSocket.getInetAddress().getHostAddress() +
                        "at port number: " + clientSocket.getPort());

                InputStream in = clientSocket.getInputStream();
                OutputStream out = clientSocket.getOutputStream();

                while((incMsgSize = in.read(byteBuffer)) != -1) {
                    out.write(byteBuffer, 0, incMsgSize);
                    byte b = (byte) incMsgSize;
                    System.out.println(b);
                }


            }


        } catch (IOException ioe) {
            System.err.println("Error: Server socket not created on port " + portNum);
            ioe.getStackTrace();
        }

    }





}
