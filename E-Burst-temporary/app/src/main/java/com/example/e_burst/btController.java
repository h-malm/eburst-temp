package com.example.e_burst;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;


import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class btController {
    private static final UUID BT_MODULE_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier


    public static TextView mBtRead;
    public static TextView mBluetoothStatus;
    private static Handler mHandler;

    private static ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private static BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path


    public final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    static BluetoothAdapter bAdapter;

    public static String lastConnected;

    public static void setBluetoothAdapter() {
        bAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bAdapter == null) {
            System.out.println("Device doesn't support Bluetooth");
            // Device doesn't support Bluetooth
        }
    }

    public static void setBtHandler() {
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
        };
    }

    public static void btConnect(String info) {

        mBluetoothStatus.setText("Connecting…");

        // Get the device MAC address, which is the last 17 chars in the View
        final String address = info.substring(info.length() - 17);
        final String name = info.substring(0, info.length() - 17);

        lastConnected = info;

        // Spawn a new thread to avoid blocking the GUI one
        new Thread() {
            @SuppressLint("MissingPermission") //checks permission before calling
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


    @SuppressLint("MissingPermission") //checks permission before calling
    private static BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        try {
            final Method m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", UUID.class);
            return (BluetoothSocket) m.invoke(device, BT_MODULE_UUID);
        } catch (Exception e) {
            //Log.e(TAG, "Could not create Insecure RFComm Connection",e);
        }

        return device.createRfcommSocketToServiceRecord(BT_MODULE_UUID);
    }

    public static void btDisconnect(){
        if (mConnectedThread!=null) mConnectedThread.cancel();
    }
}