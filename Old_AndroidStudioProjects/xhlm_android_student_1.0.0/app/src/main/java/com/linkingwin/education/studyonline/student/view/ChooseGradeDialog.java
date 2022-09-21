package com.linkingwin.education.studyonline.student.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.view.DropPopupDialog;
import com.linkingwin.education.studyonline.student.adapter.GradeAdapter;
import com.linkingwin.education.studyonline.student.vo.Grade;

import org.xutils.view.annotation.ContentView;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.widget_choose_grade)
public class ChooseGradeDialog extends DropPopupDialog<Grade> {

    public ChooseGradeDialog(Context context) {
        super(context);
        onCreate();
    }

    private void onCreate() {
        GridView primaryschool = getContentView().findViewById(R.id.gv_primary_school);
        final List<Grade> gradeData = new ArrayList<>();
        gradeData.add(new Grade("一年级"));
        gradeData.add(new Grade("二年级"));
        gradeData.add(new Grade("三年级"));
        gradeData.add(new Grade("四年级"));
        gradeData.add(new Grade("五年级"));
        gradeData.add(new Grade("六年级"));
        primaryschool.setAdapter(new GradeAdapter(getContext(), gradeData));
        primaryschool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setValue(gradeData.get(position));
            }
        });

        GridView juniorHighschool = getContentView().findViewById(R.id.gv_junior_high_school);
        final List<Grade> juniorHighGradeData = new ArrayList<>();
        juniorHighGradeData.add(new Grade("初一"));
        juniorHighGradeData.add(new Grade("初二"));
        juniorHighGradeData.add(new Grade("初三"));
        juniorHighschool.setAdapter(new GradeAdapter(getContext(), juniorHighGradeData));
        juniorHighschool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setValue(juniorHighGradeData.get(position));
            }
        });

        GridView highschool = getContentView().findViewById(R.id.gv_high_school);
        final List<Grade> highschoolData = new ArrayList<>();
        highschoolData.add(new Grade("高一"));
        highschoolData.add(new Grade("高二"));
        highschoolData.add(new Grade("高三"));
        highschool.setAdapter(new GradeAdapter(getContext(), highschoolData));
        highschool.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setValue(highschoolData.get(position));
            }
        });
    }


}
