package com.gb.androidapponjava.view;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.androidapponjava.databinding.ItemWeatherHistoryBinding;
import com.gb.androidapponjava.model.WeatherHistory;

import java.text.SimpleDateFormat;

class WeatherHistoryAdapter extends ListAdapter<WeatherHistory, WeatherHistoryAdapter.WeatherHistoryViewHolder> {

    public WeatherHistoryAdapter(DiffUtil.ItemCallback<WeatherHistory> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public WeatherHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHistoryViewHolder(ItemWeatherHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHistoryViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public static class WeatherHistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemWeatherHistoryBinding binding;

        public WeatherHistoryViewHolder(ItemWeatherHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(WeatherHistory current) {
            binding.weatherHistoryCity.setText(current.getCity());
            binding.weatherHistoryTemperature.setText(String.valueOf(current.getTemperature()));
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            binding.weatherHistoryDate.setText(df.format(current.getDate() * 1000));
        }

    }

    public static class WeatherHistoryDiff extends DiffUtil.ItemCallback<WeatherHistory> {
        @Override
        public boolean areItemsTheSame(@NonNull WeatherHistory oldItem, @NonNull WeatherHistory newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull WeatherHistory oldItem, @NonNull WeatherHistory newItem) {
            return oldItem.getDate() == newItem.getDate();
        }
    }
}
