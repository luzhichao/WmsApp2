package com.linkingwin.elearn.teacher.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.teacher.LoginActivity;
import com.linkingwin.elearn.teacher.activities.ForgetPWD;
import com.linkingwin.elearn.teacher.activities.HomePage;
import com.linkingwin.elearn.teacher.activities.Message;
import com.linkingwin.elearn.teacher.activities.Mine;
import com.linkingwin.elearn.teacher.activities.WorkSpace;

import static com.ashokvarma.bottomnavigation.BottomNavigationBar.MODE_FIXED;
import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_HOME;
import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_MINE;
import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_MSG;
import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_WORKSPACE;

public class BottomBar implements BottomNavigationBar.OnTabSelectedListener {
    private Context context;

    public BottomBar(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void initBar(BottomNavigationBar bar, int position) {
        //2.构建显示的内容
        bar
                .addItem(new BottomNavigationItem(R.drawable.icon_home_gray, "首页"))
                .addItem(new BottomNavigationItem(R.drawable.icon_work_gray, "工作台"))
                .addItem(new BottomNavigationItem(R.drawable.icon_msg_gray, "消息"))
                .addItem(new BottomNavigationItem(R.drawable.icon_mine_gray, "我的"))
                .setFirstSelectedPosition(position)
                .setBarBackgroundColor(R.color.titleWhite)
                .setMode(MODE_FIXED)
                .initialise();
        //3.添加监听
        bar.setTabSelectedListener(this);
    }

    @Override
    public void onTabSelected(int position) {
        Intent intent = new Intent();
        switch (position) {
            case BOTTOM_BAR_HOME:
                intent = new Intent(context, HomePage.class);
                context.startActivity(intent);
                Tools.findActivity(context).finish();
                break;
            case BOTTOM_BAR_WORKSPACE:
                intent = new Intent(context, WorkSpace.class);
                context.startActivity(intent);
                Tools.findActivity(context).finish();
                break;
            case BOTTOM_BAR_MSG:
                intent = new Intent(context, Message.class);
                context.startActivity(intent);
                Tools.findActivity(context).finish();
                break;
            case BOTTOM_BAR_MINE:
                intent = new Intent(context, Mine.class);
                context.startActivity(intent);
                Tools.findActivity(context).finish();
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
}
