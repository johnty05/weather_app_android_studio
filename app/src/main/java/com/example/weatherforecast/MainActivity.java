package com.example.weatherforecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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

    // for city name from lat and long
    private String cityNameFromLatLong;

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
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_CODE);
        }

        Location location = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        cityNameFromLatLong = getCityName(location.getLongitude(), location.getLatitude());
        getWeatherInfo(cityNameFromLatLong);

        // operation for the search icon
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = editCityName.getText().toString();
                if(city.isEmpty()){
                    Toast.makeText(MainActivity.this, "Please Enter City Name", Toast.LENGTH_SHORT).show();
                } else{
                    cityName.setText(cityNameFromLatLong);
                    getWeatherInfo(city);
                }
            }
        });
    }

    private void getWeatherInfo(String city) {
        String url = "http://api.weatherapi.com/v1/forecast.json?key=3253604179ee4a05868115644220103&q=" + city + "&days=1&aqi=no&alerts=no";
        cityName.setText(city);
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                home.setVisibility(View.VISIBLE);
                // we get response from json object
                // we need to extract data from this object
                // we clear the recycler view arraylist


                // when user searches for multiple time for diff weather it wont be added to simple arraylist multiple times
                weatherModelArrayList.clear();
                // "current" "temp_c" is taken form the response - can be viewed in POSTMAN
                try {
                    String tempFromResponse = response.getJSONObject("current").getString("temp_c");
                    temp.setText(tempFromResponse+" C");
                    int isDayFromResponse = response.getJSONObject("current").getInt("is_day");
                    String conditionFromResponse = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String conditionIconFromResponse = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("http:".concat(conditionIconFromResponse)).into(symbol);
                    String iconFromResponse = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    weather.setText(conditionFromResponse);
                    if(isDayFromResponse == 1){
                        // image for daytime
                        Picasso.get().load("https://images.pexels.com/photos/1266810/pexels-photo-1266810.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").into(bgImage);
                    } else{
                        // image for night time or evening
                        Picasso.get().load("https://images.pexels.com/photos/1624438/pexels-photo-1624438.jpeg?auto=compress&cs=tinysrgb&dpr=2&h=650&w=940").into(bgImage);
                    }
                    JSONObject forecastObjectFromResponse = response.getJSONObject("forecast");
                    JSONObject forecastFirstObj = forecastObjectFromResponse.getJSONArray("forecastday").getJSONObject(0);
                    JSONArray hourArray = forecastFirstObj.getJSONArray("hour");

                    for(int i=0; i<hourArray.length(); i++){
                        JSONObject hourObj = hourArray.getJSONObject(i);
                        String time = hourObj.getString("time");
                        String tem = hourObj.getString("temp_c");
                        String img = hourObj.getJSONObject("condition").getString("icon");
                        String humid = hourObj.getString("humidity");
                        weatherModelArrayList.add(new WeatherModel(time, tem, img, humid));
                    }
                    weatherAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Enter a valid City Name!!", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    private String getCityName(double longitude, double latitude) {
        String cName = "SORRY NO SUCH CITY FOUND";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for(Address ad: addresses) {
                if(ad!=null){
                    String city = ad.getLocality();
                    if(city!=null && !city.equals("")){
                        cName = city;
                    } else{
                        Log.d("TAG","CITY NOT FOUND");
                        // TODO - Learn the use of toast
                        Toast.makeText(this, "User City Not Found!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cName;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==PERMISSION_CODE){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "PERMISSION GRANTED", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "PLEASE GRANT PERMISSION", Toast.LENGTH_SHORT).show();
                // if user does not give perms then close the application
                finish();
            }
        }
    }
}