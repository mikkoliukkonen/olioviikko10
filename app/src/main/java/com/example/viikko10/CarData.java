package com.example.viikko10;



public class CarData {
    private String type;   // Ajoneuvoluokan nimi
    private int count;     // Lukumäärä

    public CarData(String type, int count) {
        this.type = type;
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public int getCount() {
        return count;
    }

    @Override
    public String toString() {
        // Palautetaan merkkijono muodossa "Tyyppi: lukumäärä"
        return type + ": " + count;
    }
}


