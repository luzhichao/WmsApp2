package com.linkingwin.elearn.teacher.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.widget.BaseHomeActivity;
import com.linkingwin.elearn.teacher.widget.BottomBar;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.linkingwin.elearn.common.http.Api.BOTTOM_BAR_MSG;

public class Message extends BaseHomeActivity {
    @BindView(R.id.bottom_bar)
    BottomNavigationBar bt_bottomBar;
    @BindView(R.id.fragment_container)
    RelativeLayout fragment_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        //绑定注解，必须要加载布局后才可以
        ButterKnife.bind(this);
//        new BottomBar(getContext(),this).initBar(bottomBar, BOTTOM_BAR_MSG);
        ElearnApplication.bottomBar = new BottomBar(getContext());
        ElearnApplication.bottomBar.initBar(bt_bottomBar, BOTTOM_BAR_MSG);

        initEaseMessage();
    }

    /**
     * 初始化Ease聊天功能
     */
    private void initEaseMessage(){
        EaseConversationListFragment conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setUserVisibleHint(true);
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                // 以下代码作用：跳转到 ChatActivity, 并传入参数：当前的用户ID
                startActivity(new Intent(Message.this, ChatActivity.class)
                        .putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, conversationListFragment)
                .show(conversationListFragment)
                .commit();
    }
}
