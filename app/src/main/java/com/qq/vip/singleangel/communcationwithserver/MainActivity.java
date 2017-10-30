package com.qq.vip.singleangel.communcationwithserver;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 连接服务器的Demo
 */
public class MainActivity extends AppCompatActivity {

    private Button btConnect;   //连接按钮
    private TextView tvMessage;  //显示从接收服务器的数据
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnect = (Button) findViewById(R.id.connect);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 注意这里因为需要连接Socket和对UI进行操作。所以使用AsyncTask
                 * 在main process执行会报错 在主线程进行网络操作
                 * 在Thread中执行会报错，在非主线程执行UI操作，即tvMessage.setText
                 */
                connectAsyncTask asyncTask = new connectAsyncTask(tvMessage);  //
                asyncTask.execute();
            }
        });
    }



    public class connectAsyncTask extends AsyncTask<Void,Void,String>{
        private final TextView textView;

        public connectAsyncTask(TextView textView){
            this.textView = textView;
        }

        @Override
        protected String doInBackground(Void... params) {
            Socket socket = new Socket();  //新建Socket
            try {
                socket.bind(null);
                /**
                 * 连接时需要IP address. port . 5000是重连时间，应该是5S
                 */
                socket.connect(new InetSocketAddress("120.78.167.211",34567),5000);
                //接受服务器信息
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
                //得到服务器信息
                String msg = in.readLine();
                in.close();
                return msg;
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }finally {
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
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }

}
