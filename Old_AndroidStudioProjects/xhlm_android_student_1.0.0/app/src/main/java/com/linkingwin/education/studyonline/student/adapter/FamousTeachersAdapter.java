package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseViewAdapter;
import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.student.api.response.Teacher;

import org.xutils.view.annotation.ContentView;

import java.util.List;

/**
 * 主页名师优选 Adpater
 */
@ContentView(R.layout.teacher_detail_view)
public class FamousTeachersAdapter extends BaseViewAdapter<Teacher> {

    public FamousTeachersAdapter(Context context, List<Teacher> dataList) {
        super(context, dataList);
    }

    @Override
    public void bindView(ViewHolder holder, Teacher teacher) {
        holder.getTextView(R.id.tv_subject_name).setText(teacher.getRealName());
        holder.getGlideImageView(R.id.iv_subject).drawing(teacher.getAvatar()).display();
        holder.getTextView(R.id.tv_subject_describe).setText(String.format("￥ %s 起", teacher.getMinPrice()));
    }
}
