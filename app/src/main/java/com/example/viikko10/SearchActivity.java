package com.example.viikko10;



import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class SearchActivity extends AppCompatActivity {

    private EditText editCity;
    private EditText editYear;
    private Button btnFetch;
    private TextView textMessage;

    // StatFin API:n URL taululle 11ic (ajoneuvokanta alueittain)
    private static final String API_URL = "https://pxdata.stat.fi/PxWeb/api/v1/fi/StatFin/mkan/statfin_mkan_pxt_11ic.px";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        editCity = findViewById(R.id.editTextCity);
        editYear = findViewById(R.id.editTextYear);
        btnFetch = findViewById(R.id.buttonFetch);
        textMessage = findViewById(R.id.textViewMessage);

        // Asetetaan painikkeelle toiminnallisuus
        btnFetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cityName = editCity.getText().toString().trim();
                String yearStr = editYear.getText().toString().trim();

                // Syötteen validointi
                if (cityName.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Anna kaupungin nimi", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (yearStr.isEmpty()) {
                    Toast.makeText(SearchActivity.this, "Anna vuosi", Toast.LENGTH_SHORT).show();
                    return;
                }
                int yearValue;
                try {
                    yearValue = Integer.parseInt(yearStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(SearchActivity.this, "Vuosi on virheellinen", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Käynnistetään haku asynkronisesti (AsyncTask)
                new FetchDataTask(cityName, yearValue).execute();
            }
        });
    }

    // Sisäinen AsyncTask-luokka verkkohaun suorittamiseen taustalla
    private class FetchDataTask extends AsyncTask<Void, Void, Boolean> {
        private String city;
        private int year;
        private String errorMessage = null;

        public FetchDataTask(String cityName, int yearValue) {
            this.city = cityName;
            this.year = yearValue;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Tyhjennetään mahdollinen vanha viesti ja näytetään "haetaan..."
            textMessage.setText("Haetaan tietoja...");
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            HttpURLConnection connection = null;
            try {
                // Muodostetaan JSON-kysely merkkijonona
                String jsonQuery = "{"
                        + "\"query\":["
                        + "{\"code\":\"Alue\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + city + "\"]}},"
                        + "{\"code\":\"Ajoneuvoluokka\",\"selection\":{\"filter\":\"item\",\"values\":[\"01\",\"02\",\"03\",\"04\",\"05\"]}},"
                        + "{\"code\":\"Liikennekäyttö\",\"selection\":{\"filter\":\"item\",\"values\":[\"0\"]}},"
                        + "{\"code\":\"Vuosi\",\"selection\":{\"filter\":\"item\",\"values\":[\"" + year + "\"]}}"
                        + "],"
                        + "\"response\":{\"format\":\"json\"}"
                        + "}";

                // Avaa yhteys
                URL url = new URL(API_URL);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Lähetetään JSON-kysely datana
                OutputStream os = connection.getOutputStream();
                os.write(jsonQuery.getBytes("UTF-8"));
                os.close();

                int responseCode = connection.getResponseCode();
                if (responseCode != 200) {
                    // Mikäli HTTP-vastauskoodi ei ole 200 (OK), käsitellään virheenä
                    errorMessage = "Hakupyynnön vastauskoodi: " + responseCode;
                    return false;
                }

                // Luetaan vastaus
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                StringBuilder responseStr = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    responseStr.append(line);
                }
                br.close();
                String jsonResponse = responseStr.toString();

                // Parsitaan JSON-vastaus
                JSONObject json = new JSONObject(jsonResponse);
                if (!json.has("data")) {
                    errorMessage = "Virhe: dataa ei löytynyt";
                    return false;
                }
                JSONArray dataArray = json.getJSONArray("data");
                if (dataArray.length() == 0) {
                    errorMessage = "Ei tietoja alueelle \"" + city + "\" vuodelle " + year;
                    return false;
                }

                // Muodostetaan lista CarData-olioita vastauksen perusteella
                List<CarData> carDataList = new ArrayList<>();
                for (int i = 0; i < dataArray.length(); i++) {
                    JSONObject dataObj = dataArray.getJSONObject(i);
                    // "key" taulukko sisältää [Alue, Ajoneuvoluokka, Liikennekäyttö, Vuosi, Tiedot]
                    JSONArray keyArray = dataObj.getJSONArray("key");
                    String classCode = keyArray.getString(1);  // Ajoneuvoluokan koodi (01-05)
                    JSONArray values = dataObj.getJSONArray("values");
                    // values[0] on lukumäärä kyseiselle yhdistelmälle
                    int count = values.isNull(0) ? 0 : values.getInt(0);
                    // Muunnetaan ajoneuvoluokan koodi nimeksi
                    String className = getVehicleClassName(classCode);
                    carDataList.add(new CarData(className, count));
                }

                // Tallennetaan haetut tiedot CarDataStorageen
                CarDataStorage.saveData(city, year, carDataList);
                return true;

            } catch (Exception e) {
                errorMessage = "Haku epäonnistui: " + e.getMessage();
                return false;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            super.onPostExecute(success);
            if (success) {
                // Ilmoitetaan käyttäjälle onnistumisesta
                textMessage.setText("Tiedot haettu onnistuneesti!");
            } else {
                // Ilmoitetaan käyttäjälle virheestä
                textMessage.setText(errorMessage != null ? errorMessage : "Tuntematon virhe haussa");
                // Tyhjennetään mahdollisesti vanhat tiedot tallennuksesta varmuuden vuoksi
                CarDataStorage.clear();
            }
        }

        // Apu: palautetaan ajoneuvoluokan koodia vastaava nimi (suomeksi)
        private String getVehicleClassName(String code) {
            switch (code) {
                case "01": return "Henkilöautot";
                case "02": return "Pakettiautot";
                case "03": return "Kuorma-autot";
                case "04": return "Linja-autot";
                case "05": return "Erikoisautot";
                default:   return code;
            }
        }
    }
}
