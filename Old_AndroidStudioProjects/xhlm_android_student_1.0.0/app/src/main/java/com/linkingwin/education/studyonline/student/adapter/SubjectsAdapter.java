package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseViewAdapter;
import com.linkingwin.education.studyonline.common.base.ViewHolder;
import com.linkingwin.education.studyonline.student.api.response.Subject;

import org.xutils.view.annotation.ContentView;

import java.util.List;

@ContentView(R.layout.subject_detail_view)
public class SubjectsAdapter extends BaseViewAdapter<Subject> {

    public SubjectsAdapter(Context context, List<Subject> datas) {
        super(context, datas);
    }

    @Override
    public void bindView(ViewHolder holder, Subject subject) {
        TextView subjectname = holder.getView(R.id.tv_subject_name);
        ImageView subjectimage = holder.getView(R.id.iv_subject);
        subjectname.setText(subject.getSubjectName());
        subjectimage.setBackground(getContext().getResources().getDrawable(subject.getSubjectImage()));
    }
}
