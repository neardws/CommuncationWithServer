package com.qq.vip.singleangel.communcationwithserver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;
import com.qq.vip.singleangel.communcationwithserver.Time.TimeTools;
import com.qq.vip.singleangel.communcationwithserver.Tools.GetInfo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.Time;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by singl on 2017/11/6.
 */

public class HeartBeats implements Runnable {
    private static final String TAG = "HeartBeats";

    private Socket client;
    private TimeTools timeTools;
    private Timer timer;
    private Context context;
    private GetInfo getInfo;

    public HeartBeats(Context context){
        timeTools = new TimeTools();
       // timeTools.syncTime();  //时间同步
        this.context = context;
        getInfo = new GetInfo(context);
    }
    @Override
    public void run() {
        Socket socket = new Socket();
        try {
            socket.bind(null);
            /**
             * * 连接时需要IP address. port . 5000是重连时间，应该是5S
             */
            socket.connect(new InetSocketAddress("120.78.167.211",34567),5000);
            client = socket;
           // timer = new Timer();
           // setTimerTask();
            SendHeartBeats(client, getInfo.getInfo());

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void SendHeartBeats(Socket socket, HeartBeatsInfo info){
        OutputStream outputStream = null;
        try {
            outputStream = socket.getOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(info);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    /**
    private void setTimerTask() {
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int i = second % 10;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (client != null){
                    SendHeartBeats(client,getInfo.getInfo());
                }else {
                    Log.d(TAG, "client is null");
                }
            }
        }, 1000, 1000 * 10);
    }
    */


    /**
     * do some action
     * 抛出异常NetworkOnMainThreadException
     */
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    if (client != null){
                        SendHeartBeats(client,getInfo.getInfo());
                    }else {
                        Log.d(TAG, "client is null");
                    }

                    // do some action
                    break;
                default:
                    break;
            }
        }
    };
}
