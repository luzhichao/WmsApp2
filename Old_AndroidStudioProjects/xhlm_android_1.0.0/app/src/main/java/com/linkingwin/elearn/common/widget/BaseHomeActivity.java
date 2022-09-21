package com.linkingwin.elearn.common.widget;

import android.app.ActivityManager;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

public class BaseHomeActivity extends BaseActivity {
    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //判断用户是否点击了“返回键”
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //与上次点击返回键时刻作差
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                //大于2000ms则认为是误操作，使用Toast进行提示
                Toast.makeText( this, "再按一次退出程序", Toast.LENGTH_SHORT ).show();
                //并记录下本次点击“返回键”的时刻，以便下次进行判断
                mExitTime = System.currentTimeMillis();
            } else {
                //小于2000ms则认为是用户确实希望退出程序-调用System.exit()方法进行退出
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
////                System.exit( 0 );
//                int pid = android.os.Process.myPid();	//获取当前应用程序的PID
//                android.os.Process.killProcess(pid);	//杀死当前进程
                ActivityManager manager = (ActivityManager)this.getSystemService(ACTIVITY_SERVICE); //获取应用程序管理器
                manager.killBackgroundProcesses(getPackageName()); //强制结束当前应用程序
            }
            return true;
        }
        return super.onKeyDown( keyCode, event );
    }
}
