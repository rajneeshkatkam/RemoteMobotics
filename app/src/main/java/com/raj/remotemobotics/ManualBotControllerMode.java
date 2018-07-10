package com.raj.remotemobotics;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

public class ManualBotControllerMode extends AppCompatActivity {


    @SuppressLint("StaticFieldLeak")
    public static Activity fa;



    //private Handler repeatUpdateHandler = new Handler();
    private OSCPortOut senderArduino;
    Boolean zeroPacket=false;
    int onClickTouchSensitivity =1,intialPWM=400, sleepTime =4;   ///For single Click , no. of packets to be sent/No.of times loop should run
    int pwmValue=0;

    SeekBar pwmSeekBar;

    AtomicBoolean botForwardMotionDownFlag=new AtomicBoolean(false);
    AtomicBoolean botBackwardMotionDownFlag=new AtomicBoolean(false);
    AtomicBoolean botLeftMotionDownFlag=new AtomicBoolean(false);
    AtomicBoolean botRightMotionDownFlag=new AtomicBoolean(false);   ////MotionEvent.ActionDown , then it is true....used to check the action of the click

    private TextView pwmValueText;


    private String arduino_IP = "192.168.43.55";  ///Static IP for Arduino
    private int ardunio_Port = 5555;              ///Port where Arduino is listening


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_bot_controller_mode);


        fa=this;

        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        Button forward = findViewById(R.id.forward);
        Button backward=findViewById(R.id.backward);
        Button left=findViewById(R.id.left);
        Button right=findViewById(R.id.right);
        Button spotLeft=findViewById(R.id.spotLeft);
        Button spotRight=findViewById(R.id.spotRight);
        Button stop=findViewById(R.id.stop);


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
                    botForwardMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botForwardMotionDownFlag.set(true);
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botForwardMotionDownFlag.get())
                            {
                                botForward();

                                try {
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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
                    botBackwardMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botBackwardMotionDownFlag.set(true);

                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botBackwardMotionDownFlag.get())
                            {
                                botBackward();

                                try {
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    });

                    motionListenerThread.start();
                }

                return false;
            }
        });


        stop.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botBackwardMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botBackwardMotionDownFlag.set(true);

                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botBackwardMotionDownFlag.get())
                            {
                                try {
                                    botStop();
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
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
                    botLeftMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botLeftMotionDownFlag.set(true);
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botLeftMotionDownFlag.get())
                            {
                                botLeft();

                                try {
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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
                    botRightMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botRightMotionDownFlag.set(true);
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botRightMotionDownFlag.get())
                            {
                                botRight();

                                try {
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                    motionListenerThread.start();
                }


                return false;
            }
        });



        spotLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botRightMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botRightMotionDownFlag.set(true);
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botRightMotionDownFlag.get())
                            {
                                botSpotLeft();

                                try {
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                    });

                    motionListenerThread.start();
                }


                return false;
            }
        });




        spotRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction()==MotionEvent.ACTION_UP) {
                    botRightMotionDownFlag.set(false);
                    try {
                        botStop();
                        botStop();
                        botStop();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                else if(event.getAction()==MotionEvent.ACTION_DOWN) {
                    botRightMotionDownFlag.set(true);
                    Thread motionListenerThread=new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while(botRightMotionDownFlag.get())
                            {
                                botSpotRight();

                                try {
                                    sleep(sleepTime);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

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
                sendPacketsToArduino(pwmValue,0,0,0,1);
            }
        };

        thread.start();

    }


    public void botRight() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,0,1,0,0);
            }
        };

        thread.start();

    }


    public void botSpotLeft() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,1,0,0,1);
            }
        };

        thread.start();

    }


    public void botSpotRight() {
        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(pwmValue,0,1,1,0);
            }
        };

        thread.start();

    }


    public void botStop() throws InterruptedException {

        sleep(sleepTime);

        Thread thread =new Thread(){

            @Override
            public void run() {
                sendPacketsToArduino(0,1,1,1,1);
            }
        };

        thread.start();



    }


    ///////////////BOT Motion Functions Ends/////////////////////////////////////






    ///Packet Sending Function

    private void sendPacketsToArduino(int pwm,int leftPin1,int leftPin2,int rightPin1,int rightPin2) {


        if (senderArduino != null) {
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
                //sleep(1);

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

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    botStop();
                    botStop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    botStop();
                    botStop();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        thread.start();

    }
}








/*   forward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                zeroPacket=false;
                while(count< onClickTouchSensitivity) {
                    botForward();
                    count++;
                }
                zeroPacket=true;

                //zeroPacket=false;
            }
        });

        backward.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int count=0;
                zeroPacket=false;
                while(count< onClickTouchSensitivity) {
                    botBackward();
                    count++;
                }
                zeroPacket=true;

            }
        });

        forward.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                autoIncrement = true;
                zeroPacket=false;
                //repeatUpdateHandler.post(new RepetitiveUpdater());
                return false;
            }
        });

        backward.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                autoDecrement = true;
                zeroPacket=false;
                //repeatUpdateHandler.post(new RepetitiveUpdater());
                return false;
            }
        });




    ////For continous click events
    class RepetitiveUpdater implements Runnable {

        @Override
        public void run() {
            long REPEAT_DELAY = 0;
            if (autoIncrement) {
                botForward();
                repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                //Log.i("portError","forward");
            } else if (autoDecrement) {
                botBackward();
                repeatUpdateHandler.postDelayed(new RepetitiveUpdater(), REPEAT_DELAY);
                //Log.i("portError","backward");
            }
        }

    }


*/






