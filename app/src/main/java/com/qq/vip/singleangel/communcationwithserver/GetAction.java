package com.qq.vip.singleangel.communcationwithserver;

import android.os.Message;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.Action;
import com.qq.vip.singleangel.communcationwithserver.Tools.GetInfo;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by singl on 2017/11/8.
 */

public class GetAction implements Runnable {
    private MainActivity mainActivity;
    public GetAction(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(22233);
            while (true){
                Socket actionSocket = serverSocket.accept();
                try {
                    InputStream inputStream = actionSocket.getInputStream();
                    ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                    Action action = (Action) objectInputStream.readObject();
                    Message message = new Message();
                    message.obj = action;
                    mainActivity.doActionHandler.sendMessage(message);
                    objectInputStream.close();
                    inputStream.close();
                }catch (IOException e){
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
