package com.example.buiderdream.apehire.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.view.activitys.MainActivity;
import com.umeng.message.UmengMessageService;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import static com.umeng.message.UmengMessageCallbackHandlerService.TAG;

/**
 * Created by 51644 on 2017/3/2.
 */

public class UmengPushService extends UmengMessageService{
    @Override
    public void onMessage(Context context, Intent intent) {
        String message=intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            UMessage msg=new UMessage(new JSONObject(message));
            UmLog.d(TAG, "message=" + message);      //消息体
            UmLog.d(TAG, "custom=" + msg.custom);    //自定义消息的内容
            UmLog.d(TAG, "title=" + msg.title);      //通知标题
            UmLog.d(TAG, "text=" + msg.text);        //通知内容
            //发送广播
            Intent intent2=new Intent();
            intent2.putExtra("title", msg.title);
            intent2.putExtra("text", msg.text);
            intent2.putExtra("ticker", msg.ticker);
            intent2.putExtra("img", msg.img);

            intent2.setAction("com.example.buiderdream.UmengPushService");
            sendBroadcast(intent2);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }




}
