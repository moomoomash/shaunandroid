package com.example.shaun.shaunandroidtest;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.app.Activity;
import android.widget.Toast;

/**
 * Created by truedemon on 2/3/2017.
 */

public class AppServiceController extends Application {
    private String input;
    private Context mContext;
    private static AppServiceController mInstance;
    private MyService mService; //= new MyService(mInstance);
//    Intent intent = new Intent(this, MyService.class);
    Intent mIntent;
    public static synchronized AppServiceController getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
//        mContext = this;
        mInstance = this;
        mService = new MyService();
        // TODO Auto-generated method stub
        super.onCreate();
    }

    /**
     * The Handler that gets information
     */
    public final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            AppServiceController activity = AppServiceController.this;
            //setSupportActionBar(MainActivity);
            // getSupportActionBar().setDisplayShowHomeEnabled(true);
            switch (msg.what) {
                case MyService.MessageConstants.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1) {
                        case MyService.STATE_CONNECTED:
                            input="Successfully connected";
                            //BluetoothUI.mBluetoothStatus.setText("Successfully connected!");
                            //mBluetoothStatus.setText("Successfully connected to: "+ MyService.mConnectedDeviceName);
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            //mConversationArrayAdapter.clear();
                            break;
                        case MyService.STATE_CONNECTING:
                            input="Connection in progress";
                            //BluetoothUI.mBluetoothStatus.setText("Connection in progress");
                            break;
                        ///case STATE_LISTEN:
                        case MyService.STATE_NONE:
                            input="Disconnected";
                            //BluetoothUI.mBluetoothStatus.setText("Disconnected!");
                            break;
                    }
                    break;
                case MyService.MessageConstants.MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
                    input=writeMessage;
                    BluetoothUI.mConversationArrayAdapter.add("Me:  " + writeMessage);
                    BluetoothUI.mConversationArrayAdapter.notifyDataSetChanged();
                    break;
                case MyService.MessageConstants.MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
                    System.out.println(readMessage);
                    BluetoothUI.mConversationArrayAdapter.add(mService.mConnectedDeviceName+ ":  " + readMessage);
                    BluetoothUI.mConversationArrayAdapter.notifyDataSetChanged();
                    Toast.makeText(getApplicationContext(),readMessage, Toast.LENGTH_SHORT).show();
                    input=readMessage;
                    if(readMessage.contains("status")){
                        System.out.println(readMessage);
                        MainActivity.getInstance().updateRobotStatus(readMessage);
                    }
                    break;
                case MyService.MessageConstants.MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                   mService.mConnectedDeviceName = msg.getData().getString(BluetoothUI.MessageConstants.DEVICE_NAME);
                    if (null != activity) {
                        Toast.makeText(activity, "Connected to ", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case MyService.MessageConstants.MESSAGE_TOAST:
                    if (null != activity) {
                        Toast.makeText(activity, msg.getData().getString(MyService.MessageConstants.TOAST),
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
//                case MessageConstants.MESSAGE_CONTROL:
//                    byte[] controlBuf = (byte[]) msg.obj;
//                    // construct a string from the buffer
//                    String controlMessage = new String(controlBuf);
//                    mConversationArrayAdapter.add("Me:  " + controlMessage);
//                    mConversationArrayAdapter.notifyDataSetChanged();
//                    break;
            }
        }
    };

    public void startService(){
        //start your service

        mService.startService();
        mIntent = new Intent(this, MyService.class);
        bindService(mIntent, mConnection, Context.BIND_AUTO_CREATE);
    }
    public void stopService(){
        //stop service
    }

    public String getMessage(){
        return input;
    }
    public MyService getService(){
        return mService;
    }
    private boolean mBound = false;
    private static final String TAG = "Batman service controller";
    private ServiceConnection mConnection = new ServiceConnection() {
        // Called when the connection with the service is established
        public void onServiceConnected(ComponentName className, IBinder service) {
            // Because we have bound to an explicit
            // service that is running in our own process, we can
            // cast its IBinder to a concrete class and directly access it.
            MyService.BTBinder binder = (MyService.BTBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        // Called when the connection with the service disconnects unexpectedly
        public void onServiceDisconnected(ComponentName className) {
//            Log.e(TAG, "onServiceDCed");
            mBound = false;
        }
    };
}
