package com.gb.androidapponjava.view;

import static com.gb.androidapponjava.databinding.FragmentWeatherHistoryBinding.inflate;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gb.androidapponjava.R;
import com.gb.androidapponjava.databinding.FragmentWeatherHistoryBinding;
import com.gb.androidapponjava.viewmodel.DataViewModel;

public class WeatherHistoryFragment extends Fragment {
    private FragmentWeatherHistoryBinding binding;
    private DataViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(DataViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        final WeatherHistoryAdapter adapter = new WeatherHistoryAdapter(new WeatherHistoryAdapter.WeatherHistoryDiff());
        binding.recyclerWeatherHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerWeatherHistory.setAdapter(adapter);
        viewModel.getFilterWeatherHistoryByDate().observe(requireActivity(), adapter::submitList);
        binding.bottomNavigation.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_filter_city:
                    viewModel.getFilterWeatherHistoryByCity().observe(requireActivity(), adapter::submitList);
                    break;
                case R.id.menu_filter_temperature:
                        viewModel.getFilterWeatherHistoryByTemperature().observe(requireActivity(), adapter::submitList);
                    break;
                case R.id.menu_filter_data:
                default:
                    viewModel.getFilterWeatherHistoryByDate().observe(requireActivity(), adapter::submitList);
                    break;
            }
            return true;
        });
    }
}
