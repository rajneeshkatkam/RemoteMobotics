package com.raj.remotemobotics;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;
import java.util.List;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    Button openCamButton,kpSend,kiSend,kdSend;
    EditText KpInput,KiInput,KdInput;

    Handler handler;

    private TextView yaw,pitch,roll,statusTextView;

    private String remoteDevice_IP="192.168.43.66";  ///Remote device Ip
    private int remoteDevice_Port= 6666;        ///Remote device Listening port

    private String hotspot_IP="192.168.43.1";  ///Remote device Ip
    private int hotspot_Port= 1111;        ///Remote device Listening port

    private OSCPortOut sender;
    private OSCPortIn receiver;


    Boolean sendPIDFlag=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        openCamButton = (Button) findViewById(R.id.openCamButton);
        kpSend = (Button) findViewById(R.id.kpSend);
        kiSend = (Button) findViewById(R.id.kiSend);
        kdSend = (Button) findViewById(R.id.kdSend);

        KpInput= (EditText) findViewById(R.id.KpInput);
        KiInput= (EditText) findViewById(R.id.KiInput);
        KdInput= (EditText) findViewById(R.id.KdInput);

        KpInput.setText("2.12");
        KiInput.setText("0");
        KdInput.setText("1.11");



        statusTextView= (TextView) findViewById(R.id.statusTextView);
        yaw= (TextView) findViewById(R.id.yaw);
        pitch= (TextView) findViewById(R.id.pitch);
        roll= (TextView) findViewById(R.id.roll);



    }


    ///Button Click Functions

    public void sendPIDValues(View view) {

        Thread thread = new Thread() {
            @Override
            public void run() {
                sendPacketsToHotSpotDevice();
            }
        };
        thread.start();

    }
    ///-------------------------------------------------------------------------------------------




    private void sendPacketsToHotSpotDevice() {

        Log.i("portError","Packet Called");

        if (sender != null) {
            try {

                Log.i("portError","Packet in");

                ///////Send Messages with arguments a multiple of 2------Very Important

                OSCMessage message = new OSCMessage("/motorValues");
                message.addArgument(Float.valueOf(String.valueOf(KpInput.getText())));
                message.addArgument(Float.valueOf(String.valueOf(KiInput.getText())));
                message.addArgument(Float.valueOf(String.valueOf(KdInput.getText())));
                message.addArgument(2);
                //message.addArgument(roll);
                //Log.i("portError","Packet Created");


                sender.send(message);
                Log.i("portError","Packet Sent");

            } catch (Exception e) {

                Log.i("portError",e.toString());
                // Error handling for some error
            }
        }

    }


    @Override
    protected void onResume() {
        super.onResume();


        /// OSC Initialization
        ///For sending messages to the Hotspot
        try {
            // Connect to some IP address and port
            sender = new OSCPortOut(InetAddress.getByName(hotspot_IP), hotspot_Port);
        } catch (Exception e) {
            // Error handling for any other errors
            Log.i("portError",e.getMessage());
        }


        ///For receiving messages from Hotspot
        try {
             receiver = new OSCPortIn(remoteDevice_Port);  ///Listening port number
            Log.i("portError","Sucess");
            OSCListener listener = new OSCListener() {
                public void acceptMessage(Date time, OSCMessage message) {

                    List<Object> args = message.getArguments();

                    //Log.i("ArraySize",message.getArguments().toString());
                    yaw.setText(String.valueOf(args.get(0)));
                    pitch.setText(String.valueOf(args.get(1)));
                    roll.setText(String.valueOf(args.get(2)));

                    ///received message
                }
            };
            receiver.addListener("/sensorValues", listener); //////Listening for Tag = '/test'
            receiver.startListening();
        } catch (SocketException e) {
            Log.d("OSCSendInitalisation", e.getMessage());
        }




    }

    @Override
    protected void onPause() {
        super.onPause();
        receiver.close();
    }

}
