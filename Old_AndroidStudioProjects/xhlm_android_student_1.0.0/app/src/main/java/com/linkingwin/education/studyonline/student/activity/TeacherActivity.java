package com.linkingwin.education.studyonline.student.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baijiayun.glide.Glide;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.common.utils.Lists;
import com.linkingwin.education.studyonline.common.utils.Maps;
import com.linkingwin.education.studyonline.student.adapter.SmallClassAdapter;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.api.response.Course;
import com.linkingwin.education.studyonline.student.api.response.Teacher;
import com.linkingwin.education.studyonline.student.ui.BuySubjectDialog;
import com.linkingwin.education.studyonline.student.view.HorizontalListView;
import com.linkingwin.education.studyonline.student.vo.Grade;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.loader.ImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.activity_teacher)
public class TeacherActivity extends BaseActivity {

    /**
     * 立即购买 一对一课程
     */
    @ViewInject(R.id.btn_buy_one2one)
    QMUIRoundButton btn_buy_one2one;

    @ViewInject(R.id.banner_teacher)
    Banner mBanner;

    @ViewInject(R.id.hlv_courses)
    HorizontalListView courses;

    private TeacherActivity.MyImageLoader mMyImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final String username = getParam("username");
        Api.GET_TEACHINFO_BY_USERNAME.request(username, new DefaultHttpCallback() {
            @Override
            public void onSucceed(Response response) {
                Log.d("", response.toString());
                Teacher teacher = ((JSONObject) response.getResult()).getObject("teachInfo", Teacher.class);
                teacher.setUsername(username);
                initView(teacher);
            }

            @Override
            public void onFail(Response response) {
                Log.d("", response.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d("", ex.toString());
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView(Teacher teacher){
        if (null == teacher) {
            return;
        }
        if (null != teacher.getPhoto()) {
            String videoUrl = teacher.getPersonalVideo();
            //初始化轮播图
            initBanner(Arrays.asList(teacher.getPhoto().split(",")));
        }
        // 初始化小班课List
        initSmallClassSubject(teacher);
    }

    private void initSmallClassSubject(Teacher teacher) {
        HashMap<String, String> params = Maps.newHashMap();
        params.put("userName", teacher.getUsername());
        Api.FIND_COURSES_BY_USERNAME_OF_TEACHER.request(params, new DefaultHttpCallback() {
            @Override
            public void onSucceed(Response response) {
                if (null != response.getResult()) {
                    SmallClassAdapter adapter = new SmallClassAdapter(TeacherActivity.this,
                            ((JSONArray) response.getResult()).toJavaList(Course.class));
                    courses.setAdapter(adapter);
                }
            }

            @Override
            public void onFail(Response response) {
                Log.e("", response.getErrorMsg());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("", ex.getMessage());
            }
        });
    }

    private void initBanner(List<String> uri) {
        List<String> titles = Lists.newArrayList();
        for (int i = 0; i < uri.size(); i++) {
            titles.add("");
        }

        mMyImageLoader = new TeacherActivity.MyImageLoader();
        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)
        //设置图片加载器
        .setImageLoader(mMyImageLoader)
        .setBannerTitles(titles)
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        .setBannerAnimation(Transformer.Default)
        //设置轮播间隔时间
        .setDelayTime(3000)
        //设置是否为自动轮播，默认是true
        .isAutoPlay(true)
        //设置指示器的位置，小点点，居中显示
        .setIndicatorGravity(BannerConfig.CENTER)
        //设置图片加载地址
        .setImages(uri)
        //开始调用的方法，启动轮播图。
        .start();
    }

    @Event(value = {R.id.btn_buy_one2one}, type = View.OnClickListener.class)
    private void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_buy_one2one:
                showBuyOne2OneCourseDialog();
                break;
        }
    }

    /**
     * 显示购买一对一课程Dialog
     */
    private void showBuyOne2OneCourseDialog() {
        final List<Grade> classList = Lists.newArrayList(
                new Grade("一年级"),
                new Grade("二年级"),
                new Grade("三年级"),
                new Grade("四年级"),
                new Grade("五年级"),
                new Grade("六年级"),
                new Grade("初一"),
                new Grade("初二"),
                new Grade("初三"),
                new Grade("高一"),
                new Grade("高二"),
                new Grade("高三"));
        new BuySubjectDialog(TeacherActivity.this, classList).init().show();
    }

    /**
     * 图片加载类
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
            Uri uri = Uri.parse((String) path);
            imageView.setImageURI(uri);
        }
    }
}
