package com.cpsc3350.networks.model;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class Request {
    private static short requestNum = 1;
    private short tml;
    private ArrayList<Short> userInput;
    private byte[] rawBytes;

    public Request (ArrayList<Short> userRequest) {
        this.userInput = userRequest;
        this.rawBytes = createByteArray(userInput);
    }

    private byte[] createByteArray(ArrayList<Short> userInput) {
        short msgLength = (short) userInput.size();
        tml = (short) (Short.BYTES + Short.BYTES + (msgLength * Short.BYTES));
        ByteBuffer buffer = ByteBuffer.allocate(tml);
        buffer.putShort(tml);
        buffer.putShort(requestNum++);

        for (short value : userInput) {
            buffer.putShort(value);
        }
        return buffer.array();
    }

    public byte[] getBytes() {
        return this.rawBytes;
    }

}
