package com.cpsc3350.networks.model;

public class Request {
    private int request = 0;
    private int tml;
    private int code;
    private int quantity;
    private byte[] incFrame;

    public Request (byte[] request) {
        this.incFrame = request;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
