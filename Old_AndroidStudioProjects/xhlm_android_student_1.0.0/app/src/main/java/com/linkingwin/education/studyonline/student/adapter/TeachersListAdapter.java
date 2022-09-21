package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseViewAdapter;
import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.common.utils.RouterUtils;
import com.linkingwin.education.studyonline.student.activity.TeacherActivity;
import com.linkingwin.education.studyonline.student.api.response.Teacher;

import org.xutils.view.annotation.ContentView;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ContentView(R.layout.recommended_teacher_detail_view)
public class TeachersListAdapter extends BaseViewAdapter<Teacher> implements AdapterView.OnItemClickListener {

    public TeachersListAdapter(Context context, List<Teacher> dataList) {
        super(context, dataList);
    }

    @Override
    public void bindView(ViewHolder holder, Teacher teacher) {
//        teacher.setPeculiarTag("老湿基");
//        teacher.setSchoolTag("老湿基大学");
        holder.getTextView(R.id.tv_name).setText(teacher.getRealName());
        holder.getGlideImageView(R.id.iv_teacher).drawing(teacher.getAvatar()).display();
        holder.getTextView(R.id.tv_minPrice).setText(String.format("￥ %s 起", teacher.getMinPrice()));
        TextView peculiarTag = holder.getTextView(R.id.tv_peculiarTag);
        if (null != teacher.getPeculiarTag()) {
            // 只显示第一个
            peculiarTag.setText(teacher.getPeculiarTag().split(",")[0]);
            peculiarTag.setVisibility(View.VISIBLE);
        } else {
            peculiarTag.setVisibility(View.GONE);
        }
        holder.getTextView(R.id.tv_teacher_describe).setText(String.format("教龄%s年 已授%s节课", teacher.getTeachYear(), teacher.getLessonsCount()));
        TextView schoolTag = holder.getTextView(R.id.tv_schoolTag);
        if (null != teacher.getSchoolTag()) {
            // 只显示第一个
            schoolTag.setText(teacher.getSchoolTag().split(",")[0]);
            schoolTag.setVisibility(View.VISIBLE);
        } else {
            schoolTag.setVisibility(View.GONE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Teacher teacher = getItem(position);
        Map<String, Serializable> params = new HashMap<>();
        params.put("username", teacher.getUsername());
        RouterUtils.goActivity(getContext(), TeacherActivity.class, params);
    }
}