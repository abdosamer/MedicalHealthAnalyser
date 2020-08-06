package com.example.medicalhealthanalyser.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalhealthanalyser.R;
import com.example.medicalhealthanalyser.adpaters.SensorRecyclerAdapter;
import com.example.medicalhealthanalyser.models.Constants;
import com.example.medicalhealthanalyser.models.SensorDevice;
import com.example.medicalhealthanalyser.services.BluetoothChatService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static com.example.medicalhealthanalyser.models.Constants.CONNECTED;
import static com.example.medicalhealthanalyser.models.Constants.CONNECTING;
import static com.example.medicalhealthanalyser.models.Constants.DeviceMac;
import static com.example.medicalhealthanalyser.models.Constants.DeviceName;
import static com.example.medicalhealthanalyser.models.Constants.FAULT;
import static com.example.medicalhealthanalyser.models.Constants.GSR_TURN_ON;
import static com.example.medicalhealthanalyser.models.Constants.NOT_CONNECTED;
import static com.example.medicalhealthanalyser.models.Constants.NO_Paired_Devices;
import static com.example.medicalhealthanalyser.models.Constants.REQUEST_ENABLE_BT;
import static com.example.medicalhealthanalyser.models.Constants.SENSOR_TURN_OFF;
import static com.example.medicalhealthanalyser.models.Constants.VERIFY;

public class HomeActivity extends AppCompatActivity {


    boolean connected = false;

    CardView mGsrSensor;
    CardView mEmgSensor;
    CardView mEcgSensor;
    CardView mTempSensor;
    CardView mBluetoothConnectBt;
    TextView mBluetoothConnectBtText;
    ProgressBar progressBar;

    StringBuilder stringBuilder;

    BluetoothAdapter bluetoothAdapter;

    ArrayList<SensorDevice> sensorDevices;
    SensorRecyclerAdapter sensorRecyclerAdapter;

    SensorDevice selectedDevice;

    BluetoothChatService bluetoothChatService = null;


    private final Handler mHandler = new Handler() { // open 1
        public void handleMessage(Message message) {// open 2
            switch (message.what) { // open 3
                case 1:
                    switch (message.arg1) { // open 4
                        case 2:
                            return;
                        case 3:
                            return;
                        default:
                            return;
                    } // close 4
                case 2:
                    String data = (String) message.obj;
                    if (data != null) {
                        Log.d("TAG", data);
                        if (data.contains("O")) {
                            Toast.makeText(HomeActivity.this, "Device Checked Sucessfully", Toast.LENGTH_SHORT).show();
                            changeBluetoothBtState(CONNECTED);
                            showProgressPar(false);
                        }
                        makeFrame(data);

                    }
                    return;
                case 4:

                    return;
                case 5:
                    return;
                default:
                    return;
            } // close 3
        } //close 2
    };

    private void makeFrame(String data) {
        char[] chData = data.toCharArray();
        for (int i = 0; i < chData.length; i++) {
            if (chData[i] == 'E') {
                Log.d("TAGS", stringBuilder.toString());
                stringBuilder = new StringBuilder();
            } else {
                stringBuilder.append(chData[i]);
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        stringBuilder = new StringBuilder();
        stringBuilder.append("0");


        // init all the layout views
        initLayoutViews();

        // init bluetoth adapter
        initBluetooth();

        ininOperations();
    }

    private void ininOperations() {
        mGsrSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(connected) {

                    if (bluetoothChatService != null) {
                        Log.d("Chat Service", "Stop");
                        bluetoothChatService.stop();
                    }
                    Intent intent = new Intent(HomeActivity.this, GsrActivity.class);
                    Toast.makeText(HomeActivity.this, selectedDevice.getmMac(), Toast.LENGTH_SHORT).show();
                    Log.d("OPEN", selectedDevice.getmMac());
                    intent.putExtra(DeviceMac, selectedDevice.getmMac());
                    startActivity(intent);
                }else{
                    Toast.makeText(HomeActivity.this, "you should be connected first", Toast.LENGTH_SHORT).show();
                }


            }
        });

        mEmgSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected){

                }else{
                    Toast.makeText(HomeActivity.this, "you should be connected first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mEmgSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected){

                }else{
                    Toast.makeText(HomeActivity.this, "you should be connected first", Toast.LENGTH_SHORT).show();
                }

            }
        });

        mTempSensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connected){

                }else{
                    Toast.makeText(HomeActivity.this, "you should be connected first", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    public void alert(String str, String str2) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(str);
        builder.setCancelable(false);
        builder.setMessage(str2);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                HomeActivity.this.finish();
            }
        });
        builder.show();
    }


    private void initLayoutViews() {

        // init Bluetooth CardView Button
        mBluetoothConnectBt = findViewById(R.id.connect_device_card);
        mBluetoothConnectBtText = findViewById(R.id.bluetooth_bt_text_view);
        mGsrSensor = findViewById(R.id.gsr_view);

        mEmgSensor = findViewById(R.id.emg_view);
        mEcgSensor = findViewById(R.id.ecg_view);
        mTempSensor = findViewById(R.id.temp_view);
        progressBar = findViewById(R.id.progressBar);

    }

    private void write(String str) {

        bluetoothChatService.write(str.getBytes());

    }

    private void showProgressPar(boolean enable) {

        if (enable) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void changeBluetoothBtState(String state) {
        switch (state) {
            case CONNECTING:
                mBluetoothConnectBtText.setText(CONNECTING);
                connected = false;
                mBluetoothConnectBtText.setTextColor(getResources().getColor(R.color.colorAccentInfo));
                mBluetoothConnectBt.setEnabled(false);
                break;
            case CONNECTED:
                mBluetoothConnectBtText.setText(CONNECTED);
                connected = true;
                mBluetoothConnectBtText.setTextColor(getResources().getColor(R.color.colorPrimary));
                mBluetoothConnectBt.setEnabled(false);
                break;
            case NOT_CONNECTED:
                mBluetoothConnectBtText.setText(NOT_CONNECTED);
                connected = false;
                mBluetoothConnectBtText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBluetoothConnectBt.setEnabled(true);
                break;
            case VERIFY:
                mBluetoothConnectBtText.setText(VERIFY);
                mBluetoothConnectBtText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBluetoothConnectBt.setEnabled(true);
                break;
            default:
                mBluetoothConnectBtText.setText(FAULT);
                mBluetoothConnectBtText.setTextColor(getResources().getColor(R.color.colorAccent));
                mBluetoothConnectBt.setEnabled(true);
                break;
        }
    }

    private void initBluetooth() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            // Device doesn't support Bluetooth
            Toast.makeText(this, R.string.no_bluetooth_drive, Toast.LENGTH_LONG).show();
            //close main activity
            finish();
        }

        mBluetoothConnectBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if we got here it means that the device support bluetooth since it doesn't close the activity
                if (mBluetoothConnectBtText.getText().toString().contentEquals(VERIFY)) {
                    bluetoothChatService.write(Constants.VERFY_SEND_MSG);
                    Toast.makeText(HomeActivity.this, "verify message sent", Toast.LENGTH_SHORT).show();
                } else {
                    checkIfBluetoothEnabled();
                    changeBluetoothBtState(CONNECTING);
                }
            }
        });
    }

    private void checkIfBluetoothEnabled() {
        //check if bluetooth is enabled
        if (!bluetoothAdapter.isEnabled()) {
            //start requesting to enable bluetooth by an intent
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            //start the activity for the request
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            //here we are showing a list of devices to pick our device from them
            initBluetoothDevicesListDialog();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            // here it is triggered by the request to enable bluetooth
            switch (resultCode) {
                case RESULT_OK: // here it means the request for enable have been accepted

                    //here we are showing a list of devices to pick our device from them
                    initBluetoothDevicesListDialog();
                    break;
                case RESULT_CANCELED: // here it means the request to enable bluetooth haven't been approved
                    break;
            }
        }
    }

    private void initBluetoothDevicesListDialog() {

        final Dialog alertDialog = new Dialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.setContentView(getLayoutInflater().inflate(R.layout.dialog_bluetooth_devices, null));
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        RecyclerView sensorList = alertDialog.findViewById(R.id.sensor_device_recycler_list);
        TextView closeBt = alertDialog.findViewById(R.id.close_dialog);
        alertDialog.show();

        closeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


        //making an Array list of paired Devices to hold the data after retrieving it to display it
        sensorDevices = new ArrayList<>();
        sensorRecyclerAdapter = new SensorRecyclerAdapter(sensorDevices, new SensorRecyclerAdapter.OnItemClickedListeaner() {
            @Override
            public void onclick(SensorDevice sensorDevice) {
                Toast.makeText(HomeActivity.this, "Connected to : " + sensorDevice.getmName() +
                        "\n Checking Device... ", Toast.LENGTH_LONG).show();

                selectedDevice = new SensorDevice(sensorDevice.getmName(), sensorDevice.getmMac());
                checkDevice();
                showProgressPar(false);
                changeBluetoothBtState(VERIFY);
                alertDialog.dismiss();
            }
        });
        initBluetoothDevicesList();

        sensorList.setLayoutManager(new LinearLayoutManager(this));
        sensorList.setHasFixedSize(false);
        sensorList.setAdapter(sensorRecyclerAdapter);

    }

    private void checkDevice() {
        if (this.bluetoothChatService == null) {
            setupConnection();
        }
    }

    private void setupConnection() {
        Log.d(Constants.TAG, "setupChat()");
        this.bluetoothChatService = new BluetoothChatService(this, this.mHandler);

        connectWithTheDevice();
    }

    private void connectWithTheDevice() {
        try {
            this.bluetoothChatService.connect(bluetoothAdapter.getRemoteDevice(selectedDevice.getmMac()), false);
        } catch (IllegalArgumentException | IOException unused) {
            Log.d(Constants.TAG, "Unable to connect! Your are trying to connect is invalid bluetooth address");
        }

    }



    public void onDestroy() {
        super.onDestroy();
        if (this.bluetoothChatService != null) {
            Log.d("Chat Service", "Stop");
            this.bluetoothChatService.stop();
        }

    }



    private void initBluetoothDevicesList() {


        //making a list of paired Devices
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {

                /* making a new device to be added in the list with the device
                 * name -> getName() and its Mac -> getAddress()
                 */
                SensorDevice sensorDevice = new SensorDevice(device.getName(), device.getAddress());
                sensorDevices.add(sensorDevice);
                sensorRecyclerAdapter.notifyDataSetChanged();

            }
        } else {

            /* here it means there is no paired device so
             * we will add a holder device that it tells user that
             * it have to pair the device first
             */
            SensorDevice sensorDevice = new SensorDevice(getString(R.string.Pair_Device_msg), NO_Paired_Devices);
            sensorDevices.add(sensorDevice);
            sensorRecyclerAdapter.notifyDataSetChanged();

        }

    }
}