package com.raj.remotemobotics;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortOut;

import java.net.InetAddress;

import static java.lang.Thread.sleep;

public class ManualBotControllerMode extends AppCompatActivity {

    //private Handler repeatUpdateHandler = new Handler();
    private OSCPortOut senderArduino;
    Boolean zeroPacket=false;
    int onClickTouchSensitivity =5,intialPWM=400;   ///For single Click , no. of packets to be sent/No.of times loop should run
    int pwmValue=0;

    SeekBar pwmSeekBar;

    Boolean botForwardMotionDownFlag=false,botBackwardMotionDownFlag=false,botLeftMotionDownFlag=false,botRightMotionDownFlag=false;   ////MotionEvent.ActionDown , then it is true....used to check the action of the click

    private TextView pwmValueText;


    private String arduino_IP = "192.168.43.55";  ///Static IP for Arduino
    private int ardunio_Port = 5555;              ///Port where Arduino is listening


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_bot_controller_mode);

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Button forward = findViewById(R.id.forward);
        Button backward=findViewById(R.id.backward);
        Button left=findViewById(R.id.left);
        Button right=findViewById(R.id.right);


        pwmSeekBar=findViewById(R.id.pwmSeekBar);
        pwmSeekBar.setMin(0);
        pwmSeekBar.setMax(1000);
        pwmSeekBar.setProgress(intialPWM);
        pwmValue=intialPWM;


        pwmValueText=findViewById(R.id.pwmValueText);
        pwmValueText.setText(String.valueOf(pwmSeekBar.getProgress()));



        ////////////////////CLICK LISTENERS CODE STARTS//////////////////////////


        forward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botForwardMotionDownFlag = false;
                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botForwardMotionDownFlag=true;
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botForwardMotionDownFlag)
                            {
                                int count = 0;
                                zeroPacket = false;
                                while (count < onClickTouchSensitivity) {
                                    botForward();
                                    count++;
                                }
                                zeroPacket = true;
                            }
                        }
                    });

                    motionListenerThread.start();
                }


                return false;
            }
        });



        backward.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botBackwardMotionDownFlag=false;
                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botBackwardMotionDownFlag=true;

                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botBackwardMotionDownFlag)
                            {
                                int count = 0;
                                zeroPacket = false;
                                while (count < onClickTouchSensitivity) {
                                    botBackward();
                                    count++;
                                }
                                zeroPacket = true;
                            }
                        }
                    });

                    motionListenerThread.start();
                }

                return false;
            }
        });



        left.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botLeftMotionDownFlag = false;
                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botLeftMotionDownFlag=true;
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botLeftMotionDownFlag)
                            {
                                int count = 0;
                                zeroPacket = false;
                                while (count < onClickTouchSensitivity) {
                                    botLeft();
                                    count++;
                                }
                                zeroPacket = true;
                            }
                        }
                    });

                    motionListenerThread.start();
                }


                return false;
            }
        });


        right.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botRightMotionDownFlag = false;
                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botRightMotionDownFlag=true;
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botRightMotionDownFlag)
                            {
                                int count = 0;
                                zeroPacket = false;
                                while (count < onClickTouchSensitivity) {
                                    botRight();
                                    count++;
                                }
                                zeroPacket = true;
                            }
                        }
                    });

                    motionListenerThread.start();
                }


                return false;
            }
        });


        /////////////////////CLICK LISTENERS CODE ENDS ///////////////////////////////




        //////////////PWM SeekBar Listener Starts//////////////////////////


        pwmSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                pwmValue= seekBar.getProgress();
                pwmValueText.setText(String.valueOf(seekBar.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        ////////////////PWM SeekBar Code Ends////////////////////////////////


    }



    ///////////////BOT Motion Functions Starts/////////////////////////////////////


    public void botForward() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,0,1,0,1);
            }
        };

        thread.start();

    }

    public void botBackward() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,1,0,1,0);
            }
        };

        thread.start();

    }

    public void botLeft() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,1,0,0,1);
            }
        };

        thread.start();

    }


    public void botRight() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,0,1,1,0);
            }
        };

        thread.start();

    }


    ///////////////BOT Motion Functions Ends/////////////////////////////////////






    ///Packet Sending Function

    private void sendPacketsToArduino(int pwm,int leftPin1,int leftPin2,int rightPin1,int rightPin2) {

        //Log.i("portError","Packet Called");

        if (!zeroPacket && senderArduino != null) {
            try {

                ///////Send Messages with arguments a multiple of 2------Very Important
                Log.i("portError","forward Entered");
                OSCMessage message = new OSCMessage("/motorValues");
                message.addArgument((float) pwm); //Left Motor PWM Value
                message.addArgument((float) pwm); //Right Motor PWM Value

                message.addArgument(leftPin1); //Left motor direction pin one    ---- pin1-0 and pin2-1  (Left Motor Forwards)
                message.addArgument(leftPin2); //Left motor direction pin two

                message.addArgument(rightPin1); //Right motor direction pin one   ---- pin1-0 and pin2-1  (Right Motor Forwards)
                message.addArgument(rightPin2); //Right motor direction pin two

                senderArduino.send(message);

                Log.i("portError",message.getArguments().toString());
                //zeroPacket=true;
                //Log.i("portError","Packet Sent");
                sleep(1);

            } catch (Exception e) {

                Log.i("portError",e.getMessage());
                //Log.i("portErrorArduino",e.toString());
                // Error handling for some error
            }
        }

        if (zeroPacket)
        {
            try {

                ///////Send Messages with arguments a multiple of 2------Very Important
                Log.i("portError"," Zero forward Entered");
                OSCMessage message = new OSCMessage("/motorValues");
                message.addArgument(0f); //Left Motor PWM Value
                message.addArgument(0f); //Right Motor PWM Value

                message.addArgument(1); //Left motor direction pin one
                message.addArgument(1); //Left motor direction pin two

                message.addArgument(1); //Right motor direction pin one
                message.addArgument(1); //Right motor direction pin two
                //message.addArgument(roll);
                //Log.i("portError","Packet Created");

                senderArduino.send(message);

                Log.i("portError",message.getArguments().toString());
                //Log.i("portError","Packet Sent");
                sleep(1);

            } catch (Exception e) {

                Log.i("portError",e.getMessage());
                //Log.i("portErrorArduino",e.toString());
                // Error handling for some error
            }

        }

    }



    @Override
    protected void onResume() {
        super.onResume();

        /// OSC Initialization
        try {
            Log.i("portError","Entered");
            // Connect to Arduino IP address and port
            senderArduino = new OSCPortOut(InetAddress.getByName(arduino_IP), ardunio_Port);
            Log.i("portError","Sucess");
        } catch (Exception e) {
            // Error handling for any other errors
            Log.i("portError",e.getMessage());
        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        int count=0;
        while(count<onClickTouchSensitivity) {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    sendPacketsToArduino(0,1,1,1,1);
                }
            };

            thread.start();
            count++;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        int count=0;
        while(count<onClickTouchSensitivity) {
            Thread thread = new Thread() {

                @Override
                public void run() {
                    sendPacketsToArduino(0,1,1,1,1);
                }
            };

            thread.start();
            count++;
        }



    }
}
