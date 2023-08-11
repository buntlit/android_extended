package com.example.weatherappwithgpsandbottomsheet.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherappwithgpsandbottomsheet.databinding.BottomSheetLayoutBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class MainBottomSheetDialogFragment extends BottomSheetDialogFragment implements Button.OnClickListener {

    private BottomSheetLayoutBinding binding;
    private MainViewModel viewModel;

    public static MainBottomSheetDialogFragment newInstance() {
        return new MainBottomSheetDialogFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BottomSheetLayoutBinding.inflate(inflater, container, false);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        setCancelable(true);
        binding.btn1.setOnClickListener(this);
        binding.btn2.setOnClickListener(this);
        binding.btn3.setOnClickListener(this);
        binding.btn4.setOnClickListener(this);
        binding.btn5.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        String textFromButton = ((Button) view).getText().toString();
        viewModel.saveModel(textFromButton);
        dismiss();
    }
}
