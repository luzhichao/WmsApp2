package com.linkingwin.education.studyonline.student.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.baijiahulian.live.ui.LiveSDKWithUI;
import com.baijiahulian.livecore.context.LPConstants;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseFragment;
import com.linkingwin.education.studyonline.common.base.MyApplication;
import com.linkingwin.education.studyonline.common.base.annotation.ToolbarContent;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.api.request.EnterRoomRequest;
import com.linkingwin.education.studyonline.student.vo.RoomRole;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

/**
 * Created by afanbaby on 2018/2/2.
 */
@ContentView(R.layout.fragment_bwx)
@ToolbarContent(resId = -1, title = "")
public class BaijiayunFragment extends BaseFragment {

    public static BaijiayunFragment newInstance() {
        return new BaijiayunFragment ();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

        }
        init();
    }

    private void init() {

    }

    @Event(value = R.id.test_btn, type = View.OnClickListener.class)
    private void enterRoom(View view) {



       final LiveSDKWithUI.LiveSDKEnterRoomListener enterRoomListener = new LiveSDKWithUI.LiveSDKEnterRoomListener() {
            @Override
            public void onError(String s) {
                ToastUtils.show(s);
            }
        };

        EnterRoomRequest enterRoomRequest = new EnterRoomRequest();
        enterRoomRequest.setMerchantId("ROOT");
        enterRoomRequest.setChannel("baijiayun");
        enterRoomRequest.setRoomId("19021398047570");
        enterRoomRequest.setUserName(MyApplication.getInstance ().getUser().getUsername());
        enterRoomRequest.setRoomRole(RoomRole.audience.name());

        Api.BAIJIAYUN_CLIENT_ENTER_ROOM.request(enterRoomRequest, new DefaultHttpCallback() {

            @Override
            public void onSuccess(Response result) {

                LiveSDKWithUI.LiveRoomUserModel userModel = new LiveSDKWithUI.LiveRoomUserModel(
                        result.getString("userName"),
                        result.getString("avatar"),
                        result.getString("userNumber"),
                        LPConstants.LPUserType.from(result.getInteger("userRole"))
                );

                LiveSDKWithUI.enterRoom(getContext(), Long.valueOf(result.getString("roomId")), result.getString("sign"), userModel, enterRoomListener);
            }

            @Override
            public void onSucceed(Response response) {

            }

            @Override
            public void onFail(Response response) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                ToastUtils.show(ex.getMessage());
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
