package com.qq.vip.singleangel.communcationwithserver;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;
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

/**
 * 连接服务器的Demo
 */
public class MainActivity extends AppCompatActivity {

    private Button btConnect;   //连接按钮
    private Button btSend;
    private TextView ip_add;  //显示从接收服务器的数据
    private TextView mac_add;
    private HeartBeats heartBeats;
    private GetInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnect = (Button) findViewById(R.id.connect);
        btSend = (Button) findViewById(R.id.send);
        ip_add = (TextView) findViewById(R.id.ip_add);
        mac_add = (TextView) findViewById(R.id.mac_add);
        info = new GetInfo(this);
        /**
        info = new GetInfo(this);
        ip_add.setText(info.getIpAdd());
        mac_add.setText(info.getMacAdd());
        */

        Thread hbThread = new Thread(new HeartBeats(this));
        hbThread.start();
        btSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


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
        /**
         * heartBeats = new HeartBeats(this);
         Thread hbThread = new Thread(heartBeats);
         hbThread.start();
         */

    }



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
                ip_add.setText(heartBeatsInfo.getIpAdd());
                mac_add.setText(heartBeatsInfo.getMacAdd());
            }
        }
    }

}
