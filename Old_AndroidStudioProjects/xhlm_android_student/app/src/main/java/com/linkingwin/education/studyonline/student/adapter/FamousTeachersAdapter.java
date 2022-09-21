package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.ListViewAdapter;
import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.student.vo.Subject;
import com.linkingwin.education.studyonline.student.vo.Teacher;

import java.util.List;

/**
 * 主页名师优选 Adpater
 */
public class FamousTeachersAdapter extends ListViewAdapter<Teacher> {

    public FamousTeachersAdapter(Context context, List<Teacher> dataList, int layoutId) {
        super(context, dataList, layoutId);
    }

    @Override
    public void bindView(ViewHolder holder, Teacher teacher) {
//        holder.getView()
    }
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(context).inflate(R.layout.teacher_detail_view, null);
        TextView subjectname = (TextView) convertView.findViewById(R.id.tv_subject_name);
        ImageView subjectimage = (ImageView) convertView.findViewById(R.id.iv_subject);
        return convertView;
    }

}
