package com.qq.vip.singleangel.communcationwithserver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.Action;
import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;
import com.qq.vip.singleangel.communcationwithserver.ClassDefined.Request;
import com.qq.vip.singleangel.communcationwithserver.Tools.GetInfo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static android.media.audiofx.AudioEffect.ERROR;

/**
 * 连接服务器的Demo
 */
public class MainActivity extends AppCompatActivity {

    private Button btConnect;   //连接按钮
    private Button btSend;
    private TextView ip_add;  //显示从接收服务器的数据
    private TextView mac_add;
    private TextView action;
    private GetInfo info;
 //   private HeartBeatsService heartBeatsService;

    //服务器向客户端发送的命令
    public static final String START_DISCOVER = "StartDiscover";
    public static final String STOP_DISCOVER = "StopDiscover";
    public static final String START_CONNECT = "StartConnect";
    public static final String STOP_CONNECT = "StopConnect";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnect = (Button) findViewById(R.id.connect);
        btSend = (Button) findViewById(R.id.send);
        ip_add = (TextView) findViewById(R.id.ip_add);
        mac_add = (TextView) findViewById(R.id.mac_add);
        action = (TextView) findViewById(R.id.action);
        info = new GetInfo();

       // heartBeatsService = new HeartBeatsService();

        startService(new Intent(MainActivity.this, HeartBeatsService.class));
        /**
        info = new GetInfo(this);
        ip_add.setText(info.getIpAdd());
        mac_add.setText(info.getMacAdd());
        */
       // new Thread(new GetAction(this)).start();

    //    Thread hbThread = new Thread(new HeartBeats(this));
     //   hbThread.start();
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Request request = new Request(Request.INITIATIVE);
                sendRequestAsyncTask asyncTask = new sendRequestAsyncTask(request,action);
                asyncTask.execute();

            }
        });

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 注意这里因为需要连接Socket和对UI进行操作。所以使用AsyncTask
                 * 在main process执行会报错 在主线程进行网络操作
                 * 在Thread中执行会报错，在非主线程执行UI操作，即tvMessage.setText
                 */

                connectAsyncTask asyncTask = new connectAsyncTask(ip_add, mac_add, info.getInfo());
                asyncTask.execute();


            }
        });

    }

    public class sendRequestAsyncTask extends AsyncTask<Void,Void,Action>{
        private final Request request;
        private final TextView tv_action;
        public sendRequestAsyncTask(Request request, TextView tv_action) {
            this.request = request;
            this.tv_action = tv_action;
        }

        @Override
        protected Action doInBackground(Void... params) {
            Socket socket = new Socket();  //新建Socket
            try {
                socket.bind(null);
                /**
                 * 连接时需要IP address. port . 5000是重连时间，应该是5S
                 */
                socket.connect(new InetSocketAddress("120.78.167.211",22333),5000);

                 OutputStream outputStream = socket.getOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                 objectOutputStream.writeObject(request);


                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                Action action = (Action) objectInputStream.readObject();
                return action;



            } catch (UnknownHostException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Action action) {
            if (action != null){
            //    tv_action.setText(action.getAction());
            }else {
                Log.d("ACTION","action is null");
            }
        }
    }

    int i = 0;

    public class connectAsyncTask extends AsyncTask<Void,Void,HeartBeatsInfo>{
        private final TextView ip_add;
        private final TextView mac_add;
        private final HeartBeatsInfo info;
        public connectAsyncTask(TextView ip_add, TextView mac_add, HeartBeatsInfo info) {
            this.ip_add = ip_add;
            this.mac_add = mac_add;
            this.info = info;
        }

        @Override
        protected HeartBeatsInfo doInBackground(Void... params) {
            Socket socket = new Socket();  //新建Socket
            try {
                socket.bind(null);
                /**
                 * 连接时需要IP address. port . 5000是重连时间，应该是5S
                 */
                socket.connect(new InetSocketAddress("120.78.167.211",33356),5000);
                /**
                 * 写入自身的IP、MAC地址

                OutputStream outputStream = socket.getOutputStream();
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                objectOutputStream.writeObject(info);
                */
                //接受服务器信息
                InputStream inputStream = socket.getInputStream();
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                //得到服务器信息
                HeartBeatsInfo info = (HeartBeatsInfo) objectInputStream.readObject();
                inputStream.close();
                return info;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(HeartBeatsInfo heartBeatsInfo) {
            if (heartBeatsInfo != null){
                ip_add.setText(heartBeatsInfo.getIpAdd() + "  "+ String.valueOf(i++));
                mac_add.setText(heartBeatsInfo.getMacAdd());
            }
        }
    }

    public Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Action act = (Action) msg.obj;
            switch (msg.what){
                case 1:
                    action.setText(act.getAction());
                    break;
                default:
                    break;
            }
            switch (act.getAction()){
                case START_DISCOVER:
                    action.setText(START_DISCOVER);
                    break;
                case STOP_DISCOVER:
                    action.setText(STOP_DISCOVER);
                    break;
                case START_CONNECT:
                    action.setText(START_CONNECT);
                    break;
                case STOP_CONNECT:
                    action.setText(STOP_CONNECT);
                    break;
            }
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String act = intent.getAction();
            switch (act){
                case START_DISCOVER:
                    action.setText(START_DISCOVER);
                    break;
                case STOP_DISCOVER:
                    action.setText(STOP_DISCOVER);
                    break;
                case START_CONNECT:
                    action.setText(START_CONNECT);
                    break;
                case STOP_CONNECT:
                    action.setText(STOP_CONNECT);
                    break;
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(START_CONNECT);
        filter.addAction(START_DISCOVER);
        filter.addAction(STOP_CONNECT);
        filter.addAction(STOP_DISCOVER);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
