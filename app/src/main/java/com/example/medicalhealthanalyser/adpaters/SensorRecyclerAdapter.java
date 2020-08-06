package com.example.medicalhealthanalyser.adpaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalhealthanalyser.R;
import com.example.medicalhealthanalyser.models.SensorDevice;
import com.example.medicalhealthanalyser.viewholders.SensorDeviceViewHolder;

import java.util.ArrayList;

public class SensorRecyclerAdapter extends RecyclerView.Adapter<SensorDeviceViewHolder> {
    ArrayList<SensorDevice> SensorList;
    public interface OnItemClickedListeaner{
        void onclick(SensorDevice sensorDevice);
    }

    OnItemClickedListeaner onItemClickedListeaner;
    public SensorRecyclerAdapter(ArrayList<SensorDevice> sensorDevices, OnItemClickedListeaner onItemClickedListeaner){
        this.SensorList = sensorDevices;
        this.onItemClickedListeaner = onItemClickedListeaner;
    }

    @NonNull
    @Override
    public SensorDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bluetooth_device,parent,false);
        return new SensorDeviceViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SensorDeviceViewHolder holder, int position) {

        final SensorDevice currentSensorDevice = SensorList.get(position);
        holder.onBindViewHolder(currentSensorDevice, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickedListeaner.onclick(currentSensorDevice);
            }
        });
    }

    @Override
    public int getItemCount() {
        return SensorList.size();
    }
}
