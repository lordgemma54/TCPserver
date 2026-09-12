package com.cpsc3350.networks.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFirstTCPClient {

    private static final int BUFFSIZE = 32;
    private static ArrayList<Integer> userInput = new ArrayList<>();

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


        Scanner scan = new Scanner(System.in);

        System.out.println("Enter an Item (enter -2 to end input): ");


        int currentInput;

        while (scan.hasNext()) {

            if (Integer.parseInt(scan.next()) == -2) {
                currentInput = Integer.parseInt(scan.next());
                userInput.add(currentInput);
                return;
            }

            currentInput = Integer.parseInt(scan.next());
            userInput.add(currentInput);

            System.out.println("Enter a quantity: ");
            if(Integer.parseInt(scan.next()) < 0) {
                System.out.println("Must enter a quantity greater than 0");
                System.out.println("Enter a quantity: ");
            }
            userInput.add(Integer.parseInt(scan.next()));

            System.out.println("Enter an item code (enter -2 to end input): ");
        }

        ByteBuffer buffer = ByteBuffer.allocate(userInput.size() * 4);
        for (Integer value : userInput) {
            buffer.putInt(value);
        }

        byte[] byteArray = buffer.array();

        try {
            out.write(byteArray);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        The stored values should then be encoded into a byte array (A) in Big Endian.
//        String userInput = scan.nextLine();
        scan.close();

//  store contents of user input
//        byte[] message = userInput.getBytes();
//        int outMsgSize = message.length;

//        out.write(message, 0, outMsgSize);
//        out.flush();

        clientSocket.close();

    }

    private byte[] encode (String message) {
        byte[] frame = new byte[message.length()];

        return frame;
    }
}
