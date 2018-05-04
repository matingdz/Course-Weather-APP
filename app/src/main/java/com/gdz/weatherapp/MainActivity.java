package com.gdz.weatherapp;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    EditText editTextCityName;
    Button buttonSearch;
    String cityName;
    String mainForcast;
    int cityTemp;
    int humidity;
    int minTemp, maxTemp;
    int degree;
    int speed;
    RelativeLayout relativeLayoutForecast,relativeLayoutHumidity;
    TextView cityForecast,cityHumidity, minTempTextView, maxTempTextView, windSpeed, windDegree;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        sharedPreferences = getSharedPreferences("prefs",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editTextCityName = findViewById(R.id.city_name);
        editTextCityName.setText(sharedPreferences.getString("city_name",null));
        buttonSearch = findViewById(R.id.search_btn);
        relativeLayoutForecast = findViewById(R.id.forcast_relative_layout);
        relativeLayoutHumidity = findViewById(R.id.humidity_relative_layout);
        cityForecast = findViewById(R.id.city_forcast);
        cityHumidity = findViewById(R.id.city_humidity);
        minTempTextView = findViewById(R.id.min_temp);
        maxTempTextView = findViewById(R.id.max_temp);
        windDegree = findViewById(R.id.wind_degree);
        windSpeed = findViewById(R.id.wind_speed);
        cityForecast.setText(sharedPreferences.getString("main",null));
        cityHumidity.setText(String.valueOf(sharedPreferences.getInt("humidity",0)+"%"));
        minTempTextView.setText(String.valueOf(sharedPreferences.getInt("min_temp",0)));
        maxTempTextView.setText(String.valueOf(sharedPreferences.getInt("max_temp",0)));
        windSpeed.setText(String.valueOf(sharedPreferences.getInt("wind_speed",0)));
        windDegree.setText(String.valueOf(sharedPreferences.getInt("wind_degree",0)));

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();

            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_camera) {
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void getInfo(){


        cityName = editTextCityName.getText().toString();
        if (cityName.isEmpty()){
            Toast.makeText(MainActivity.this,"Please Type a City Name",Toast.LENGTH_LONG).show();
        }
        else {

            editor.putString("city_name",cityName);
            editor.commit();


            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    "http://api.openweathermap.org/data/2.5/weather?q="+cityName+"&appid=42332c5e63fba421b1553e2409fb39aa",
                    null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    try {
                        JSONArray jsonArray = response.getJSONArray("weather");
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        mainForcast = jsonObject.getString("main");
                        JSONObject jsonObjectTemp = response.getJSONObject("main");
                        cityTemp = jsonObjectTemp.getInt("temp");
                        humidity = jsonObjectTemp.getInt("humidity");
                        minTemp = jsonObjectTemp.getInt("temp_min");
                        maxTemp = jsonObjectTemp.getInt("temp_max");
                        JSONObject jsonObjectWind = response.getJSONObject("wind");
                        speed = jsonObjectWind.getInt("speed");
                        degree = jsonObjectWind.getInt("deg");
                        cityForecast.setText(mainForcast);
                        cityHumidity.setText(String.valueOf(humidity + "%"));
                        minTempTextView.setText(String.valueOf(minTemp - 273));
                        maxTempTextView.setText(String.valueOf(maxTemp - 273));
                        windDegree.setText(String.valueOf(degree));
                        windSpeed.setText(String.valueOf(speed));

                        if(mainForcast.equals("Clear")){
                            Drawable drawableSunnyWeather = getResources().getDrawable(R.drawable.sun);
                            relativeLayoutForecast.setBackground(drawableSunnyWeather);
                        }
                        if(mainForcast.equals("Clouds")){
                            Drawable drawableCloudyWeather = getResources().getDrawable(R.drawable.cloud);
                            relativeLayoutForecast.setBackground(drawableCloudyWeather);
                        }
                        if(humidity<=50){
                            Drawable drawableDryWeather = getResources().getDrawable(R.drawable.dry);
                            relativeLayoutHumidity.setBackground(drawableDryWeather);
                        }
                        if(humidity>=51){
                            Drawable drawableHumidWeather = getResources().getDrawable(R.drawable.humid);
                            relativeLayoutHumidity.setBackground(drawableHumidWeather);
                        }

                        editor.putString("main",mainForcast);
                        editor.putInt("humidity",humidity);
                        editor.putInt("min_temp",minTemp - 273);
                        editor.putInt("max_temp",maxTemp - 273);
                        editor.putInt("wind_speed",speed);
                        editor.putInt("wind_degree",degree);
                        editor.commit();

                    } catch (JSONException e) {


                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();

                }
            });

            RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
            requestQueue.add(jsonObjectRequest);

        }

    }
    }

