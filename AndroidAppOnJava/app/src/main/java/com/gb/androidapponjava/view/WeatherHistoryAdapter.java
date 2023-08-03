package com.gb.androidapponjava.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.androidapponjava.databinding.ItemWeatherHistoryBinding;

import java.util.List;

class WeatherHistoryAdapter extends RecyclerView.Adapter<WeatherHistoryAdapter.WeatherHistoryViewHolder> {

    private final List<String> stringList;

    public WeatherHistoryAdapter(List<String> stringList) {
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public WeatherHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeatherHistoryViewHolder(ItemWeatherHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherHistoryViewHolder holder, int position) {
        holder.bind(stringList.get(position));
    }

    @Override
    public int getItemCount() {
        if (stringList != null) {
            return stringList.size();
        } else {
            return 0;
        }
    }

    public static class WeatherHistoryViewHolder extends RecyclerView.ViewHolder {
        private final ItemWeatherHistoryBinding binding;

        public WeatherHistoryViewHolder(ItemWeatherHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(String value) {
            binding.weatherHistoryValue.setText(value);
        }

    }
}
