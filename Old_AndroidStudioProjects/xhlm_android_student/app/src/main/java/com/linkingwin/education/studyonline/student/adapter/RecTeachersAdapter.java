package com.linkingwin.education.studyonline.student.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkingwin.education.studyonline.R;

import java.util.List;

public class RecTeachersAdapter extends BaseAdapter {

    private Context context;
    private List<String> list;

    public RecTeachersAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        convertView = LayoutInflater.from(context).inflate(R.layout.recommended_teacher_detail_view, null);
        TextView subjectname = (TextView) convertView.findViewById(R.id.tv_name);
        subjectname.setText("初高中张老师" + list.get(position));
        return convertView;
    }
}
