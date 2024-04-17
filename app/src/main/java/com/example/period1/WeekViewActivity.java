package com.example.period1;

import static com.example.period1.CalendarUtils.daysInWeekArray;
import static com.example.period1.CalendarUtils.monthYearFromDate;
import static com.example.period1.CalendarUtils.selectedDate;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeekViewActivity extends AppCompatActivity implements CalendarAdapter.OnItemListener
{
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private TextView monthYearText;
    private RecyclerView calendarRecyclerView;
    private ListView eventListView;
    private TextView temperatureTextView;
    private TextView tipsTextView;
    private WeatherAPI weatherAPI;
    private CalendarAdapter calendarAdapter;
    private FusedLocationProviderClient fusedLocationClient;
    private WeatherTips weatherTips;

    private ArrayList<LocalDate> selectedPeriodDates = new ArrayList<>();

    private ViewPager2 viewPager;
    private OkHttpClient client;
    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week_view);
        initWidgets();
        setWeekView();

        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Make a network request to the API
        String url = "https://newsapi.org/v2/everything?q=menstruation&language=en&apiKey=abe00b4b0c9c485dbd4d5e5d14e32248";
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();

                NewsResponse newsResponse = gson.fromJson(json, NewsResponse.class);
                List<News> newsList = newsResponse.getArticles();

                runOnUiThread(() -> {
                    NewsAdapter newsAdapter = new NewsAdapter(newsList);
                    viewPager.setAdapter(newsAdapter);
                });
                System.out.println("API Worked");
            }

            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                // Handle the error
                System.out.println("Something wrong");
            }
        });

        // Set OnClickListener for the "Log Period" button
        Button btnLogPeriod = findViewById(R.id.btnLogPeriod);
        btnLogPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logPeriodAction(v);
            }
        });

        // Initialize Weather API and tips
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherAPI = retrofit.create(WeatherAPI.class);
        weatherTips = new WeatherTips();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        requestLocation();
    }

    private void initWidgets()
    {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        monthYearText = findViewById(R.id.monthYearTV);
        eventListView = findViewById(R.id.eventListView);
        temperatureTextView = findViewById(R.id.temperatureTextView);
        tipsTextView = findViewById(R.id.tipsTextView);

        ImageView menuIcon = findViewById(R.id.menu_icon);
        menuIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the PeriodInputActivity
                startActivity(new Intent(WeekViewActivity.this, PeriodInputActivity.class));
            }
        });

        ImageView calendarIcon = findViewById(R.id.calendar_icon);
        calendarIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the PeriodInputActivity
                startActivity(new Intent(WeekViewActivity.this, MainActivity.class));
            }
        });
    }

    private void setWeekView() {
        monthYearText.setText(monthYearFromDate(CalendarUtils.selectedDate));
        ArrayList<LocalDate> days = daysInWeekArray(CalendarUtils.selectedDate);

        // Initialize calendarAdapter
        calendarAdapter = new CalendarAdapter(days, this);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 7);
        calendarRecyclerView.setLayoutManager(layoutManager);
        calendarRecyclerView.setAdapter(calendarAdapter);
        setEventAdpater();
    }

    public void previousWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.minusWeeks(1);
        setWeekView();
    }

    public void nextWeekAction(View view)
    {
        CalendarUtils.selectedDate = CalendarUtils.selectedDate.plusWeeks(1);
        setWeekView();
    }

    @Override
    public void onItemClick(int position, LocalDate date) {
        CalendarUtils.selectedDate = date;
        setWeekView();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        setEventAdpater();
    }

    private void setEventAdpater()
    {
        ArrayList<Event> dailyEvents = Event.eventsForDate(CalendarUtils.selectedDate);
        EventAdapter eventAdapter = new EventAdapter(getApplicationContext(), dailyEvents);
        eventListView.setAdapter(eventAdapter);
    }

    public void newEventAction(View view)
    {
        startActivity(new Intent(this, EventEditActivity.class));
    }

    public void logPeriodAction(View view) {
        selectedPeriodDates.clear();
        LocalDate startDate = CalendarUtils.selectedDate;
       LocalDate firstPeriodDate = null;

        for (int i = 0; i < 5; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            selectedPeriodDates.add(currentDate);

            // Create events for each selected period date
            Event newEvent = new Event("Period Day " + (i + 1) + "" , currentDate, LocalTime.now());
            Event.eventsList.add(newEvent);

            if (i == 0) {
                firstPeriodDate = currentDate;
            }
        }

        if (firstPeriodDate != null) {
            // Calculate the difference in days between the selected date and the first day of the period
            long daysDifference = java.time.temporal.ChronoUnit.DAYS.between(CalendarUtils.selectedDate, firstPeriodDate);
            String upcomingDateString;
            if (daysDifference > 0) {
                upcomingDateString = "Period in " + daysDifference + " days";
            } else if (daysDifference < 0) {
                upcomingDateString = "Period started " + Math.abs(daysDifference) + " days ago";
            } else {
                upcomingDateString = "Period today";
            }

            // Get the TextView and set its text to the calculated string
            TextView upcomingDateTextView = findViewById(R.id.upcomingDate);
            upcomingDateTextView.setText(upcomingDateString);
        }

        calendarAdapter.highlightPeriod(selectedPeriodDates);
    }

    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, location -> {
                        if (location != null) {
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            String apiKey = "9095ab83fa1c195c59549467800bfaf6"; // Replace with your API key
                            getCurrentWeather(latitude, longitude, apiKey);
                        } else {
                            Toast.makeText(WeekViewActivity.this, "Failed to retrieve location", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MainActivity.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentWeather(double latitude, double longitude, String apiKey) {
        Call<WeatherResponse> call = weatherAPI.getCurrentWeather(latitude, longitude, apiKey);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherInfo weatherInfo = response.body().getMain();
                    double temperatureKelvin = weatherInfo.getTemp();
                    double temperatureCelsius = temperatureKelvin - 273.15; // Conversion
                    temperatureTextView.setText(String.format("Temperature: %.1f °C", temperatureCelsius));

                    // Determine the temperature category and display a random tip
                    String tip = weatherTips.getRandomTip(temperatureCelsius);
                    tipsTextView.setText(tip);
                } else {
                    temperatureTextView.setText("Failed to fetch weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                temperatureTextView.setText("Error: " + t.getMessage());
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MainActivity.LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                requestLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
