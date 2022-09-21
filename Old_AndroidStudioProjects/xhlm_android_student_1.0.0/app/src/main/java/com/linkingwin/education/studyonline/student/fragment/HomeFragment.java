package com.linkingwin.education.studyonline.student.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.baijiayun.glide.Glide;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseFragment;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.student.activity.SearchSubjectActivity;
import com.linkingwin.education.studyonline.student.adapter.FamousTeachersAdapter;
import com.linkingwin.education.studyonline.student.adapter.TeachersListAdapter;
import com.linkingwin.education.studyonline.student.adapter.SubjectsAdapter;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.view.HorizontalListView;
import com.linkingwin.education.studyonline.student.api.response.Subject;
import com.linkingwin.education.studyonline.student.api.response.Teacher;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ToolbarContent(resId = -1, title = "")
@ContentView(R.layout.fragment_home)
public class HomeFragment extends BaseFragment implements OnBannerListener {

    @ViewInject(R.id.index_banner)
    Banner mBanner;

    @ViewInject(R.id.lv_subjects)
    HorizontalListView lv_subjects;

    @ViewInject(R.id.lv_teachers)
    HorizontalListView lv_teachers;

    @ViewInject(R.id.lv_rec_teachers)
    ListView lv_rec_teachers;

    @ViewInject(R.id.tv_search_text)
    TextView tv_search_text;

    @ViewInject(R.id.iv_search)
    ImageView iv_search;

    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;

    private final static List<Subject> subjects = new ArrayList<Subject>() {
        {
            String[] subjects = new String[]{"数学", "语文", "物理", "化学", "英语", "其它"};
            Integer[] images = new Integer[]{
                    R.drawable.btn_shuxue, R.drawable.btn_yuwen, R.drawable.btn_wuli,
                    R.drawable.btn_huaxue, R.drawable.btn_yingyu, R.drawable.btn_shuxue};
            for (int i = 0; i < subjects.length; i++) {
                Subject subject = new Subject();
                subject.setSubjectName(subjects[i]);
                subject.setSubjectImage(images[i]);
                add(subject);
            }
        }
    };

    private final static List<Teacher> teachers = new ArrayList<Teacher>() {
        {
            String[] teachers = new String[]{"张老师", "李老师", "王老师", "邓老师", "黄老师", "刘老师"};
            for (int i = 0; i < teachers.length; i++) {
                Teacher teacher = new Teacher();
                teacher.setRealName(teachers[i]);
                add(teacher);
            }
        }
    };

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment ();
        Bundle args = new Bundle ();
        fragment.setArguments (args);
        return fragment;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.banner01);
        imagePath.add(R.drawable.banner02);
        imageTitle.add("我是海鸟一号");
        imageTitle.add("我是海鸟二号");

        Api.GET_TEACHER_LIST.request("abc", new DefaultHttpCallback() {
            @Override
            public void onSucceed(Response response) {
                Log.d(this.getClass().getName(), response.toString());
                JSONArray jsonArray = response.getResult();
                //加载老师信息
                List<Teacher> teachers = jsonArray.toJavaList(Teacher.class);
                lv_teachers.setAdapter(new FamousTeachersAdapter(getContext(), teachers));
                lv_rec_teachers.setAdapter(new TeachersListAdapter(getContext(), teachers));
            }

            @Override
            public void onFail(Response response) {
                Log.d(getClass().getName(), response.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(getClass().getName(), ex.getMessage());
            }
        });

    }


    private void initView() {
        mMyImageLoader = new MyImageLoader();
        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(imagePath)
                //轮播图的监听
                .setOnBannerListener(this)
                //开始调用的方法，启动轮播图。
                .start();
        lv_subjects.setAdapter(new SubjectsAdapter(getContext(), subjects));
    }

    @Override
    public void OnBannerClick(int position) {

    }


    /**
     * 图片加载类
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

    @Event(value = {R.id.tv_search_text, R.id.iv_search}, type = View.OnClickListener.class)
    private void regist(View view) {
        RouterUtils.fragmentGoActivity(HomeFragment.this, SearchSubjectActivity.class, null);
    }

}
