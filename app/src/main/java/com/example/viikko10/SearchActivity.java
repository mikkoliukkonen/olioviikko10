package com.example.viikko10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    private EditText cityInput, yearInput;
    private TextView statusText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        cityInput = findViewById(R.id.CityNameEdit);
        yearInput = findViewById(R.id.YearEdit);
        searchButton = findViewById(R.id.SearchButton);
        statusText = findViewById(R.id.StatusText);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = cityInput.getText().toString().trim();
                String yearStr = yearInput.getText().toString().trim();

                if (city.isEmpty()) {
                    statusText.setText("Haku epäonnistui, kaupungin nimi puuttuu");
                    return;
                }

                int year;
                try {
                    year = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    statusText.setText("Haku epäonnistui, vuosi ei ole kelvollinen numero");
                    return;
                }

                // Simuloidaan hakua (oikeassa toteutuksessa kutsuisi getData-metodia)
                if (city.equalsIgnoreCase("Helsinki")) {
                    CarDataStorage.clearData();
                    CarDataStorage.setCity(city);
                    CarDataStorage.setYear(year);
                    CarDataStorage.addCarData(new CarData("Henkilöautot", 100000));
                    CarDataStorage.addCarData(new CarData("Pakettiautot", 20000));
                    CarDataStorage.addCarData(new CarData("Kuorma-autot", 5000));
                    CarDataStorage.addCarData(new CarData("Linja-autot", 1000));
                    CarDataStorage.addCarData(new CarData("Erikoisautot", 500));
                    statusText.setText("Haku onnistui");
                } else {
                    statusText.setText("Haku epäonnistui, kaupunkia ei olemassa tai se on kirjoitettu väärin.");
                }
            }
        });
    }
}

