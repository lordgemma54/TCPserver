package com.cpsc3350.networks.server;

import com.cpsc3350.networks.model.Bill;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

public class MyFirstTCPServer {
//    create parent socket -------
//    bind port ------
//    adjust queue limit if needed
//    wait for an incoming request
//    accept that incoming request and create child socket
//    establish input and output streams
//    read and write
//    close child socket

//    1 - write a simple server
//    2 - write a simple client
//    3 - send stuff back and forth to test / git
//    4 - write a bill class
//    5 - write encoding class
//    6 - write decoding class
//    7 - test encoding / git
//    8 - incorporate encoding into server / client paradigm
//    9 - test / git
//    10 - check all criteria
//

    private static final int BUFFSIZE = 1024;


    public static void main(String[] args){
//      arg length
        if(args.length != 1) {
            throw new IllegalArgumentException("Please enter the correct port");
        }

        int portNum = Integer.parseInt(args[0]);

//        store the correct port number
        while (portNum != 10015) {
            System.out.println("port number invalid: " + portNum + " Please try again.");
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

//  ------------------------------ CAPTURE TML ----------------------
                byte[] bytesTML = new byte[2];
                readFullArray(in, bytesTML);

                ByteBuffer TMLBuffer = ByteBuffer.wrap(bytesTML);
                short tml = TMLBuffer.getShort();

                System.out.println("TML = " + tml);

//  ------------------------------ CAPTURE REMAINING ----------------------
                byte[] receivedBytes = new byte[tml];
                System.arraycopy(bytesTML, 0, receivedBytes, 0, 2);
                readFullArray(in, receivedBytes);

//  ----------------------------------------------------
                System.out.print("Received hex values from client: ");
                for(byte b : receivedBytes) {
                    System.out.print(String.format("%02X ", b & 0xFF));
                }

                byte[] responseBytes = processBill(receivedBytes);
                out.write(responseBytes);
                out.flush();
                clientSocket.close();
            }
        } catch (IOException ioe) {
            System.err.println("Error: Server socket not created on port " + portNum);
            ioe.printStackTrace();
        }
    }

    static byte[] processBill(byte[] receivedBytes) {
            Bill bill = new Bill(receivedBytes);
            return bill.buildBill();
    }

    private static void readFullArray(InputStream in, byte[] buffer) throws IOException {
        int bytesRead = 0;
        while(bytesRead < buffer.length) {
            int n = in.read(buffer, bytesRead, buffer.length - bytesRead);
            if (n == -1) {
                throw new IOException("Incomplete array received");
            }
            bytesRead += n;
        }
    }

// look up code received from the client, match it to the contents of the data file, and return the corresponding responses
// javac com/cpsc3350/networks/client/MyFirstTCPClient.java     TO COMPILE
// java com.cpsc3350.networks.client.MyFirstTCPClient localhost 10015      TO RUN

}
