package com.example.medicalhealthanalyser.services;

import com.example.medicalhealthanalyser.models.Constants;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothChatService {
    private static final String TAG = "BluetoothChatService";
    /* access modifiers changed from: private */
    public final BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
    /* access modifiers changed from: private */
    public ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private AcceptThread mSecureAcceptThread;
    /* access modifiers changed from: private */
    public int mState = 0;

    private class AcceptThread extends Thread {
        private String mSocketType;
        private final BluetoothServerSocket mmServerSocket;

        public AcceptThread(boolean z) throws IOException {
            BluetoothServerSocket bluetoothServerSocket;
            this.mSocketType = z ? "Secure" : "Insecure";
            if (z) {
                try {
                    bluetoothServerSocket = BluetoothChatService.this.mAdapter.listenUsingRfcommWithServiceRecord(Constants.NAME_SECURE, Constants.MY_UUID_SECURE);
                } catch (IOException e) {
                    String str = BluetoothChatService.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Socket Type: ");
                    sb.append(this.mSocketType);
                    sb.append("listen() failed");
                    Log.e(str, sb.toString(), e);
                    bluetoothServerSocket = null;
                }
            } else {
                bluetoothServerSocket = BluetoothChatService.this.mAdapter.listenUsingInsecureRfcommWithServiceRecord(Constants.NAME_INSECURE, Constants.MY_UUID_INSECURE);
            }
            this.mmServerSocket = bluetoothServerSocket;
        }

        public void run() {
            String str = BluetoothChatService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Socket Type: ");
            sb.append(this.mSocketType);
            sb.append("BEGIN mAcceptThread");
            sb.append(this);
            Log.d(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("AcceptThread");
            sb2.append(this.mSocketType);
            setName(sb2.toString());
            while (BluetoothChatService.this.mState != 3) {
                try {
                    BluetoothSocket accept = this.mmServerSocket.accept();
                    if (accept != null) {
                        synchronized (BluetoothChatService.this) {
                            switch (BluetoothChatService.this.mState) {
                                case 0:
                                case 3:
                                    try {
                                        accept.close();
                                        break;
                                    } catch (IOException e) {
                                        Log.e(BluetoothChatService.TAG, "Could not close unwanted socket", e);
                                        break;
                                    }
                                case 1:
                                case 2:
                                    BluetoothChatService.this.connected(accept, accept.getRemoteDevice(), this.mSocketType);
                                    break;
                            }
                        }
                    }
                } catch (IOException e2) {
                    String str2 = BluetoothChatService.TAG;
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Socket Type: ");
                    sb3.append(this.mSocketType);
                    sb3.append(" accept() failed");
                    Log.e(str2, sb3.toString(), e2);
                }
            }
            String str3 = BluetoothChatService.TAG;
            StringBuilder sb4 = new StringBuilder();
            sb4.append("END mAcceptThread, socket Type: ");
            sb4.append(this.mSocketType);
            Log.i(str3, sb4.toString());
        }

        public void cancel() {
            String str = BluetoothChatService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Socket Type");
            sb.append(this.mSocketType);
            sb.append("cancel ");
            sb.append(this);
            Log.d(str, sb.toString());
            try {
                this.mmServerSocket.close();
            } catch (IOException e) {
                String str2 = BluetoothChatService.TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Socket Type");
                sb2.append(this.mSocketType);
                sb2.append("close() of server failed");
                Log.e(str2, sb2.toString(), e);
            }
        }
    }

    private class ConnectThread extends Thread {
        private String mSocketType;
        private final BluetoothDevice mmDevice;
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice bluetoothDevice, boolean z) throws IOException {
            BluetoothSocket bluetoothSocket;
            this.mmDevice = bluetoothDevice;
            this.mSocketType = z ? "Secure" : "Insecure";
            if (z) {
                try {
                    bluetoothSocket = bluetoothDevice.createRfcommSocketToServiceRecord(Constants.MY_UUID_SECURE);
                } catch (IOException e) {
                    String str = BluetoothChatService.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("Socket Type: ");
                    sb.append(this.mSocketType);
                    sb.append("create() failed");
                    Log.e(str, sb.toString(), e);
                    bluetoothSocket = null;
                }
            } else {
                bluetoothSocket = bluetoothDevice.createInsecureRfcommSocketToServiceRecord(Constants.MY_UUID_INSECURE);
            }
            this.mmSocket = bluetoothSocket;
        }

        public void run() {
            String str = BluetoothChatService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("BEGIN mConnectThread SocketType:");
            sb.append(this.mSocketType);
            Log.i(str, sb.toString());
            StringBuilder sb2 = new StringBuilder();
            sb2.append("ConnectThread");
            sb2.append(this.mSocketType);
            setName(sb2.toString());
            BluetoothChatService.this.mAdapter.cancelDiscovery();
            try {
                this.mmSocket.connect();
                synchronized (BluetoothChatService.this) {
                    BluetoothChatService.this.mConnectThread = null;
                }
                BluetoothChatService.this.connected(this.mmSocket, this.mmDevice, this.mSocketType);
            } catch (Exception e) {
                String str2 = BluetoothChatService.TAG;
                StringBuilder sb3 = new StringBuilder();
                sb3.append("Connection error ");
                sb3.append(e.toString());
                Log.d(str2, sb3.toString());
                try {
                    this.mmSocket.close();
                } catch (Exception e2) {
                    String str3 = BluetoothChatService.TAG;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("unable to close() ");
                    sb4.append(this.mSocketType);
                    sb4.append(" socket during connection failure");
                    Log.e(str3, sb4.toString(), e2);
                }
                BluetoothChatService.this.connectionFailed();
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (Exception e) {
                String str = BluetoothChatService.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("close() of connect ");
                sb.append(this.mSocketType);
                sb.append(" socket failed");
                Log.e(str, sb.toString(), e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private InputStream mmInStream;
        private OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket bluetoothSocket, String str) {
            InputStream inputStream;
            String str2 = BluetoothChatService.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("create ConnectedThread: ");
            sb.append(str);
            Log.d(str2, sb.toString());
            this.mmSocket = bluetoothSocket;
            OutputStream outputStream = null;
            try {
                inputStream = bluetoothSocket.getInputStream();
                try {
                    outputStream = bluetoothSocket.getOutputStream();
                } catch (IOException e) {

                    Log.e(BluetoothChatService.TAG, "temp sockets not created", e);
                    this.mmInStream = inputStream;
                    this.mmOutStream = outputStream;
                }
            } catch (IOException e2) {

                inputStream = null;
                Log.e(BluetoothChatService.TAG, "temp sockets not created", e2);
                this.mmInStream = inputStream;
                this.mmOutStream = outputStream;
            }
            this.mmInStream = inputStream;
            this.mmOutStream = outputStream;
        }

        public void run() {
            Log.i(BluetoothChatService.TAG, "BEGIN mConnectedThread");
            byte[] bArr = new byte[512];
            while (true) {
                try {
                    int read = this.mmInStream.read(bArr);
                    int[] iArr = new int[read];
                    StringBuilder sb = new StringBuilder();
                    sb.append("");
                    sb.append(read);
                    Log.d("Byte readed", sb.toString());
                    for (int i = 0; i < read; i++) {
                        iArr[i] = bArr[i] & 255;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("");
                        sb2.append(iArr[i]);
                        Log.d("tempbuffer ", sb2.toString());
                    }
                    BluetoothChatService.this.mHandler.obtainMessage(2, read, -1, new String(iArr, 0, read).toString()).sendToTarget();
                } catch (IOException e) {
                    Log.e(BluetoothChatService.TAG, "disconnected", e);
                    BluetoothChatService.this.connectionLost();
                    return;
                }
            }
        }

        public void write(byte[] bArr) {
            try {
                this.mmOutStream.write(bArr);
                BluetoothChatService.this.mHandler.obtainMessage(3, -1, -1, bArr).sendToTarget();
            } catch (IOException e) {
                Log.e(BluetoothChatService.TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(BluetoothChatService.TAG, "close() of connect socket failed", e);
            }
        }
    }

    public BluetoothChatService(Context context, Handler handler) {
        this.mHandler = handler;
    }

    private synchronized void setState(int i) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("setState() ");
        sb.append(this.mState);
        sb.append(" -> ");
        sb.append(i);
        Log.d(str, sb.toString());
        this.mState = i;
        this.mHandler.obtainMessage(1, i, -1).sendToTarget();
    }

    public synchronized int getState() {
        return this.mState;
    }

    public synchronized void start() throws IOException {
        Log.d(TAG, "start");
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        setState(1);
        if (this.mSecureAcceptThread == null) {
            this.mSecureAcceptThread = new AcceptThread(true);
            this.mSecureAcceptThread.start();
        }
    }

    public synchronized void connect(BluetoothDevice bluetoothDevice, boolean z) throws IOException {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("connect to: ");
        sb.append(bluetoothDevice);
        Log.d(str, sb.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append(this.mState);
        sb2.append("");
        Log.d("Connection state", sb2.toString());
        if (this.mState == 2 && this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        this.mConnectThread = new ConnectThread(bluetoothDevice, z);
        this.mConnectThread.start();
        setState(2);
    }

    public synchronized void connected(BluetoothSocket bluetoothSocket, BluetoothDevice bluetoothDevice, String str) {
        String str2 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("connected, Socket Type:");
        sb.append(str);
        Log.d(str2, sb.toString());
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mSecureAcceptThread != null) {
            this.mSecureAcceptThread.cancel();
            this.mSecureAcceptThread = null;
        }
        this.mConnectedThread = new ConnectedThread(bluetoothSocket, str);
        this.mConnectedThread.start();
        Message obtainMessage = this.mHandler.obtainMessage(4);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.DEVICE_NAME, bluetoothDevice.getName());
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
        setState(3);
    }

    public synchronized void stop() {
        Log.d(TAG, "stop");
        if (this.mConnectThread != null) {
            this.mConnectThread.cancel();
            this.mConnectThread = null;
        }
        if (this.mConnectedThread != null) {
            this.mConnectedThread.cancel();
            this.mConnectedThread = null;
        }
        if (this.mSecureAcceptThread != null) {
            this.mSecureAcceptThread.cancel();
            this.mSecureAcceptThread = null;
        }
        setState(0);
    }

    public void write(byte[] bArr) {
        synchronized (this) {
            if (this.mState == 3) {
                ConnectedThread connectedThread = this.mConnectedThread;
                connectedThread.write(bArr);
            }
        }
    }

    /* access modifiers changed from: private */
    public void connectionFailed() {
        Message obtainMessage = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Fail");
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

    /* access modifiers changed from: private */
    public void connectionLost() {
        Message obtainMessage = this.mHandler.obtainMessage(5);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TOAST, "Disconnected");
        obtainMessage.setData(bundle);
        this.mHandler.sendMessage(obtainMessage);
    }

}