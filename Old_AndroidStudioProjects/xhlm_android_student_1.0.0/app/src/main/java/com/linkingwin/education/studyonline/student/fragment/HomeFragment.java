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
            String[] subjects = new String[]{"??????", "??????", "??????", "??????", "??????", "??????"};
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
            String[] teachers = new String[]{"?????????", "?????????", "?????????", "?????????", "?????????", "?????????"};
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
        imageTitle.add("??????????????????");
        imageTitle.add("??????????????????");

        Api.GET_TEACHER_LIST.request("abc", new DefaultHttpCallback() {
            @Override
            public void onSucceed(Response response) {
                Log.d(this.getClass().getName(), response.toString());
                JSONArray jsonArray = response.getResult();
                //??????????????????
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
        //??????????????????????????????????????????????????????????????????
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //?????????????????????
        mBanner.setImageLoader(mMyImageLoader);
        //???????????????????????????,????????????????????????,????????????????????????
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //?????????????????????
        mBanner.setBannerTitles(imageTitle);
        //????????????????????????
        mBanner.setDelayTime(3000);
        //???????????????????????????????????????true
        mBanner.isAutoPlay(true);
        //???????????????????????????????????????????????????
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //????????????????????????
        mBanner.setImages(imagePath)
                //??????????????????
                .setOnBannerListener(this)
                //??????????????????????????????????????????
                .start();
        lv_subjects.setAdapter(new SubjectsAdapter(getContext(), subjects));
    }

    @Override
    public void OnBannerClick(int position) {

    }


    /**
     * ???????????????
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
