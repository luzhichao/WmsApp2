package com.linkingwin.education.studyonline.student.activity;

import android.content.Intent;
import android.os.Bundle;

import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseBaseActivity;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.student.fragment.BaseEaseChatFragment;

public class ChatActivity extends EaseBaseActivity {
    public static ChatActivity activityInstance;
    private BaseEaseChatFragment chatFragment;
    String toChatUsername;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        activityInstance = this;
        toChatUsername = getIntent().getExtras().getString(EaseConstant.EXTRA_USER_ID);
        chatFragment = new BaseEaseChatFragment();
        //set arguments
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // enter to chat activity when click notification bar, here make sure only one chat activiy
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
    }

    public String getToChatUsername(){
        return toChatUsername;
    }
}
