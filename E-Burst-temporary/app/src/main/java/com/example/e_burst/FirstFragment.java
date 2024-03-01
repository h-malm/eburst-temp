package com.example.e_burst;

import static androidx.core.content.ContextCompat.registerReceiver;

import static com.example.e_burst.btController.bAdapter;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.e_burst.databinding.FragmentFirstBinding;

public class FirstFragment extends Fragment {
    private FragmentFirstBinding binding;

    /*
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier

    private FragmentFirstBinding binding;

    public TextView mBtRead;
    private TextView mBluetoothStatus;
    private ArrayAdapter<String> mBTArrayAdapter;
    private Set<BluetoothDevice> pairedDevices;
    private Handler mHandler;

    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path
    private ListView mDevicesListView;

    public final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    BluetoothAdapter bAdapter;

     */

    /* not used
    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // There are no request codes
                        Intent data = result.getData();
//                                doSomeOperations();
                        System.out.println("It did something");

                    }
                }
            });

     */

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        /*
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            System.out.println("Device doesn't support Bluetooth");
            // Device doesn't support Bluetooth
        }*/
        btController.setBluetoothAdapter();

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mBTArrayAdapter = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1);


        btController.mBtRead = binding.btRead;
        btController.mBluetoothStatus = binding.btStatus;

        /* in settings
        mDevicesListView = binding.devicesListView;
        mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
        mDevicesListView.setOnItemClickListener(mDeviceClickListener);

         */
        btController.setBtHandler();
/*
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_READ) {
                    String readMessage = null;
                    readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);
                    mBtRead.setText(readMessage);
                }

                if (msg.what == CONNECTING_STATUS) {
                    char[] sConnected;
                    if (msg.arg1 == 1)
                        mBluetoothStatus.setText("Connected to Device: " + msg.obj);
                    else
                        mBluetoothStatus.setText("Connection Failed");
                }
            }
        };*/

        binding.settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });


        /* not used (used needs to have bluetooth on and have paired to controller in device settings)
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
        });*/

        //
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

                //if (mConnectedThread!=null) mConnectedThread.cancel();
                /* not used (change to disconnect)
                if (bAdapter.isEnabled()) {
                    if (ActivityCompat.checkSelfPermission(view.getContext(), android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    bAdapter.disable();
                    System.out.println("Turning Bluetooth Off");
                } else {
                    System.out.println("Bluetooth is already off");
                }*/
            }
        });

        /* in setting
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
        });*/

        /* not used
        binding.discover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Check if the device is already discovering
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.BLUETOOTH_ADMIN)!= PackageManager.PERMISSION_GRANTED){
                    return;
                }

                if (bAdapter.isDiscovering()) {
                    System.out.println("discovering?");
                    bAdapter.cancelDiscovery();
                } else {
                    if (bAdapter.isEnabled()) {
                        System.out.println("beforeStart");
                        mBTArrayAdapter.clear(); // Is this necessary?
                        bAdapter.startDiscovery();

                        System.out.println("afterStart");
                        IntentFilter intentFilter = new IntentFilter();
                        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
                        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
                        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
                        if (getContext() != null) {
                            System.out.println("Running through the 90's");
                            registerReceiver(getContext(), blReceiver, intentFilter, ContextCompat.RECEIVER_NOT_EXPORTED);
                            //getContext().registerReceiver( blReceiver,  intentFilter);

                            //bAdapter.cancelDiscovery();
                        }
                    }
                }
            }
        });*/


    }

    public void toast(CharSequence text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

    private void setProgressbar(int progress) {
        binding.progressBar3.setProgress(progress);
    }

    /* not used
    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        //@SuppressLint("MissingPermission")

        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("onReceive");
            String action = intent.getAction();
            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                System.out.println("D finished");
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                System.out.println("D Start");
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                if (getContext() != null) {
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                }
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
                System.out.println(mBTArrayAdapter);
            }


        }
    };*/

    /*
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




    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;

        //getView().getContext().unregisterReceiver(blReceiver);
    }

}