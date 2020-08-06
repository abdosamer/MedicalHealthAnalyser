package com.example.medicalhealthanalyser.models;

public class SensorDevice {
    private String mName;
    private String mMac ;

    public SensorDevice(String mName, String mMac) {
        this.mName = mName;
        this.mMac = mMac;
    }

    public String getmName() {
        return mName;
    }

    public String getmMac() {
        return mMac;
    }

}
