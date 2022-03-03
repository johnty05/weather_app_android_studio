package com.example.weatherforecast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout home;
    private ProgressBar progressBar;
    private TextView cityName, temp, weather;
    private TextInputEditText editCityName;
    private ImageView bgImage, symbol, search;
    private RecyclerView forecast;
    private ArrayList<WeatherModel> weatherModelArrayList;
    private WeatherAdapter weatherAdapter;

    // permission and loc manager
    private LocationManager locManager;
    private int PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for fullscreen app
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

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
        weatherModelArrayList = new ArrayList<>();
        weatherAdapter = new WeatherAdapter(this, weatherModelArrayList);
        forecast.setAdapter(weatherAdapter);

        // initialize loc manager
        // check for perms grant
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    }

    private void getWeatherInfo(String cityName) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=3253604179ee4a05868115644220103&q=" + cityName + "&days=1&aqi=no&alerts=no";
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "SORRY NO SUCH CITY FOUND";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
    }
}