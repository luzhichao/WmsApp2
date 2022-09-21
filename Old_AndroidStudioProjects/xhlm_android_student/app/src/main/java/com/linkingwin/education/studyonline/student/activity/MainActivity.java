package com.linkingwin.education.studyonline.student.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment.EaseConversationListItemClickListener;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.student.BottomNavigationViewHelper;
import com.linkingwin.education.studyonline.student.adapter.ViewPagerAdapter;
import com.linkingwin.education.studyonline.student.fragment.BaijiayunFragment;
import com.linkingwin.education.studyonline.student.fragment.HomeFragment;
import com.linkingwin.education.studyonline.student.fragment.MineFragment;
import com.linkingwin.education.studyonline.student.view.MainViewPager;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;


@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    private BottomNavigationView bottomNavigationView;
    private ViewPagerAdapter viewPagerAdapter;
    private MainViewPager viewPager;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        viewPager = (MainViewPager) findViewById(R.id.vp);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        List<Fragment> list = new ArrayList<>();
        list.add(HomeFragment.newInstance());
        list.add(BaijiayunFragment.newInstance());
        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setUserVisibleHint (true);
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        list.add (conversationListFragment);
        list.add(MineFragment.newInstance());
        viewPagerAdapter.setList(list);

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_notifications:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.navigation_person:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };

}
