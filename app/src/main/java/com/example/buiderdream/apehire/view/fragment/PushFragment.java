package com.example.buiderdream.apehire.view.fragment;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseFragment;
import com.example.buiderdream.apehire.bean.PushMessage;
import com.example.buiderdream.apehire.constants.ConstantUtils;
import com.example.buiderdream.apehire.utils.CommonAdapter;
import com.example.buiderdream.apehire.utils.DateUtils;
import com.example.buiderdream.apehire.utils.SharePreferencesUtil;
import com.example.buiderdream.apehire.utils.ViewHolder;
import com.example.buiderdream.apehire.view.activitys.MainActivity;
import com.example.buiderdream.apehire.view.activitys.PushMessageActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by 51644 on 2017/2/28.
 */

public class PushFragment extends BaseFragment {
    private View pushFragmentView;
    private ListView lv_pushMessage;
    private List<PushMessage> pushMessageList;
    private CommonAdapter<PushMessage> adapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (pushFragmentView == null) {
            pushFragmentView = inflater.inflate(R.layout.fragment_push, container, false);
        }
        if (pushFragmentView != null) {
            return pushFragmentView;
        }
        return inflater.inflate(R.layout.fragment_push, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context = getActivity();
        initView();
    }

    private void initView() {
        lv_pushMessage = (ListView) pushFragmentView.findViewById(R.id.lv_pushMessage);
        pushMessageList = (List<PushMessage>) SharePreferencesUtil.readObject(context,ConstantUtils.PUSH_MESSAGE_DATA);
        if (pushMessageList==null||pushMessageList.size()==0){
            pushMessageList = new ArrayList<>();
        }
        adapter = new CommonAdapter<PushMessage>(context,pushMessageList,R.layout.item_pushfragment_list) {
            @Override
            public void convert(ViewHolder helper, PushMessage item) {

                helper.setImageByUrl2(R.id.img_userHeader,item.getImg());
                helper.setText(R.id.tv_title,item.getTitle());
                helper.setText(R.id.tv_time, DateUtils.converTime(item.getTime()));

            }
        };
        lv_pushMessage.setAdapter(adapter);
        lv_pushMessage.setEmptyView(pushFragmentView.findViewById(R.id.img_emptyView));
        lv_pushMessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PushMessageActivity.actionStart(getActivity(),pushMessageList.get(position));
            }
        });
    }

    @Override
    public void onDestroyView() {
        ViewGroup parent = (ViewGroup) pushFragmentView.getParent();
        if (parent != null) {
            parent.removeView(pushFragmentView);
        }
        super.onDestroyView();
    }

    public static class UMBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            String title = bundle.getString("title");
            String text = bundle.getString("text");
            String ticker = bundle.getString("ticker");
            String img = bundle.getString("img");
            sendNotifiction(context, title, text, ticker, img);
        }
    }

    private static void sendNotifiction(Context context, String title, String text, String ticker, String img) {
        NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        List<PushMessage> list = (List<PushMessage>) SharePreferencesUtil.readObject(context, ConstantUtils.PUSH_MESSAGE_DATA);
       if (list==null||list.size()==0){
           list =new  ArrayList();
       }
        PushMessage message = new PushMessage(title, text, img,System.currentTimeMillis()/1000);
        list.add(message);
        SharePreferencesUtil.saveObject(context, ConstantUtils.PUSH_MESSAGE_DATA, list);
        List<PushMessage> list2 = (List<PushMessage>) SharePreferencesUtil.readObject(context, ConstantUtils.PUSH_MESSAGE_DATA);
        Notification notification = new Notification.Builder(context)
                .setTicker(ticker)  //设置顶部出现文字
                .setContentTitle(title) //设置下拉后通知标题
                .setContentText(text) //设置下拉后出现的内容
                .setSmallIcon(R.mipmap.ic_apehire)
                .setContentIntent(pi) //设置点击跳转
                .build();
        notification.flags = Notification.FLAG_NO_CLEAR;
        manager.notify(0, notification);

    }

}
