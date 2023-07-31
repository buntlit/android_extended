package com.gb.androidapponjava.view;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gb.androidapponjava.databinding.ItemCityListBinding;

import java.util.List;

class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private List<String> cities;
    private final OnItemClickListener onItemClickListener;

    public CitiesAdapter(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void updateCitiesList(List<String> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityViewHolder(ItemCityListBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false)
                , this.onItemClickListener
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

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        private final ItemCityListBinding binding;

        public CityViewHolder(ItemCityListBinding binding, OnItemClickListener onItemClickListener) {
            super(binding.getRoot());
            this.binding = binding;
            binding.cityTextView.setOnClickListener(view-> {
                if (onItemClickListener != null) {
                    onItemClickListener.onCityTextClick(getAdapterPosition());
                }
            });
        }

        void bind(String city) {
            binding.cityTextView.setText(city);
        }
    }

    public interface OnItemClickListener {
        void onCityTextClick(int position);
    }
}

