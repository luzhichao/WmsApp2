package com.linkingwin.education.studyonline.student.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.baijiayun.glide.Glide;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseFragment;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;
import com.linkingwin.education.studyonline.student.adapter.RecTeachersAdapter;
import com.linkingwin.education.studyonline.student.adapter.SubjectsAdapter;
import com.linkingwin.education.studyonline.student.adapter.FamousTeachersAdapter;
import com.linkingwin.education.studyonline.student.view.HorizontalListView;
import com.linkingwin.education.studyonline.student.vo.Subject;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Arrays;
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

    private final static List<String> teachers = Arrays.asList("1", "2", "3","1", "2", "3");
    private final static List<String> rcteachers = Arrays.asList("1", "2", "3","4", "5", "6");

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
        lv_subjects.setAdapter(new SubjectsAdapter(getContext(), subjects, R.layout.subject_detail_view));
        lv_teachers.setAdapter(new FamousTeachersAdapter(teachers, getContext()));
        lv_rec_teachers.setAdapter(new RecTeachersAdapter(rcteachers, getContext()));
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

}
