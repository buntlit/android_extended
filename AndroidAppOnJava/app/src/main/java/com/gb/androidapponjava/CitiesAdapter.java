package com.gb.androidapponjava;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {

    private List<String> cities;
    private static OnItemClickListener onItemClickListener;

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city_list, parent, false));
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

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.cityTextView);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onCityTextClick(view, getAdapterPosition());
                    }
                }
            });
        }


        void bind(String city) {
            textView.setText(city);
        }
    }

    public interface OnItemClickListener {
        void onCityTextClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}

