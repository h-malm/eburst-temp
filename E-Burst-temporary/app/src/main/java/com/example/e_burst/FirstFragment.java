package com.example.e_burst;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_burst.databinding.FragmentFirstBinding;

import java.util.Set;

public class FirstFragment extends Fragment {

    private static final int REQUEST_ENABLE_BT = 0;

    private FragmentFirstBinding binding;

    public static TextView mBtRead;
    private ArrayAdapter<String> mBTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;

    BluetoothAdapter bAdapter;

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
//                                doSomeOperations();
                        System.out.println("2");
                    }
                }
            });

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {


        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            System.out.println("1");
            // Device doesn't support Bluetooth
        }

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBTArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1);


        mBtRead = binding.btRead;


        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        binding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        binding.scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                    return;
                }
                if (!bAdapter.isEnabled()) {
                    System.out.println("Turning On Bluetooth...");
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    //startActivityForResult(intent, REQUEST_ENABLE_BT);


                    someActivityResultLauncher.launch(intent);
                } else {
                    System.out.println("Bluetooth is already on");
                }
            }
        });

        binding.off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (bAdapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    bAdapter.disable();
                    System.out.println("Turning Bluetooth Off");
                } else {
                    System.out.println("Bluetooth is already off");
                }
            }
        });

        binding.pairedBtn.setOnClickListener(new View.OnClickListener() {
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

        binding.discover.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){


                // Check if the device is already discovering
                if (ActivityCompat.checkSelfPermission(view.getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (bAdapter.isDiscovering()) {
                    System.out.println("discovering?");
                    bAdapter.cancelDiscovery();
                } else {
                    if (bAdapter.isEnabled()) {
                        System.out.println("beforeStart");
                        mBTArrayAdapter.clear(); // clear items
                        bAdapter.startDiscovery();

                        System.out.println("afterStart");
                        v.getContext().registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
                    }
                }
            }
        });

    }

    public void toast(CharSequence text){
        Toast.makeText(getActivity(),text,Toast.LENGTH_LONG).show();
    }
    private void setProgressbar(int progress){
        binding.progressBar3.setProgress(progress);
    }


    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive");
            String action = intent.getAction();
            if(BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
                System.out.println(mBTArrayAdapter);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        getView().getContext().unregisterReceiver(blReceiver);
    }

}