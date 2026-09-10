package com.cpsc3350.networks.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyFirstTCPClient {

    private static final int BUFFSIZE = 32;

    public static void main(String[] args) throws IOException {

        if(args.length != 2) {
            throw new IllegalArgumentException("Please enter an IP address and port number");
        }

        String urlName = args[0];
        int portNum = Integer.parseInt(args[1]);

        System.out.println("Connecting to url: " + urlName + " on port: " + portNum);
        Socket clientSocket = new Socket(urlName, portNum);
        InputStream in = clientSocket.getInputStream();
        OutputStream out = clientSocket.getOutputStream();

        System.out.println("provide details of your order: ");
        Scanner scan = new Scanner(System.in);

        String userInput = scan.nextLine();
        scan.close();

//  store contents of user input
        byte[] message = userInput.getBytes();
        int outMsgSize = message.length;

        out.write(message, 0, outMsgSize);
        out.flush();

        clientSocket.close();

    }
}
