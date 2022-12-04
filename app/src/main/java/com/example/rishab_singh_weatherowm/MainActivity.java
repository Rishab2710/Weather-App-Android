package com.example.rishab_singh_weatherowm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button button;
    ImageView imageView;
    TextView tempTv, time, longitude, latitude, humidity, sunrise, sunset, pressure;
    TextView wind, country, city_name, max_temp, min_temp, feels, descriptionBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editTextTextPersonName);
        button = findViewById(R.id.button);
        imageView = findViewById(R.id.imageView);
        tempTv = findViewById(R.id.textView3);
        time = findViewById(R.id.textView2);

        longitude = findViewById(R.id.longitude);
        latitude = findViewById(R.id.latitude);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        pressure = findViewById(R.id.pressure);
        wind = findViewById(R.id.wind);
        country = findViewById(R.id.country);
        city_name = findViewById(R.id.city_nam);
        max_temp = findViewById(R.id.temp_max);
        min_temp = findViewById(R.id.min_temp);
        feels = findViewById(R.id.feels);
        descriptionBox = findViewById(R.id.description);

        button.setOnClickListener(v -> FindWeather());

    }

    public void FindWeather(){
        final String city = editText.getText().toString();
        String url ="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=98c850c1b91d8632aff1862eec1a4b81";
        StringRequest stringRequest = new StringRequest(Request.Method.GET,url, response -> {
            try {

                //find temperature
                JSONObject jsonObject = new JSONObject(response);
                JSONObject object = jsonObject.getJSONObject("main");
                double temp = object.getDouble("temp") - 273.15;
                tempTv.setText("Temperature\n"+ String.format("%.2f", temp)+"°C");


                //find country
                JSONObject object8 = jsonObject.getJSONObject("sys");
                String country1 = object8.getString("country");
                country.setText(country1+"  :");

                //find city
                String city1 = jsonObject.getString("name");
                city_name.setText(city1);

                //find icon
                JSONArray jsonArray = jsonObject.getJSONArray("weather");
                JSONObject obj = jsonArray.getJSONObject(0);
                String icon = obj.getString("icon");
                Picasso.get().load("http://openweathermap.org/img/wn/"+icon+"@2x.png").into(imageView);

                //Description
                JSONObject jsonObjectDescription = jsonArray.getJSONObject(0);
                String description1 = jsonObjectDescription.getString("description");
                descriptionBox.setText("Description  "+ description1);


                //find date & time.. gets the system date and time not the locations
                //Since API does not give date and time of location except UTC timestamp
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat std = new SimpleDateFormat("HH:mm a \nE, MMM dd yyyy");
                String date = std.format(calendar.getTime());
                time.setText(date);


                //find latitude
                JSONObject object2 = jsonObject.getJSONObject("coord");
                double lat_find = object2.getDouble("lat");
                latitude.setText(lat_find+"°  N");

                //find longitude
                JSONObject object3 = jsonObject.getJSONObject("coord");
                double long_find = object3.getDouble("lon");
                longitude.setText(long_find+"°  E");

                //find humidity
                JSONObject object4 = jsonObject.getJSONObject("main");
                int humidity_find = object4.getInt("humidity");
                humidity.setText(humidity_find+"  %");

                //find sunrise
                JSONObject object5 = jsonObject.getJSONObject("sys");
                String sunrise_find = object5.getString("sunrise");
                sunrise.setText(sunrise_find+"  am");

                //find sunset
                JSONObject object6 = jsonObject.getJSONObject("sys");
                String sunset_find = object6.getString("sunset");
                sunset.setText(sunset_find+"  pm");

                //find pressure
                JSONObject object7 = jsonObject.getJSONObject("main");
                String pressure_find = object7.getString("pressure");
                pressure.setText(pressure_find+"  hPa");

                //find wind speed
                JSONObject object9 = jsonObject.getJSONObject("wind");
                String wind_find = object9.getString("speed");
                wind.setText(wind_find+"  km/h");

                //find min temperature
                JSONObject object10 = jsonObject.getJSONObject("main");
                double minTemp = object10.getDouble("temp_min")-273.15;
                min_temp.setText("Min Temp\n"+String.format("%.2f", minTemp)+" °C");

                //find max temperature
                JSONObject object12 = jsonObject.getJSONObject("main");
                double maxTemp = object12.getDouble("temp_max")-273.15;
                max_temp.setText("Max Temp\n"+String.format("%.2f", maxTemp)+" °C");

                //find feels
                JSONObject object13 = jsonObject.getJSONObject("main");
                double feels_find = object13.getDouble("feels_like")-273.15;
                feels.setText(String.format("%.2f", feels_find)+" °C");


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(stringRequest);
    }

}