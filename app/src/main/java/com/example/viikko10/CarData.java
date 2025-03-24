package com.example.viikko10;

public class CarData {
    private String type;
    private int amount;

    public CarData(String type, int amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return type + ": " + amount;
    }
}



