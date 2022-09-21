package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;
import android.widget.CheckBox;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseViewAdapter;
import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.student.vo.Grade;

import org.xutils.view.annotation.ContentView;

import java.util.List;

@ContentView(R.layout.widget_tag)
public class GradeAdapter extends BaseViewAdapter<Grade> {

    public GradeAdapter(Context context, List<Grade> dataList) {
        super(context, dataList);
    }

    @Override
    public void bindView(ViewHolder holder, Grade grade) {
        CheckBox btn = holder.getView(R.id.checkbox_tag);
        btn.setText(grade.lableName());
    }
}
