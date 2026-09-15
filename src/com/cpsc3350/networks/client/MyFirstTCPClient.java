package com.cpsc3350.networks.client;

import com.cpsc3350.networks.model.Request;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Scanner;

public class MyFirstTCPClient {

    private static ArrayList<Short> userInput = new ArrayList<>();
    private static Request request;

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


        while (scan.hasNext()) {
            short itemCode = Short.parseShort(scan.next());
            if (itemCode == -2) {
                userInput.add(itemCode);
                break;
            }
            while (itemCode < 0) {
                System.out.println("Item codes are positive values, please enter a new item code:");
                itemCode = Short.parseShort(scan.next());
            }

            System.out.println("Enter a quantity: ");
            short quantity = Short.parseShort(scan.next());
            if(quantity < 0) {
                System.out.println("Must enter a quantity greater than 0");
                System.out.println("Enter a quantity: ");
                quantity = Short.parseShort(scan.next());
            }

            userInput.add(quantity);
            userInput.add(itemCode);
            System.out.println("Enter an Item (enter -2 to end input): ");
        }

        scan.close();

        request = new Request(userInput);
        byte[] bytes = request.getBytes();
        for(byte b : bytes) {
            System.out.print(String.format("%02X ", b));
        }

        try {
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        clientSocket.close();
    }
}
