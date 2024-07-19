package com.example.myweatherapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView tvCityName, tvTemperature, tvDescription;
    private ImageView ivWeatherIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvCityName = findViewById(R.id.tvCityName);
        tvTemperature = findViewById(R.id.tvTemperature);
        tvDescription = findViewById(R.id.tvDescription);
        ivWeatherIcon = findViewById(R.id.ivWeatherIcon);

        fetchWeatherData();
    }

    private void fetchWeatherData() {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.openweathermap.org/data/2.5/weather?q=Lahore&appid=f80c13b52bdd4202a0f14a47660a6854";

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    try {
                        JSONObject json = new JSONObject(responseData);
                        String cityName = json.getString("name");
                        JSONObject main = json.getJSONObject("main");
                        double temp = main.getDouble("temp") - 273.15; // Convert Kelvin to Celsius
                        JSONArray weatherArray = json.getJSONArray("weather");
                        JSONObject weather = weatherArray.getJSONObject(0);
                        String description = weather.getString("description");
                        String icon = weather.getString("icon");

                        runOnUiThread(() -> {
                            tvCityName.setText(cityName);
                            tvTemperature.setText(String.format("%.2f°C", temp));
                            tvDescription.setText(description);
                            // Load the weather icon using a library like Picasso or Glide
                            // Picasso.get().load("http://openweathermap.org/img/wn/" + icon + "@2x.png").into(ivWeatherIcon);
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
