package com.yusufekremunlu.easyway.ui.main.weather;

import static android.content.ContentValues.TAG;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.weather.WeatherRVModel;
import com.yusufekremunlu.easyway.ui.main.weather.adapters.WeatherRVAdapter;
import com.yusufekremunlu.easyway.utils.Credentials;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class WeatherFragment extends Fragment {
    private ArrayList<WeatherRVModel> weatherRVModelArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private TextView cityNameIV;
    private RelativeLayout homeRL;
    private TextInputEditText cityEdt;
    private TextView temperatureIV;
    private ImageView iconIV;
    private TextView conditionIV;
    private ImageView backIV;
    Button refreshButton;
    private final ActivityResultLauncher<String> mPermissionResult = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            result -> {
                if (result) {
                    Log.e(TAG, "onActivityResult: PERMISSION GRANTED");
                } else {
                    Log.e(TAG, "onActivityResult: PERMISSION DENIED");
                }
            });

    @SuppressLint("CutPasteId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        RecyclerView weatherRV = view.findViewById(R.id.idRvWeather);
        backIV = view.findViewById(R.id.idIVBack);
        cityEdt = view.findViewById(R.id.idEdtCity);
        conditionIV = view.findViewById(R.id.idTVCondition);
        iconIV = view.findViewById(R.id.idIVIcon);
        ImageView searchIV = view.findViewById(R.id.idIVSearch);
        cityNameIV = view.findViewById(R.id.idTVCityName);
        homeRL = view.findViewById(R.id.idRLHome);
        temperatureIV = view.findViewById(R.id.idTVTemperature);

        weatherRVModelArrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(weatherRVModelArrayList, getContext());
        weatherRV.setAdapter(weatherRVAdapter);

        Button enableLocationBtn = view.findViewById(R.id.idBtnEnableLocation);

        enableLocationBtn.setOnClickListener(v -> {
            if (checkLocationPermissions()) {

            } else {
                requestLocationPermissions();
            }
        });

        locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);

        if (checkLocationPermissions()) {
            getLocation();
        } else {
            enableLocationBtn.setVisibility(View.VISIBLE);
        }

        searchIV.setOnClickListener(v -> {
            String city = Objects.requireNonNull(cityEdt.getText()).toString();
            if (city.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter city Name", Toast.LENGTH_SHORT).show();
            } else {
                cityNameIV.setText(city);
                getWeatherInfo(city);
            }
        });
        refreshButton = view.findViewById(R.id.idBtnEnableLocation);
        refreshButton.setOnClickListener(v -> {
            refresh();
        });
        return view;
    }

    private boolean checkLocationPermissions() {
        return ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermissions() {
        mPermissionResult.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (location != null) {
            String cityName = getCityName(location.getLongitude(), location.getLatitude());
            getWeatherInfo(cityName);
        } else {
            Toast.makeText(requireContext(), "Location information could not be obtained. Please enable location services and press the refresh button", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCityName(double longitude, double latitude) {
        String cityName = "Not found";
        Geocoder gcd = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = gcd.getFromLocation(latitude, longitude, 10);
            for (Address adr : addresses) {
                if (adr != null) {
                    String city = adr.getLocality();
                    if (city != null && !city.equals("")) {
                        cityName = city;
                    } else {
                        Log.d("TAG", "CITY NOT FOUND");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    private void getWeatherInfo(String cityName) {
        String url = Credentials.WEATHER_BASE_API_URL + "/" + Credentials.WEATHER_API_VERSION + "/forecast.json?key=" + Credentials.WEATHER_API_KEY + "&q=" + cityName + "&days=1&aqi=yes&alerts=yes";
        cityNameIV.setText(cityName);
        RequestQueue requestQueue = Volley.newRequestQueue(requireContext());

        @SuppressLint({"SetTextI18n", "NotifyDataSetChanged"}) JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            homeRL.setVisibility(View.VISIBLE);
            weatherRVModelArrayList.clear();
            try {
                String temperature = response.getJSONObject("current").getString("temp_c");
                temperatureIV.setText(temperature + "°C");
                int isDay = response.getJSONObject("current").getInt("is_day");
                String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                String conditionIcon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                Picasso.get().load("http:".concat(conditionIcon)).into(iconIV);
                conditionIV.setText(condition);
                if (isDay == 1) {
                    Picasso.get().load("https://images.unsplash.com/photo-1558486012-817176f84c6d?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1340&q=80").into(backIV);
                } else {
                    Picasso.get().load("https://images.unsplash.com/photo-1505322022379-7c3353ee6291?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1500&q=80").into(backIV);
                }

                JSONObject forecastObj = response.getJSONObject("forecast");
                JSONObject forecastO = forecastObj.getJSONArray("forecastday").getJSONObject(0);
                JSONArray hourArray = forecastO.getJSONArray("hour");

                for (int i = 0; i < hourArray.length(); i++) {
                    JSONObject hourObj = hourArray.getJSONObject(i);
                    String time = hourObj.getString("time");
                    String temper = hourObj.getString("temp_c");
                    String img = hourObj.getJSONObject("condition").getString("icon");
                    String wind = hourObj.getString("wind_kph");
                    weatherRVModelArrayList.add(new WeatherRVModel(time, temper, img, wind));
                }
                weatherRVAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(requireContext(), "Please enter a valid city name.", Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonObjectRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(checkLocationPermissions()){
            getLocation();
        } else {
            requestLocationPermissions();
        }
    }
    public void refresh(){
        if(checkLocationPermissions()){
            getLocation();
        } else {
            requestLocationPermissions();
        }
    }
}