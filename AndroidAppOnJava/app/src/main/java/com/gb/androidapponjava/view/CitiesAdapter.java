package com.gb.androidapponjava.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.androidapponjava.R;

import java.util.List;

class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private List<String> cities;
    private final OnItemClickListener onItemClickListener;

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public CitiesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_list, parent, false),
                this.onItemClickListener
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.bind(cities.get(position));
    }

    @Override
    public int getItemCount() {
        if (cities != null) {
            return cities.size();
        } else return 0;
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {

        private final TextView textView;

        public CityViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.cityTextView);
            textView.setOnClickListener((view) -> {
                        if (onItemClickListener != null) {
                            onItemClickListener.onCityTextClick(getAdapterPosition());
                        }
                    }
            );
        }

        void bind(String city) {
            textView.setText(city);
        }
    }

    public interface OnItemClickListener {
        void onCityTextClick(int position);
    }
}

