package com.qq.vip.singleangel.communcationwithserver.Time;

import android.os.Handler;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 时间操作相关类
 * 考虑时钟同步问题和其他定时器的问题
 * 后期可加入逻辑时钟
 * Created by singl on 2017/11/6.
 */

public class TimeTools {
    private Timer timer;
    private NtpTime ntpTime;

    public TimeTools(){
        init();
    }

    public  void syncTime(){
        ntpTime.startSyncTime();
    }

    public void stopSyncTime(){
        ntpTime.stopSyncTime();
    }

    public void init(){
        timer = new Timer();
        ntpTime = new NtpTime();
        setTimerTask();
    }

    private void setTimerTask() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                Message message = new Message();
                message.what = 1;
                doActionHandler.sendMessage(message);
            }
        }, 1000, 1000/* 表示1000毫秒之後，每隔1000毫秒執行一次 */);
    }

    /**
     * do some action
     */
    private Handler doActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int msgId = msg.what;
            switch (msgId) {
                case 1:
                    // do some action
                    break;
                default:
                    break;
            }
        }
    };

}
