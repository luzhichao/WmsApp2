package com.linkingwin.education.studyonline.student.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.GridView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.view.BottomSheetDialogUi;
import com.linkingwin.education.studyonline.common.base.view.MultCheckButtonListAdapterUi;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.common.utils.ToastUtils;
import com.linkingwin.education.studyonline.student.activity.SubscribeSubjectActivity;
import com.linkingwin.education.studyonline.student.vo.Grade;
import com.qmuiteam.qmui.layout.QMUIButton;

import java.util.List;

public class BuySubjectDialog extends BottomSheetDialogUi {


    private List<Grade> classList;

    public BuySubjectDialog(@NonNull Context context, List<Grade> classList) {
        super(context, R.layout.dialog_buy_course);
        this.classList = classList;
    }

    @Override
    public BottomSheetDialogUi init() {
        GridView gridView = findViewById(R.id.gv_canbuy_classlist);
        final MultCheckButtonListAdapterUi<Grade> adapter =
                new MultCheckButtonListAdapterUi<Grade>(getContext(), gridView)
                        .setDataList(classList)
                        .setCheckBoxId(R.id.checkbox_tag)
                        .init();
        QMUIButton button = findViewById(R.id.btn_buy);
        final BottomSheetDialog dialog = getBottomSheetDialog();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Grade> checkedData = adapter.getCheckedData();
                ToastUtils.show(com.alibaba.fastjson.JSONObject.toJSONString(checkedData));
                RouterUtils.goActivity(getContext(), SubscribeSubjectActivity.class, null);
                dialog.dismiss();
            }
        });
        return this;
    }
}
