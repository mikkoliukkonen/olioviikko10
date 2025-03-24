package com.example.viikko10;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnSearch;
    private Button btnShowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSearch = findViewById(R.id.buttonSearch);
        btnShowList = findViewById(R.id.buttonList);

        // Asetetaan nappeihin kuuntelijat siirtymään toisiin aktiviteetteihin
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Siirry SearchActivity-näkymään
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Siirry ListInfoActivity-näkymään
                Intent intent = new Intent(MainActivity.this, ListInfoActivity.class);
                startActivity(intent);
            }
        });
    }
}
