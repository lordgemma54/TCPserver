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

        System.out.println("Enter an Item code: ");

        while (scan.hasNext()) {
            short itemCode = Short.parseShort(scan.next());
            while (itemCode < 0) {
                System.out.println("Item codes are positive values, please enter a new item code:");
                itemCode = Short.parseShort(scan.next());
            }

            System.out.println("Enter a quantity (enter -2 to end input): ");
            short quantity = Short.parseShort(scan.next());
            if (quantity == -2) {
                userInput.add(quantity);
                break;
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

//        ---------------------------------- READ SERVER RESPONSE ---------------
        byte [] requestNumBytes = new byte[2];
        readFullArray(in,requestNumBytes, 0, 2);
        ByteBuffer reqNumBuffer = ByteBuffer.wrap(requestNumBytes);
        short incomingReqNum = reqNumBuffer.getShort();
        System.out.println(incomingReqNum);

        byte [] incomingTML = new byte [2];
        readFullArray(in, incomingTML, 0, 2);
        ByteBuffer tmlBuffer = ByteBuffer.wrap(incomingTML);
        short responseTml = tmlBuffer.getShort();
        System.out.println(responseTml);

        byte[] receivedBytes = new byte[responseTml];
        System.arraycopy(requestNumBytes, 0, receivedBytes, 0, 2);
        System.arraycopy(incomingTML, 0, receivedBytes, 2, 2);

        readFullArray(in, receivedBytes, 4, responseTml - 4);

        clientSocket.close();

        for (byte b : receivedBytes) {
            System.out.printf(
                    "0x%02X ",
                    b & 0xFF
            );
        }

        ByteBuffer finalBuffer = ByteBuffer.wrap(receivedBytes);
        short requestNum = finalBuffer.getShort();
        short responseTML = finalBuffer.getShort();
        int responseTotalCost = finalBuffer.getInt();

        System.out.println();
        System.out.println("==========================================");
        System.out.println("                 BILL");
        System.out.println("==========================================");
        System.out.println("Request Number: " + requestNum);
        System.out.println("------------------------------------------");

        System.out.printf("%-25s %8s %8s %8s%n", "Description", "Cost", "Quantity", "Line Cost");
        System.out.println("------------------------------------------");

        while(finalBuffer.hasRemaining()) {
            int descLength = finalBuffer.get() & 0xFF;
            byte[] descriptionBytes = new byte[descLength];
            finalBuffer.get(descriptionBytes);
            String description = new String(descriptionBytes);
            short itemCost = finalBuffer.getShort();
            short quantity = finalBuffer.getShort();
            int lineCost = itemCost * quantity;

            System.out.printf("%-25s %8d %8d %8d%n",
                    description,
                    itemCost,
                    quantity,
                    lineCost);
        }
        System.out.println("------------------------------------------");
        System.out.printf("%-25s %8s %8s %8d%n",
                "TOTAL",
                "",
                "",
                responseTotalCost);
        System.out.println("==========================================");

//        client must take a string url OR a dotted quad - and port number
//        
    }

    private static void readFullArray(InputStream in, byte[] buffer, int offset, int length) throws IOException {
        int bytesRead = 0;
        while(bytesRead < length) {
            int n = in.read(buffer, offset + bytesRead, length - bytesRead);
            if (n == -1) {
                throw new IOException("Incomplete array received");
            }
            bytesRead += n;
        }
    }

}
