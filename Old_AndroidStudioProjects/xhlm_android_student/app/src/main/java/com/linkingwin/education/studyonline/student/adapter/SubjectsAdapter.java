package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import java.util.List;
import java.util.Map;

public class SubjectsAdapter extends ListViewAdapter<Subject> {


    public SubjectsAdapter(Context context, List<Subject> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void bindView(ViewHolder holder, Subject subject) {
        TextView subjectname = holder.getView(R.id.tv_subject_name);
        ImageView subjectimage = holder.getView(R.id.iv_subject);
        subjectname.setText(subject.getSubjectName());
        subjectimage.setBackground(getContext().getResources().getDrawable(subject.getSubjectImage()));
    }
}
