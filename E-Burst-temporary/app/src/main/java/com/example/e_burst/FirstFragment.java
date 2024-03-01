package com.example.e_burst;

import static com.example.e_burst.btController.bAdapter;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_burst.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        btController.setBluetoothAdapter();

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        btController.mBtRead = binding.btRead;
        btController.mBluetoothStatus = binding.btStatus;


        btController.setBtHandler();


        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        binding.off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!bAdapter.isEnabled()) {
                    return;
                }

                /*
                if connected
                btController.btDisconnect();
                else
                */
                if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                btController.btConnect(btController.lastConnected);
            }
        });
    }

    private void setProgressbar(int progress) {
        binding.progressBar3.setProgress(progress);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

    }

}