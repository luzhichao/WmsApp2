package com.linkingwin.education.studyonline.student.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.alibaba.fastjson.JSONArray;
import com.linkingwin.education.studyonline.R;
import com.linkingwin.education.studyonline.common.base.BaseActivity;
import com.linkingwin.education.studyonline.common.base.view.PopupDialogButton;
import com.linkingwin.education.studyonline.common.http.DefaultHttpCallback;
import com.linkingwin.education.studyonline.common.http.Response;
import com.linkingwin.education.studyonline.student.adapter.TeachersListAdapter;
import com.linkingwin.education.studyonline.student.api.Api;
import com.linkingwin.education.studyonline.student.api.response.Course;
import com.linkingwin.education.studyonline.student.api.response.Teacher;
import com.linkingwin.education.studyonline.student.view.ChooseGradeDialog;
import com.linkingwin.education.studyonline.student.view.DropSelectorDialog;
import com.linkingwin.education.studyonline.student.vo.DropBean;
import com.linkingwin.education.studyonline.student.vo.Grade;
import com.qmuiteam.qmui.layout.QMUIButton;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_search_subject)
public class SearchSubjectActivity extends BaseActivity {

    /**
     * 智能排序_选择控件
     */
    @ViewInject(R.id.sel_orderlist)
    PopupDialogButton<DropBean> orderlistSel;

    /**
     * 年级_选择控件
     */
    @ViewInject(R.id.sel_gradelist)
    PopupDialogButton<Grade> sel_gradelist;

    /**
     * 学科_选择控件
     */
    @ViewInject(R.id.sel_subjectlist)
    PopupDialogButton<DropBean> subjectlistSel;

    /**
     * 筛选_选择控件
     */
    @ViewInject(R.id.sel_otherlist)
    PopupDialogButton<DropBean> otherlistSel;

    /**
     * 课程列表
     */
    @ViewInject(R.id.lv_search_teacher)
    ListView lv_search_teacher;

    /**
     * 搜索课程按钮
     */
    @ViewInject(R.id.btn_search_subjects)
    QMUIButton btn_search_subjects;

    @ViewInject(R.id.tv_search_text)
    EditText tv_search_text;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    private void initData() {
        List<DropBean> orderlistData = new ArrayList<>();
        orderlistData.add(new DropBean("1", "价格最高"));
        orderlistData.add(new DropBean("2", "授课课多"));
        // R.layout.dropdown_content
        orderlistSel.setDropPopupDialog(new DropSelectorDialog(SearchSubjectActivity.this).init(orderlistData))
                .setAnchor(findViewById(R.id.sel_orderlist))
                .setValue(orderlistData.get(0));

        final Grade defaultGrade = new Grade(null, "年级");
        sel_gradelist.setDropPopupDialog(new ChooseGradeDialog(SearchSubjectActivity.this))
                .setValue(defaultGrade);


        List<DropBean> subjectlistData = new ArrayList<>();
        subjectlistData.add(new DropBean("", "科目"));
        subjectlistData.add(new DropBean("语文"));
        subjectlistData.add(new DropBean("数学"));
        subjectlistData.add(new DropBean("英语"));
        subjectlistData.add(new DropBean("物理"));
        subjectlistData.add(new DropBean("化学"));
        subjectlistData.add(new DropBean("政治"));
        subjectlistData.add(new DropBean("历史"));
        subjectlistData.add(new DropBean("地理"));
        subjectlistData.add(new DropBean("奥数"));
        subjectlistData.add(new DropBean("科学"));
        subjectlistData.add(new DropBean("不限"));
        subjectlistSel.setDropPopupDialog(new DropSelectorDialog(SearchSubjectActivity.this).init(subjectlistData))
                .setValue(subjectlistData.get(0));

        List<DropBean> otherlistData = new ArrayList<>();
        otherlistData.add(new DropBean("", "筛选", "0"));
        otherlistData.add(new DropBean("1","男老师", "1"));
        otherlistData.add(new DropBean("0","女老师", "1"));
        otherlistData.add(new DropBean("1","5年以下", "2"));
        otherlistData.add(new DropBean("2","5-10年", "2"));
        otherlistData.add(new DropBean("3","10年以上", "2"));
        otherlistSel.setDropPopupDialog(new DropSelectorDialog(SearchSubjectActivity.this).init(otherlistData))
                .setValue(otherlistData.get(0));

        search(null);

    }

    public void searchTeacherList(Course requestParam) {
        Api.FIND_TEACH_BY_FILTER.request(requestParam, new DefaultHttpCallback() {
            @Override
            public void onSucceed(Response response) {
                Log.d(this.getClass().getName(), response.toString());
                JSONArray jsonArray = response.getResult();
                //加载老师信息
                List<Teacher> teachers = jsonArray.toJavaList(Teacher.class);
                TeachersListAdapter teachersListAdapter = new TeachersListAdapter(SearchSubjectActivity.this, teachers);
                lv_search_teacher.setAdapter(teachersListAdapter);
                lv_search_teacher.setOnItemClickListener(teachersListAdapter);
            }

            @Override
            public void onFail(Response response) {
                Log.d(getClass().getName(), response.toString());
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.d(getClass().getName(), ex.getMessage());
            }
        });
    }


    /**
     *
     * @param view
     */
    @Event(value = R.id.btn_search_subjects, type = View.OnClickListener.class)
    private void search(View view) {
        Course courseRequest = new Course();
        courseRequest.setInputText(tv_search_text.getText().toString());
        courseRequest.setOrderRule(orderlistSel.getValue().getValue());
        courseRequest.setCourseClass(sel_gradelist.getValue().getValue());
        courseRequest.setSubject(subjectlistSel.getValue().getValue());
        String otherFilterValue = otherlistSel.getValue().getValue();
        if (null != otherFilterValue) {
            if (otherlistSel.getValue().getType().equals("1")) {
                courseRequest.setSex(otherFilterValue);
            } else if (otherlistSel.getValue().getType().equals("2")) {
                courseRequest.setTeachYearType(otherFilterValue);
            }
        }
        searchTeacherList(courseRequest);
    }
}
