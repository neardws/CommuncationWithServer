package com.qq.vip.singleangel.communcationwithserver.ClassDefined;

import java.io.Serializable;
import java.net.Socket;

/**
 * Created by singl on 2017/11/6.
 */
public class HeartBeatsInfo implements Serializable {
    private String macAdd;
    private String ipAdd;
    private Socket client;

    public HeartBeatsInfo(String macAdd, String ipAdd) {
        this.macAdd = macAdd;
        this.ipAdd = ipAdd;
        this.client = null;
    }

    public String getMacAdd() {
        return this.macAdd;
    }
    public String getIpAdd() {
        return this.ipAdd;
    }
    public Socket getSocket() {
        return this.client;
    }
    public void setMacAdd(String macAdd) {
        this.macAdd = macAdd;
    }
    public void setIpAdd(String ipAdd) {
        this.ipAdd = ipAdd;
    }
    public void setSocket(Socket client) {
        this.client = client;
    }

}
