package com.example.weatherforecast;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.ViewHolder> {
    private Context context;
    private ArrayList<WeatherModel> weatherModelArrayList;

    public WeatherAdapter(Context context, ArrayList<WeatherModel> weatherModelArrayList) {
        this.context = context;
        this.weatherModelArrayList = weatherModelArrayList;
    }

    @NonNull
    @Override
    public WeatherAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.weather_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WeatherModel model = weatherModelArrayList.get(position);
        holder.tempShow.setText(model.getTemp() + " C");
        Picasso.get().load("http:".concat(model.getIcon())).into(holder.icon);
        holder.humidity.setText(model.getHumidity() + " humid");
        SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        SimpleDateFormat processedInput = new SimpleDateFormat("hh:mm aa");
        try{
            Date date = input.parse(model.getTime());
            holder.time.setText(processedInput.format(date));
        } catch (ParseException e){
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return weatherModelArrayList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {
        private TextView time, tempShow, humidity;
        private ImageButton icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.idTime);
            tempShow = itemView.findViewById(R.id.idTempShow);
            humidity = itemView.findViewById(R.id.idHumidity);
            icon = itemView.findViewById(R.id.idIcon);

        }
    }
}
