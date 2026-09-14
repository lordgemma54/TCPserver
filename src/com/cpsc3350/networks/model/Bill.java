package com.cpsc3350.networks.model;

public class Bill {

    private int request = 0;
    private int tml;
    private int code;
    private int quantity;
    private String description;


    public Bill (byte[] clientRequest) {
        this.code = code;
        this.quantity = quantity;
    }

//    read in the csv
//    discard the tml and requestNum
//    match each code and quantity to lines in the file
//    calculate totals
//    create byte array to send back to server

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
