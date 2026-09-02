package com.cpsc3350.networks;

import java.io.IOException;

import static java.lang.Integer.parseInt;


public class Main {
        public static String hostName;
        public static int portNumber;

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            throw new IllegalArgumentException("Incorrect number of params");
        }

        hostName = args[0];
        portNumber = parseInt(args[1]);

    System.out.println(hostName);
    System.out.println(portNumber);
    }
}
