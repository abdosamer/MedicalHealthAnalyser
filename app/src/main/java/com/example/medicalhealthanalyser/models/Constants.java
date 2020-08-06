package com.example.medicalhealthanalyser.models;

import java.util.UUID;

public abstract class Constants {

    //embedded Commands
    public static final byte[] GSR_TURN_ON = "1".getBytes();
    public static final byte[] EMG_TURN_ON = "1".getBytes();
    public static final byte[] SENSOR_TURN_OFF = "0".getBytes();
    public static final byte[] VERFY_SEND_MSG = "6".getBytes();
    public static final String VERFY_RECIEVE_MSG = "OK";

    //Sensor States
    public static final String START = "Start";
    public static final String STOP = "Stop";

    //bluetooth Button States
    public static final String NOT_CONNECTED = "Connect";
    public static final String VERIFY = "Click to Verify";
    public static final String CONNECTING = "Connecting...";
    public static final String FAULT = "Fault";
    public static final String CONNECTED = "Connected";

    //bluetooth Constants
    public static final String NO_Paired_Devices = "-1";
    public static final String DeviceName = "deviceName";
    public static final String DeviceMac = "deviceMac";


    /* renamed from: CR */
    public static final String f58CR = "\r";
    public static final String CRLF = "\r\n";
    public static final String DEVICE_NAME = "device_name";
    public static final String H_CR = "0d";
    public static final String H_CRLF = "0d0a";
    public static final String H_LF = "0a";

    /* renamed from: LF */
    public static final String f59LF = "\n";
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_TOAST = 5;
    public static final int MESSAGE_WRITE = 3;
    public static final UUID MY_UUID_INSECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final UUID MY_UUID_SECURE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    public static final String NAME_INSECURE = "BluetoothChatInsecure";
    public static final String NAME_SECURE = "BluetoothChatSecure";
    public static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    public static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    public static final int REQUEST_ENABLE_BT = 3;
    public static final int STATE_CONNECTED = 3;
    public static final int STATE_CONNECTING = 2;
    public static final int STATE_LISTEN = 1;
    public static final int STATE_NONE = 0;
    public static final String TAG = "BluetoothConnectionDemo";
    public static final String TOAST = "toast";


}
