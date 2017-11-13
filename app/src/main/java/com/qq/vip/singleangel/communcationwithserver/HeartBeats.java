package com.qq.vip.singleangel.communcationwithserver;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.Action;
import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;
import com.qq.vip.singleangel.communcationwithserver.Time.TimeTools;
import com.qq.vip.singleangel.communcationwithserver.Tools.GetInfo;

import java.io.IOException;
import java.io.InputStream;
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
    private MainActivity mainActivity;
    private GetInfo getInfo;

    public HeartBeats(MainActivity context){
        timeTools = new TimeTools();
        timeTools.syncTime();  //时间同步
        this.mainActivity = context;
        getInfo = new GetInfo();
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
            client.setKeepAlive(true);
            timer = new Timer();
            setTimerTask();
            setTimerTask2();
            //SendHeartBeats(client, getInfo.getInfo());

        }catch (IOException e){
            e.printStackTrace();
        }

    }
    public void dealWithAction(Action action){
        Message message = new Message();
        message.obj = action;
        mainActivity.doActionHandler.sendMessage(message);
        doActionHandler.obtainMessage(1,message);
    }

    public void SendHeartBeats(Socket socket, HeartBeatsInfo info){
        OutputStream outputStream = null;
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectOutputStream.writeObject(info);
            objectOutputStream.flush();
            socket.shutdownOutput();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void GetAction(Socket socket){
        InputStream inputStream = null;
        try {
            inputStream = socket.getInputStream();
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            Action action = (Action) objectInputStream.readObject();
            dealWithAction(action);
        }catch (IOException e){
            e.printStackTrace();
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }

    private void setTimerTask() {
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int i = second % 2;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (client != null){
                    SendHeartBeats(client,getInfo.getInfo());
                }else {
                    Log.d(TAG, "client is null");
                }
            }
        }, 1000 * i , 1000 * 10);
    }

    private void setTimerTask2(){
        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int i = second % 2;
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (client != null){
                    GetAction(client);
                }else {
                    Log.d(TAG, "client is null");
                }
            }
        }, 1000 * i , 1000);
    }


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
