package com.example.e_burst;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_burst.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;
    private FragmentFirstBinding binding;
    TextView pairedTV;
    Button btOn, btSearch, btOff, btPaired;
    BluetoothAdapter bAdapter;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = getView();

        pairedTV = view.findViewById(R.id.pairedTV);
        btOn = view.findViewById(R.id.btOn);
        btSearch = view.findViewById(R.id.btOff);
        btOff = view.findViewById(R.id.btSearch);
        btPaired = view.findViewById(R.id.btPaired);

        btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bAdapter.isEnabled()) {
                    System.out.println("Turning On Bluetooth...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    System.out.println("Bluetooth is already on");
                }
            }
        });

        btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                if (!bAdapter.isDiscovering()) {
                    System.out.println("Making Your Device Discoverable");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        btOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAdapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    bAdapter.disable();
                    System.out.println("Turning Bluetooth Off");
                } else {
                    System.out.println("Bluetooth is already off");
                }
            }
        });

        btPaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bAdapter.isEnabled()) {
                    pairedTV.setText("Paired Devices");
                    if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Set<BluetoothDevice> devices = bAdapter.getBondedDevices();
                    for (BluetoothDevice device : devices) {
                        pairedTV.append("\nDevice: " + device.getName() + ", " + device);
                    }
                } else {
                    System.out.println("Turn on bluetooth to get paired devices");
                }
            }
        });


        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}