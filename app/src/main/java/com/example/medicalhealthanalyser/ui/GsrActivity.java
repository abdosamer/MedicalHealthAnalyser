package com.example.medicalhealthanalyser.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.bluetooth.BluetoothAdapter;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.medicalhealthanalyser.R;
import com.example.medicalhealthanalyser.models.Constants;
import com.example.medicalhealthanalyser.models.SensorDevice;
import com.example.medicalhealthanalyser.services.BluetoothChatService;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.IOException;

import static com.example.medicalhealthanalyser.models.Constants.CONNECTED;
import static com.example.medicalhealthanalyser.models.Constants.DeviceMac;
import static com.example.medicalhealthanalyser.models.Constants.DeviceName;
import static com.example.medicalhealthanalyser.models.Constants.GSR_TURN_ON;
import static com.example.medicalhealthanalyser.models.Constants.SENSOR_TURN_OFF;
import static com.example.medicalhealthanalyser.models.Constants.START;
import static com.example.medicalhealthanalyser.models.Constants.STOP;

public class GsrActivity extends AppCompatActivity {


    LineChart gsrGraph;
    Thread graphThread;
    boolean plotData = true;


    CardView StartCardView;
    TextView startTextView;

    SensorDevice selectedDevice;

    BluetoothAdapter bluetoothAdapter;

    BluetoothChatService bluetoothChatService = null;
    StringBuilder stringBuilder;

    String mac = "00:21:13:00:E7:F2";

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
                dataGraphing(stringBuilder.toString());
                stringBuilder = new StringBuilder();
            } else if (chData[i] == '\"') {

            } else if(chData[i]== '0'||chData[i]== '1'||chData[i]== '2'||chData[i]== '3'||chData[i]== '4'
                    ||chData[i]== '5'||chData[i]== '6'||chData[i]== '7'||chData[i]== '8'||chData[i]== '9'){
                stringBuilder.append(chData[i]);
            }
        }

    }

    private void dataGraphing(String data) {
            addEntry(Integer.parseInt(data));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gsr_layout);

        stringBuilder = new StringBuilder();
        stringBuilder.append("0");


        initViews();

        initSensor();

        checkDevice();

        initGraph();
    }

    private void initGraph() {

        gsrGraph = (LineChart) findViewById(R.id.gsr_graph);


        // enable description text
        gsrGraph.getDescription().setEnabled(true);

        // enable touch gestures
        gsrGraph.setTouchEnabled(true);

        // enable scaling and dragging
        gsrGraph.setDragEnabled(true);
        gsrGraph.setScaleEnabled(true);
        gsrGraph.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        gsrGraph.setPinchZoom(true);

        // set an alternative background color
        gsrGraph.setBackgroundColor(Color.TRANSPARENT);

        LineData data = new LineData();
        data.setValueTextColor(Color.WHITE);

        // add empty data
        gsrGraph.setData(data);

        // get the legend (only possible after setting data)
        Legend l = gsrGraph.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.WHITE);

        XAxis xl = gsrGraph.getXAxis();
        xl.setTextColor(Color.WHITE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);
        xl.setEnabled(true);

        YAxis leftAxis = gsrGraph.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.colorPrimary));
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMaximum(10000f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = gsrGraph.getAxisRight();
        rightAxis.setEnabled(false);

        gsrGraph.getAxisLeft().setDrawGridLines(false);
        gsrGraph.getXAxis().setDrawGridLines(false);
        gsrGraph.setDrawBorders(false);

        feedMultiple();


        startPlot();
    }


    private void addEntry(int event) {

        LineData data = gsrGraph.getData();

        if (data != null) {

            ILineDataSet set = data.getDataSetByIndex(0);
            // set.addEntry(...); // can be called as well

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }


            float value = (float) event;
            Log.d("TAG", "addEntry: "+value);
//            data.addEntry(new Entry(set.getEntryCount(), (float) (Math.random() * 80) + 10f), 0);
            data.addEntry(new Entry(set.getEntryCount(), value + 5 ), 0);
            data.notifyDataChanged();

            // let the chart know it's data has changed
            gsrGraph.notifyDataSetChanged();

            // limit the number of visible entries
            gsrGraph.setVisibleXRangeMaximum(150);
            // gsrGraph.setVisibleYRange(30, AxisDependency.LEFT);

            // move to the latest entry
            gsrGraph.moveViewToX(data.getEntryCount());

        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setLineWidth(3f);
        set.setColor(Color.RED);
        set.setHighlightEnabled(false);
        set.setDrawValues(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        set.setCubicIntensity(0.2f);
        return set;
    }

    private void feedMultiple() {

        if (graphThread != null){
            graphThread.interrupt();
        }

        graphThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true){
                    plotData = true;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        });

        graphThread.start();
    }



    void startPlot() {
        if (graphThread != null) {
            graphThread.interrupt();
        }

        graphThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    plotData = true;
                    try {
                        graphThread.sleep(10);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        bluetoothChatService.write(SENSOR_TURN_OFF);
        if(graphThread != null){
            graphThread.interrupt();
        }
    }


    private void initSensor() {

        StartCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTextView.getText().toString().contentEquals(START)) {
                    Toast.makeText(GsrActivity.this, "started", Toast.LENGTH_SHORT).show();
                    TurnOnSensor();
                } else if (startTextView.getText().toString().contentEquals(STOP)) {
                    Toast.makeText(GsrActivity.this, "STOPPED", Toast.LENGTH_SHORT).show();
                    TurnOffSensor();
                }
            }
        });
    }


    private void write(byte[] str) {

        bluetoothChatService.write(GSR_TURN_ON);

    }


    private void checkDevice() {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mac = "00:21:13:00:E7:F2";
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
            this.bluetoothChatService.connect(bluetoothAdapter.getRemoteDevice(mac), false);
        } catch (IllegalArgumentException | IOException unused) {
            Log.d(Constants.TAG, "Unable to connect! Your are trying to connect is invalid bluetooth address");
        }

    }


    private void initViews() {
        StartCardView = findViewById(R.id.connect_gsr_device_card);
        startTextView = findViewById(R.id.start_gsr_sensor_view);
    }

    void TurnOnSensor() {
        write(GSR_TURN_ON);
        enablePowerButton(false);
    }

    void TurnOffSensor() {
        bluetoothChatService.write(SENSOR_TURN_OFF);
        enablePowerButton(true);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bluetoothChatService != null) {
            Log.d("Chat Service", "Stop");

            bluetoothChatService.stop();
        }
    }

    void enablePowerButton(boolean enable) {
        if (enable) {
            startTextView.setText(START);
            startTextView.setTextColor(getResources().getColor(R.color.colorPrimary));
            StartCardView.setEnabled(true);

        } else {
            startTextView.setText(STOP);
            startTextView.setTextColor(getResources().getColor(R.color.colorAccent));
            StartCardView.setEnabled(true);

        }
    }


}