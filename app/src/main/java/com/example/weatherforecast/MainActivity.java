package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout home;
    private ProgressBar progressBar;
    private TextView cityName, temp, weather;
    private TextInputEditText editCityName;
    private ImageView bgImage, symbol, search;
    private RecyclerView forecast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        home = findViewById(R.id.idHome);
        progressBar = findViewById(R.id.idProgressBar);
        cityName = findViewById(R.id.idCityName);
        temp = findViewById(R.id.idTemp);
        weather = findViewById(R.id.idWeather);
        editCityName = findViewById(R.id.idEditCityName);
        bgImage = findViewById(R.id.idBgImage);
        symbol = findViewById(R.id.idSymbol);
        search = findViewById(R.id.idSearch);
        forecast = findViewById(R.id.idForecast);
    }
}