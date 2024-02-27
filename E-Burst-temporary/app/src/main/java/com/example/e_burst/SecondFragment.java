package com.example.e_burst;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_burst.databinding.FragmentSecondBinding;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class SecondFragment extends Fragment   {

    private FragmentSecondBinding binding;

    private TextView mBluetoothStatus;
    private ArrayAdapter<String> mBTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private ListView mDevicesListView;

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
        //mDevicesListView.setOnItemClickListener(mDeviceClickListener);

        mBluetoothStatus = binding.textView8;

        binding.homebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        /*
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
            }
        });
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        //mDevicesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if (!bAdapter.isEnabled()) {
                return;
            }

            mBluetoothStatus.setText("Connecting…");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) view).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            // Spawn a new thread to avoid blocking the GUI one
            new Thread() {
                @Override
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = bAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                            return;
                        }
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                        }
                    }
                    if (!fail) {
                        mConnectedThread = new ConnectedThread(mBTSocket, mHandler);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            //Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }
        if (ActivityCompat.checkSelfPermission(getView().getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            //
            return null;
        }
        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}