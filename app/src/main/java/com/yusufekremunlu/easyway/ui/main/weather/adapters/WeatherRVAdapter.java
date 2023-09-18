package com.yusufekremunlu.easyway.ui.main.weather.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.yusufekremunlu.easyway.R;
import com.yusufekremunlu.easyway.model.entity.weather.WeatherRVModel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.WeatherViewHolder> {
    private final List<WeatherRVModel> weatherRVModelList;
    private final Context context;

    public WeatherRVAdapter(List<WeatherRVModel> weatherRVModelList, Context context) {
        this.weatherRVModelList = weatherRVModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weather_rv_item, parent, false);
        return new WeatherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.WeatherViewHolder holder, int position) {
        WeatherRVModel weather = weatherRVModelList.get(position);
        holder.bind(weather);
    }

    @Override
    public int getItemCount() {
        return weatherRVModelList.size();
    }

    public class WeatherViewHolder extends RecyclerView.ViewHolder {
        TextView time, temperature, windSpeed;
        ImageView conditionIcon;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.idIVTime);
            temperature = itemView.findViewById(R.id.idIVTemperature);
            windSpeed = itemView.findViewById(R.id.idIVWindSpeed);
            conditionIcon = itemView.findViewById(R.id.idIVCondition);
        }

        public void bind(WeatherRVModel weatherRVModel) {
            temperature.setText(weatherRVModel.getTemperature() + "°C");
            windSpeed.setText(weatherRVModel.getWindSpeed() + "Km/h");
            Picasso.get().load("http:".concat(weatherRVModel.getIcon())).into(conditionIcon);
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
            SimpleDateFormat output = new SimpleDateFormat("hh:mm aa");
            try {
                Date t = input.parse(weatherRVModel.getTime());
                time.setText(output.format(t));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
}


