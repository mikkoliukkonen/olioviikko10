package com.example.viikko10;



import java.util.ArrayList;
import java.util.List;

public class CarDataStorage {
    private static String city = null;
    private static int year = 0;
    private static List<CarData> dataList = new ArrayList<>();

    // Tallenna uusi hakutulos
    public static void saveData(String cityName, int yearValue, List<CarData> carDataList) {
        city = cityName;
        year = yearValue;
        dataList.clear();
        if (carDataList != null) {
            dataList.addAll(carDataList);
        }
    }

    // Palauta tallennettu kaupunki
    public static String getCity() {
        return city;
    }

    // Palauta tallennettu vuosi
    public static int getYear() {
        return year;
    }

    // Palauta tallennettu CarData-lista
    public static List<CarData> getDataList() {
        return dataList;
    }

    // Tyhjennä tallennetut tiedot
    public static void clear() {
        city = null;
        year = 0;
        dataList.clear();
    }
}

