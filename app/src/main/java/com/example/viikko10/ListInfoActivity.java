package com.example.viikko10;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ListInfoActivity extends AppCompatActivity {

    private TextView CityText, YearText, CarInfoText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_info);

        CityText = findViewById(R.id.CityText);
        YearText = findViewById(R.id.YearText);
        CarInfoText = findViewById(R.id.CarInfoText);

        String city = CarDataStorage.getCity();
        int year = CarDataStorage.getYear();
        List<CarData> cars = CarDataStorage.getCarData();

        CityText.setText(city != null ? city : "Ei tietoa");
        YearText.setText(String.valueOf(year));

        StringBuilder builder = new StringBuilder();
        int total = 0;
        for (CarData car : cars) {
            builder.append(car.getType()).append(": ").append(car.getAmount()).append("\n");
            total += car.getAmount();
        }

        builder.append("Yhteensä: ").append(total);
        CarInfoText.setText(builder.toString());
    }
}

