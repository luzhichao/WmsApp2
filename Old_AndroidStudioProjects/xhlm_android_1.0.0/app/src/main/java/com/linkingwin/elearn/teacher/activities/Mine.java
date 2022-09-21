package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.util.GlideImageLoader;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseHomeActivity;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.linkingwin.elearn.teacher.widget.BottomBar;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;
import qiu.niorgai.StatusBarCompat;

import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_MINE;

public class Mine extends BaseHomeActivity {
    @BindView( R.id.bottom_bar )
    BottomNavigationBar bt_bottomBar;
    @BindView( R.id.mine_banner )
    Banner mineBanner;
    @BindView( R.id.iv_icon )
    ImageView iv_icon;
    @BindView( R.id.tv_teach_name )
    TextView tv_teach_name;
    @BindView( R.id.tv_teach_base_info )
    TextView tv_teach_base_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_mine );
        //绑定注解，必须要加载布局后才可以
        ButterKnife.bind( this );
        //加载沉浸式bar,全屏显示
        setTranslucentStatusBar( true );
        //加载底部导航菜单
        ElearnApplication.bottomBar=new BottomBar(getContext());
        ElearnApplication.bottomBar.initBar(bt_bottomBar, BOTTOM_BAR_MINE);
        //初始化头部banner
        initMine();
    }


    /**
     * 实现返回刷新
     */
    @Override
    protected void onResume() {
        super.onResume();
        initPersonDetail();
    }

    /**
     * 个人资料跳转
     * @param v
     */
    @OnClick(R.id.rl_about_me)
    public void openAboutMe(View v){
        Intent intent = new Intent(Mine.this,
                AboutMe.class);
        Mine.this.startActivity(intent);
    }

    /**
     * 加载Banner布局等头部信息
     */
    private void initMine(){
        //初始化banner
        ArrayList<Integer> imagePath;
        ArrayList<String> imageTitle;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.mipmap.banner_about_me);
        imageTitle.add("mine_banner");
        Tools.initBannerView( mineBanner,imagePath,imageTitle);
        //初始化icon头像
        TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
        String avatar =  "https://s1.ax1x.com/2018/05/19/CcdVQP.png";
        if(!StringUtils.isEmpty(ElearnApplication.teachBusinessInfo.getAvatar())){
            avatar=ElearnApplication.teachBusinessInfo.getAvatar();
        }
        new GlideLoader().displayImage(this, avatar, iv_icon);
        initPersonDetail();
    }

    /**
     * 个人资料初始化
     */
    private void initPersonDetail(){
        TeachBusinessInfo tmp = ElearnApplication.teachBusinessInfo;
        tv_teach_name.setText( tmp.getRealName() == null ? "" : tmp.getRealName() );
        tv_teach_base_info.setText( (tmp.getTeachYear() == null ? "" : "教龄" + tmp.getTeachYear() + "年" + "|") + (tmp.getSubDep() == null ? "" : tmp.getSubDep()));
    }

    /**
     * 打开认证中心
     */
    @OnClick(R.id.rl_auth_centre)
    public void openAuthCentre(){
        Intent intent = new Intent(Mine.this,
                AuthCentre.class);
        Mine.this.startActivity(intent);
    }

    /**
     * 打开我的订单
     * @param view
     */
    @OnClick(R.id.ll_my_order)
    public void openMyOrder(View view){
        Intent intent = new Intent(Mine.this,
                MyOrderActivity.class);
        Mine.this.startActivity(intent);
    }

    /**
     * 打开我的钱包
     * @param view
     */
    @OnClick(R.id.ll_my_wallet)
    public void openMyWallet(View view){
        Intent intent = new Intent(Mine.this,
                Wallet.class);
        Mine.this.startActivity(intent);
    }

    /**
     * 打开我的学生列表
     * @param view
     */
    @OnClick(R.id.ll_my_sudent)
    public void openMySutent(View view){
        Intent intent = new Intent(Mine.this,
                MySudentActivity.class);
        Mine.this.startActivity(intent);
    }

    /**
     * 打开我的课程管理页面
     * @param v
     */
    @OnClick(R.id.rl_course_manager)
    public void openCourseManager(View v){
        Tools.toOtherPage( this,Course.class,false,null );
    }

    /**
     * 打开评价中心
     * @param view
     */
    @OnClick(R.id.rl_evaluate)
    public void openEvaluate(View view){
        Intent intent = new Intent(Mine.this,
                EvaluateActivity.class);
        Mine.this.startActivity(intent);
    }
}
