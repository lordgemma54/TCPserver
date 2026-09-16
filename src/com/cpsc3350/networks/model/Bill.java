package com.cpsc3350.networks.model;

import com.cpsc3350.networks.utility.ProcessedItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

public class Bill {

    private byte[] clientRequest;
    private String filePath = "data.csv";
    private final Map<Short, ItemDetails> itemMap = new HashMap<>();

//    -------------------------BILL CONSTRUCTOR-----------------------------
    public Bill (byte[] clientRequest) {
       this.clientRequest = clientRequest;
       loadCSV(this.filePath);
    }

//    ----------------------------------------------------------------------
    public class ItemDetails {
        String description;
        short itemCost;

        public ItemDetails(String description, short itemCost) {
            this.description = description;
            this.itemCost = itemCost;
        }
    }
//    ----------------------------READ FILE------------------------------------------
    private void loadCSV(String filePath) {
        System.out.println("Looking for CSV at: " + new File(filePath).getAbsolutePath());
        File file = new File(filePath);

        try(Scanner scanner = new Scanner(file)) {
            while(scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] tokens = line.split(",");

                if (tokens.length >= 3) {
                    try {
                        if (tokens[0].startsWith("\uFEFF")) {
                            tokens[0] = tokens[0].substring(1);
                        }
                        short itemCode = Short.parseShort(tokens[0].trim());
                        String description = tokens[1].trim();
                        short cost = Short.parseShort(tokens[2].trim());
                        itemMap.put(itemCode, new ItemDetails(description, cost));
                    } catch (NumberFormatException n) {
                        System.err.println("Invalid CSV line " + line);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Could not find file: " + filePath);
        }
    }

//    ----------------------------BUILD BILL------------------------------------------
    public byte[] buildBill() {

        ByteBuffer readBuffer = ByteBuffer.wrap(clientRequest);
        readBuffer.order(ByteOrder.BIG_ENDIAN);

        short incomingTML = readBuffer.getShort();
        short incomingRequestNum = readBuffer.getShort();

//        builds error response if tml and byte array length do not match
            if (incomingTML != clientRequest.length) {
                // Build the error response: request number (short) + -2 (short)
                ByteBuffer errorBuffer = ByteBuffer.allocate(4);
                errorBuffer.order(ByteOrder.BIG_ENDIAN);
                errorBuffer.putShort(incomingRequestNum);
                errorBuffer.putShort((short) -2);

                return errorBuffer.array();
              }

        System.out.println("Incoming TML: " + incomingTML);
        System.out.println("Incoming Request number: " + incomingRequestNum);

        ArrayList<ProcessedItem> processedItems = new ArrayList<>();
        int billTotal = 0;


        while(readBuffer.hasRemaining()) {
            short incomingQuantity = readBuffer.getShort();
            if (incomingQuantity == -2) {
                break;
            }
            short incomingItemCode = readBuffer.getShort();

            String description = "";
            short cost =  0;

            ItemDetails itemDetail = itemMap.get(incomingItemCode);
                if(itemDetail != null) {
                    description = itemDetail.description;
                    cost = itemDetail.itemCost;
                } else {
                    description = "Article not available";
                }
                billTotal += cost * incomingQuantity;
            ProcessedItem processedItem = new ProcessedItem(description, incomingQuantity, cost);
            processedItems.add(processedItem);
        }

        return createResponse(incomingRequestNum, billTotal, processedItems);

    }

    private byte[] createResponse(short requestNum, int billTotal, ArrayList<ProcessedItem> items) {

//        Allocate space for the response array
//        2 for TML, 2 for requestNum, 4 for billTotal
        int responseLength = 2 + 2 + 4;

        for (ProcessedItem item : items) {
            byte [] descriptionBytes = item.getDescription().getBytes();
            responseLength += 1 + descriptionBytes.length + 2 + 2;
        }

        ByteBuffer buffer = ByteBuffer.allocate(responseLength);
        buffer.order(ByteOrder.BIG_ENDIAN);

//        Create response byte array
        buffer.putShort(requestNum);
        buffer.putShort((short) responseLength);
        buffer.putInt(billTotal);

        for(ProcessedItem item : items) {
            byte[] descriptionBytes = item.getDescription().getBytes();
            buffer.put((byte)descriptionBytes.length);
            buffer.put(descriptionBytes);
            buffer.putShort(item.getCost());
            buffer.putShort(item.getQuantity());
        }
        return buffer.array();
    }



}

