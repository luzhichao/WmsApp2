package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.youth.banner.Banner;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.toolsfinal.StringUtils;

public class AboutMe extends BaseActivity {
    @BindView(R.id.title_about_me)
    TitleView titleView;
    @BindView(R.id.iv_icon)
    ImageView iv_icon;
    @BindView(R.id.pg_fill)
    ProgressBar pg_fill;
    @BindView(R.id.about_banner)
    Banner aboutBanner;
    @BindView(R.id.tv_teach_name)
    TextView tv_teach_name;
    @BindView(R.id.tv_teach_base_info)
    TextView tv_teach_base_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);  //必须要加，注解才会生效否则报Null错误
        setTranslucentStatusBar(true);
        //定义title的样式,todo,需要跳转预览主页的页面
        titleView.setTitleText(getString(R.string.title_about_me), R.color.titleWhite)
                .setSubTitle("预览主页", R.color.titleWhite, null)
                .setBackGround(null);
        initAboutMe();//初始化头部banner
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
     * 个人资料初始化
     */
    private void initPersonDetail() {
        TeachBusinessInfo tmp = ElearnApplication.teachBusinessInfo;
        tv_teach_name.setText(tmp.getRealName() == null ? "" : tmp.getRealName());
        tv_teach_base_info.setText((tmp.getTeachYear() == null ? "" : "教龄" + tmp.getTeachYear() + "年" + "|") + (tmp.getSubDep() == null ? "" : tmp.getSubDep()));
    }

    public void initAboutMe() {
        //初始化banner
        ArrayList<Integer> imagePath;
        ArrayList<String> imageTitle;
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.mipmap.banner_about_me);
        imageTitle.add("about_banner");
        Tools.initBannerView(aboutBanner, imagePath, imageTitle);

        //初始化个人头像
        TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
        String avatar = "https://s1.ax1x.com/2018/05/19/CcdVQP.png";
        if (!StringUtils.isEmpty(ElearnApplication.teachBusinessInfo.getAvatar())) {
            avatar = ElearnApplication.teachBusinessInfo.getAvatar();
        }
        new GlideLoader().displayImage(this, avatar, iv_icon);
        initPersonDetail();
    }


    /**
     * 跳转到教学特点页面
     */
    @OnClick(R.id.rl_peculiar)
    public void openPeculiar(View v) {
        Tools.toOtherPage(this, Peculiar.class, false, null);
    }

    /**
     * 跳转到成功案例页面
     */
    @OnClick(R.id.rl_success_case)
    public void openSuccessCase(View v) {
        Tools.toOtherPage(this, SuccessCase.class, false, null);
    }

    /**
     * 跳转到教学经历页面
     */
    @OnClick(R.id.rl_teach_exp)
    public void openExperience(View v) {
        Tools.toOtherPage(this, Experience.class, false, null);
    }

    /**
     * 跳转到职称页面
     *
     * @param view
     */
    @OnClick(R.id.rl_job_title)
    public void openJobLevel(View view) {
        Tools.toOtherPage(this, JobLevel.class, false, null);
    }

    /**
     * 基本信息跳转
     *
     * @param v
     */
    @OnClick(R.id.ll_base_info)
    public void openBaseInfo(View v) {
        Intent intent = new Intent(AboutMe.this,
                TeacherBaseInfo.class);
        AboutMe.this.startActivity(intent);
    }

    /**
     * 毕业院校跳转
     */
    @OnClick(R.id.rl_school)
    public void openSchool(View view) {
        Intent intent = new Intent(AboutMe.this,
                School.class);
        AboutMe.this.startActivity(intent);
    }

    /**
     * 打开奖励荣誉页面
     *
     * @param view
     */
    @OnClick(R.id.rl_honor)
    public void openHonor(View view) {
        Intent intent = new Intent(AboutMe.this,
                HonorActivity.class);
        AboutMe.this.startActivity(intent);
    }

    /**
     * 打开风采展示-照片
     *
     * @param view
     */
    @OnClick(R.id.rl_my_picture)
    public void openMyPicture(View view) {
        Intent intent = new Intent(AboutMe.this,
                MyPictureActivity.class);
        AboutMe.this.startActivity(intent);
    }

    /**
     * 打开视频录制
     */
    @OnClick(R.id.tv_video)
    public void openVideo() {
        Intent intent = new Intent(AboutMe.this,
                VideoActivity.class);
        AboutMe.this.startActivity(intent);
    }

    /**
     * 打开标签设置页面
     */
    @OnClick(R.id.tv_person_label)
    public void openLabelSet() {
        Tools.toOtherPage(this, LabelSet.class, false, null);
    }
}
