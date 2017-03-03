package com.example.shaun.shaunandroidtest;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
//    RobotControl rc = new RobotControl();


    //public BluetoothService btServ = BluetoothUI.BLUETOOTH_SERVICE;
    private Button left;
    private Button right;
    private Button forward;
    private Button backward;
    private Button turnLeft;
    private Button turnRight;
    private Button fast;
    private Button explore;
    public TextView mBTStatus;
    public TextView mRobotStatus;
    //public ArrayAdapter<String> mRobotStatusAdapter;
//    private Button button_bt_settings;
    private MyService mService;
    private boolean mBound;
    public String control="n";
    private static final String TAG = "BATMAN";
    private BluetoothUI BTui;
    Timer timer=new Timer();
    public static MainActivity mInstance;
    public static MainActivity getInstance() {
        return mInstance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controller);
        setupControl();
        BluetoothUI BTui = new BluetoothUI();
        AppServiceController.getInstance().startService();
        mInstance = this;
        mRobotStatus = (TextView)findViewById(R.id.robotStatus);
//        if (savedInstanceState == null) {
//            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//            BluetoothUI fragment = new BluetoothUI();
//            transaction.replace(R.id.activity_controller, fragment);
//            transaction.commit();
//        }

        //robotStatus.setText("Connected to: "+mService.mConnectedDeviceName);
    }

    public String getControl(){
        return control;
        //ctrlMsg(control);
    }

    public void setupControl(){
        // Turn left
        left = (Button) findViewById(R.id.ButtonLeft);
        left.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //String msg = "a";
                //sendMessage(msg);
                //outputFeedback("MovingL");
                control="sl";
                sendControl(control);
                //endControl(control);
            }
        });

        // Turn Right
        right = (Button) findViewById(R.id.ButtonRight);
        right.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control="sr";
                sendControl(control);
                //sendControl(control);
                //String msg = "d";
                //sendMessage(msg);
                //outputFeedback("MovingR");

            }
        });

        // Turn Back
        backward = (Button) findViewById(R.id.ButtonDown);

        backward.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                control="r";
                sendControl(control);
                //sendControl(control);
                //String msg = "s";
                //sendMessage(msg);
                //outputFeedback("MovingB");
                // autoSend = false;
            }
        });

        // Go Forward
        forward = (Button) findViewById(R.id.ButtonUP);
        forward.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control="f";
                sendControl(control);
                //String msg = "w";
                //sendMessage(msg);
                //("MovingF");
                //sendControl(control);

            }

        });

        //Rotate Left
        turnLeft = (Button) findViewById(R.id.ButtonTurnL);
        turnLeft.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control="tl";
                sendControl(control);;

            }

        });

        //Rotate Left
        turnRight = (Button) findViewById(R.id.ButtonTurnR);
        turnRight.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control="tr";
                sendControl(control);;

            }

        });

        //Rotate Left
        fast = (Button) findViewById(R.id.ButtonFast);
        fast.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control="beginFastest";
                sendControl(control);;

            }

        });

        //Rotate Left
        explore = (Button) findViewById(R.id.ButtonExplore);
        explore.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                control="beginExplore";
                sendControl(control);;

            }

        });
//        button_bt_settings = (Button) findViewById(R.id.ButtonBT);
//        button_bt_settings.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                startActivity(bt_Screen);
////                bindService(bt_Screen, mConnection, Context.BIND_AUTO_CREATE);
//            }
//
//        });
    }

    public void sendControl(String input){
            if (input.length() > 0) {
                // Get the message bytes and tell the BluetoothChatService to write
                //this.write(send);
//            BTui.write(input.getBytes());
//            btServ.write("lolll".getBytes());
                mService.write(input.getBytes());
            }

    }

    @Override
    public void onStart(){
        super.onStart();
//        Intent intent = new Intent(this, MyService.class);
//        startService(intent);
//        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);

        mService = AppServiceController.getInstance().getService();
        if (mService.getBTState()==mService.STATE_CONNECTED)
        {
            mBTStatus = (TextView)findViewById(R.id.btStatus);
            mBTStatus.setText("Connected to: "+mService.mConnectedDeviceName);
        }
    }
//    private ServiceConnection mConnection = new ServiceConnection() {
//        // Called when the connection with the service is established
//        public void onServiceConnected(ComponentName className, IBinder service) {
//            // Because we have bound to an explicit
//            // service that is running in our own process, we can
//            // cast its IBinder to a concrete class and directly access it.
//            MyService.BTBinder binder = (MyService.BTBinder) service;
//            mService = binder.getService();
//            mBound = true;
//        }
//
//        // Called when the connection with the service disconnects unexpectedly
//        public void onServiceDisconnected(ComponentName className) {
//            Log.e(TAG, "onServiceDisconnected");
//            mBound = false;
//        }
//    };
//    public void startBTServ(View view){
//        //BluetoothService BTServ = new BluetoothService();
////        Intent intent = new Intent(this, BluetoothService.class);
////        startActivity(intent);
////        BTui.start()
//        Intent intent = new Intent(this, BluetoothService.class);
//        startActivity(intent);
//    }
    public void startBTServ(View view){
        //BluetoothService BTServ = new BluetoothService();
//        Intent intent = new Intent(this, BluetoothService.class);
//        startActivity(intent);
//        Intent bt_Screen = new Intent(getApplicationContext(), BluetoothUI.class);

        Intent intent = new Intent(this, BluetoothUI.class);
        startActivity(intent);
//        Intent intent = new Intent(this, BluetoothService.class);
//        startActivity(intent);
    }

    public void updateRobotStatus(String toThis) {
        mRobotStatus.invalidate();
        mRobotStatus.setText(toThis);
    }

}
