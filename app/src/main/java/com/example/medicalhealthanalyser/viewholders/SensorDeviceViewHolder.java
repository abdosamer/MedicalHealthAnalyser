package com.example.medicalhealthanalyser.viewholders;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.medicalhealthanalyser.R;
import com.example.medicalhealthanalyser.models.Constants;
import com.example.medicalhealthanalyser.models.SensorDevice;

public class SensorDeviceViewHolder extends RecyclerView.ViewHolder {

    View itemView;
    TextView mName;
    TextView mAddress;

    public SensorDeviceViewHolder(@NonNull View itemView) {
        super(itemView);
        this.itemView = itemView;
        mName = itemView.findViewById(R.id.device_name);
        mAddress = itemView.findViewById(R.id.device_mac);
    }


    public void onBindViewHolder(SensorDevice model, View.OnClickListener onViewClick) {

        mName.setText(model.getmName());
        mAddress.setText(model.getmMac());

        if (model.getmMac().contentEquals(Constants.NO_Paired_Devices)) {
            mAddress.setVisibility(View.GONE);
        } else {
            itemView.setOnClickListener(onViewClick);
        }
    }

}
