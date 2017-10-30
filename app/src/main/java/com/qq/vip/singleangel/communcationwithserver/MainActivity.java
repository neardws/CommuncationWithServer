package com.qq.vip.singleangel.communcationwithserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private Button btConnect;
    private TextView tvMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btConnect = (Button) findViewById(R.id.connect);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        btConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Socket socket = null;
                try {
                    InetAddress serverAdd = InetAddress.getByName("120.78.167.211");
                    socket = new Socket(serverAdd,34567);
                    //接受服务器信息
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
                    //得到服务器信息
                    String msg = in.readLine();
                    tvMessage.setText(msg);
                    in.close();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    try{
                        socket.close();
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                }

            }
        });
    }

}
