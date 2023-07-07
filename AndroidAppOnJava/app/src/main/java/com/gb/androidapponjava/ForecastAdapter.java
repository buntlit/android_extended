package com.gb.androidapponjava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder> {

    private List<String> days;
    private int index;
    private Context context;

    public ForecastAdapter(List<String> days, int index, Context context) {
        this.days = days;
        this.index = index;
        this.context = context;
    }

    @NonNull
    @Override
    public ForecastViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ForecastViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ForecastViewHolder holder, int position) {
        if (position == 0) {
            holder.bind(days.get(position), context.getResources().getStringArray(R.array.temperatureTomorrow)[index]);
        } else {
            holder.bind(days.get(position), context.getResources().getStringArray(R.array.temperatureAfterTomorrow)[index]);
        }

    }


    @Override
    public int getItemCount() {
        return days.size();
    }

    static class ForecastViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewDay;
        private final TextView textViewValue;

        public ForecastViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.dayNameTextView);
            textViewValue = itemView.findViewById(R.id.dayValueTextview);
        }

        void bind(String dayName, String dayValue) {
            textViewDay.setText(dayName);
            textViewValue.setText(dayValue);
        }
    }
}
