package com.linkingwin.elearn.teacher.activities;


import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.hyphenate.easeui.domain.EaseUser;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.util.GlideImageLoader;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.BaseHomeActivity;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.linkingwin.elearn.teacher.widget.BottomBar;
import com.youth.banner.Banner;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;

import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_HOME;

public class HomePage extends BaseHomeActivity {
    @BindView( R.id.home_banner )
    Banner banner;
    @BindView( R.id.iv_icon )
    ImageView iv_icon;
    @BindView( R.id.bottom_bar )
    BottomNavigationBar bt_bottomBar;
    @BindView(R.id.tv_tip)
    TextView tv_tip;
    @BindView(R.id.tv_apply_tip)
    TextView tv_apply_tip;

    @BindView(R.id.ll_apply)
    LinearLayout ll_apply;
    @BindView(R.id.ll_line)
    TextView ll_line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        //绑定注解，必须要加载布局后才可以
        ButterKnife.bind( this );
        //打开沉浸
        setTranslucentStatusBar( true );
        //加载icon
        TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
        String avatar =  "https://s1.ax1x.com/2018/05/19/CcdVQP.png";
        if(!StringUtils.isEmpty(ElearnApplication.teachBusinessInfo.getAvatar())){
            avatar=ElearnApplication.teachBusinessInfo.getAvatar();
        }
        new GlideLoader().displayImage( getContext(), avatar, iv_icon );
        //加载底部导航菜单
        ElearnApplication.bottomBar=new BottomBar(getContext());
        ElearnApplication.bottomBar.initBar(bt_bottomBar, BOTTOM_BAR_HOME);
        //加载Banner布局视图
        initBannerView();
        initApplySubject();
    }

    //加载Banner布局
    private void initBannerView(){
        ArrayList<Integer> imagePath;
        ArrayList<String> imageTitle;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.mipmap.banner);
        imageTitle.add("黑板");
        Tools.initBannerView( banner,imagePath,imageTitle);
    }

    //todo,测试
    @OnClick(R.id.iv_icon)
    public void testMES(){
        EaseUser user = new EaseUser("18672331126");
        if (user != null) {
            String username = user.getUsername();
            // demo中直接进入聊天页面，实际一般是进入用户详情页
            startActivity(new Intent(this, ChatActivity.class).putExtra("userId", username));
        }
    }

    @OnClick(R.id.rl_apply)
    public void openApplySubject() {
        Tools.toOtherPage(this,TeacherApplySubject.class,false,null);
    }

    private void initApplySubject(){
        String applyStatus=ElearnApplication.teachBusinessInfo.getApplyTeachStatus();
        if("1".equals(applyStatus)){
            ll_apply.setVisibility(View.GONE);
            ll_line.setVisibility(View.GONE);
        }
    }
}
