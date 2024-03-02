package com.example.e_burst;

import static com.example.e_burst.btController.bAdapter;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_burst.databinding.FragmentSecondBinding;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    private ArrayAdapter<String> mBTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView mDevicesListView;

    private TextView distance;

    private Spinner spinspin;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBTArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1);

        mDevicesListView = binding.devicesListView;
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);


        //List for spinner item (Something else eventually)
        final List<String> list = new ArrayList<>();
        list.add("English");
        list.add("Svenska");
        list.add("Suomi");


        //Creates Spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_list_item_1, list);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);


        spinspin = binding.spinner;
        spinspin.setAdapter(adapter);

        distance = binding.textViewDistance;
        distance.setText(String.valueOf(btController.totalDistance/1000).substring(0,2));
        btController.mBluetoothStatus = binding.textView8;

        binding.homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        binding.resetDistance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btController.resetDistance();
                distance.setText(String.valueOf(btController.totalDistance));
            }
        });

        binding.connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        binding.pairedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mBTArrayAdapter.clear();
                pairedDevices = bAdapter.getBondedDevices();
                if (bAdapter.isEnabled()) {
                    // put it's one to the adapter
                    for (BluetoothDevice device : pairedDevices)

                        mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

                }
                mDevicesListView.setVisibility(View.VISIBLE);
            }
        });
    }



    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            mDevicesListView.setVisibility(View.INVISIBLE);

            if (!bAdapter.isEnabled()) {
                return;
            }

            String info = ((TextView) view).getText().toString();

            if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            btController.btConnect(info);

        }
    };


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}