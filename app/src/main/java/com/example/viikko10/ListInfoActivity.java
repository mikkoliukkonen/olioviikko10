package com.example.viikko10;



import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;
import android.widget.ArrayAdapter;

public class ListInfoActivity extends AppCompatActivity {

    private TextView textHeader;
    private ListView listView;
    private TextView textTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_info);

        textHeader = findViewById(R.id.textViewHeader);
        listView = findViewById(R.id.listViewData);
        textTotal = findViewById(R.id.textViewTotal);

        // Noudetaan tallennetut tiedot
        String city = CarDataStorage.getCity();
        int year = CarDataStorage.getYear();
        List<CarData> dataList = CarDataStorage.getDataList();

        if (city == null || dataList == null || dataList.isEmpty()) {
            // Ei tietoja - näytetään tyhjälle listalle ilmoitus
            textHeader.setText("Ei näytettäviä tietoja");
            textTotal.setText("");
        } else {
            // Asetetaan otsikko "Kaupunki – Vuosi"
            textHeader.setText(city + " – " + year);
            // Asetetaan CarData-lista listView:hen
            ArrayAdapter<CarData> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, dataList);
            listView.setAdapter(adapter);
            // Lasketaan kokonaismäärä (summa)
            int totalCount = 0;
            for (CarData cd : dataList) {
                totalCount += cd.getCount();
            }
            textTotal.setText("Yhteensä: " + totalCount);
        }
    }
}
