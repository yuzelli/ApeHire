package com.example.buiderdream.apehire.view.activitys;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.buiderdream.apehire.R;
import com.example.buiderdream.apehire.base.BaseActivity;
import com.example.buiderdream.apehire.utils.JudgeUtils;
import com.example.buiderdream.apehire.view.fragment.BossMineFragment;
import com.example.buiderdream.apehire.view.fragment.HireFragment;
import com.example.buiderdream.apehire.view.fragment.MessageFragment;
import com.example.buiderdream.apehire.view.fragment.TechnologyFragment;
import com.example.buiderdream.apehire.view.fragment.UserMineFragment;

/**
 * 主activity根据用户类型加载不同的Fragment
 *
 * @author 李秉龙
 */
public class MainActivity extends BaseActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost tabHost;
    //定义一个布局
    private LayoutInflater layoutInflater;


    //定义数组来存放user的Fragment界面
    private Class userFragmentArray[] = {HireFragment.class, TechnologyFragment.class, MessageFragment.class, UserMineFragment.class};
    //定义数组来存放boss的Fragment界面
    private Class bossFragmentArray[] = {HireFragment.class, TechnologyFragment.class, MessageFragment.class, BossMineFragment.class};
    //定义数组来存放的按钮图片
    private int tabImageViewArray[] = {R.drawable.tab_hire_btn, R.drawable.tab_hire_btn,
            R.drawable.tab_hire_btn, R.drawable.tab_hire_btn};
    //Tab选项卡的文字
    private String tabtTextViewArray[] = {"招聘", "技术", "消息", "我的"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        tabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(), R.id.fl_pageContent);

        //得到fragment的个数
        int count = userFragmentArray.length;
        if (!JudgeUtils.getUserType(getApplication())) {
            for (int i = 0; i < count; i++) {
                //为每一个Tab按钮设置图标、文字和内容
                TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabtTextViewArray[i]).setIndicator(getTabItemView(i));
                //将Tab按钮添加进Tab选项卡中
                tabHost.addTab(tabSpec, bossFragmentArray[i], null);
                //设置Tab按钮的背景
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
            }
        } else {
            for (int i = 0; i < count; i++) {
                //为每一个Tab按钮设置图标、文字和内容
                TabHost.TabSpec tabSpec = tabHost.newTabSpec(tabtTextViewArray[i]).setIndicator(getTabItemView(i));
                //将Tab按钮添加进Tab选项卡中
                tabHost.addTab(tabSpec, userFragmentArray[i], null);
                //设置Tab按钮的背景
                tabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
            }
        }

    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.main_tab_select_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_tabIcon);
        imageView.setImageResource(tabImageViewArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.tv_tabText);
        textView.setText(tabtTextViewArray[index]);
        return view;
    }
    public static void actionStart(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
}
