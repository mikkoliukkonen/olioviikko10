package com.example.viikko10;

import java.util.ArrayList;
import java.util.List;

public class CarDataStorage {
    private static String city;
    private static int year;
    private static ArrayList<CarData> carData = new ArrayList<>();

    // Singleton-tyylinen metodi, jos joskus haluat käyttää sitä
    public static CarDataStorage getInstance() {
        return new CarDataStorage(); // ei oikeaa singletonia tässä, mutta säilytetty metodin nimi
    }

    // Palauttaa ajoneuvotietolistan
    public static ArrayList<CarData> getCarData() {
        return carData;
    }

    // Lisää yksittäinen CarData-olio listaan
    public static void addCarData(CarData data) {
        carData.add(data);
    }

    // Aseta kaupungin nimi
    public static void setCity(String cityName) {
        city = cityName;
    }

    // Aseta vuosi
    public static void setYear(int yearValue) {
        year = yearValue;
    }

    // Palauttaa kaupungin nimen
    public static String getCity() {
        return city;
    }

    // Palauttaa vuoden
    public static int getYear() {
        return year;
    }

    // Tyhjennä kaikki tallennetut tiedot
    public static void clearData() {
        city = null;
        year = 0;
        carData.clear();
    }
}


