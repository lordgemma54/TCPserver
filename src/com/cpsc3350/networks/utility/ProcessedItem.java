package com.cpsc3350.networks.utility;

public class ProcessedItem {

    String description;
    short quantity;
    short cost;

    public ProcessedItem(String description, short quantity, short cost) {
        this.description = description;
        this.quantity = quantity;
        this.cost = cost;
    }

    public String getDescription() {
        return description;
    }

    public short getQuantity() {
        return quantity;
    }

    public short getCost() {
        return cost;
    }

}
