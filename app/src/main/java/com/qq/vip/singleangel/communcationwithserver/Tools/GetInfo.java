package com.qq.vip.singleangel.communcationwithserver.Tools;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.util.Log;

import com.qq.vip.singleangel.communcationwithserver.ClassDefined.HeartBeatsInfo;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by singl on 2017/11/6.
 */

public class GetInfo {

    private Context context;

    public GetInfo(Context context){
        this.context = context;
    }

    public HeartBeatsInfo getInfo(){
        HeartBeatsInfo info = new HeartBeatsInfo(getIpAdd(),getMacAdd());
        return info;
    }
    /**
     * 获得wifi 连接下的ip地址
     * 获得LTE 连接下的ip地址未实现，考虑DisplayName在不同机型是否一致
     * @return
     */
    public String getIpAdd(){
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 需要权限
     *<uses-permission android:name="android.permission.ACCESS_WIFI_STATE"></uses-permission>
     *
     * @return
     */
    public String getMacAdd(){
        String address = null;
        // 把当前机器上的访问网络接口的存入 Enumeration集合中
        Enumeration<NetworkInterface> interfaces = null;
        try {
            interfaces = NetworkInterface.getNetworkInterfaces();
        } catch (SocketException e) {
            e.printStackTrace();
        }
        Log.d("TEST_BUG", " interfaceName = " + interfaces );
        while (interfaces.hasMoreElements()) {
            NetworkInterface netWork = interfaces.nextElement();
            // 如果存在硬件地址并可以使用给定的当前权限访问，则返回该硬件地址（通常是 MAC）。
            byte[] by = new byte[0];
            try {
                by = netWork.getHardwareAddress();
            } catch (SocketException e) {
                e.printStackTrace();
            }
            if (by == null || by.length == 0) {
                continue;
            }
            StringBuilder builder = new StringBuilder();
            for (byte b : by) {
                builder.append(String.format("%02X:", b));
            }
            if (builder.length() > 0) {
                builder.deleteCharAt(builder.length() - 1);
            }
            String mac = builder.toString();
            Log.d("TEST_BUG", "interfaceName="+netWork.getName()+", mac="+mac);
            // 从路由器上在线设备的MAC地址列表，可以印证设备Wifi的 name 是 wlan0
            if (netWork.getName().equals("wlan0")) {
                Log.d("TEST_BUG", " interfaceName ="+netWork.getName()+", mac="+mac);
                address = mac;
            }
        }

        return address;
    }
}

