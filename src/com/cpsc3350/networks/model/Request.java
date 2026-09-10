package com.cpsc3350.networks.model;

public class Request {
    private int request = 0;
    private int tml;
    private int code;
    private int quantity;

    public Request (int code, int quantity) {
        this.code = code;
        this.quantity = quantity;
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
