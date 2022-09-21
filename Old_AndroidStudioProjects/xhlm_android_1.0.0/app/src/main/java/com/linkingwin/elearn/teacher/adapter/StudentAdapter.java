package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.teacher.model.StudentVO;

import java.util.List;

import cn.finalteam.toolsfinal.StringUtils;

public class StudentAdapter extends ListViewAdapter<StudentVO> {

    public StudentAdapter(Context context, List<StudentVO> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void bindView(ViewHolder holder, StudentVO studentVO) {

        //获取控件
        ImageView iv_image = holder.getView(R.id.iv_image);
        TextView tv_nick_name = holder.getView(R.id.tv_nick_name);
        TextView tv_status = holder.getView(R.id.tv_status);
        TextView tv_course_name = holder.getView(R.id.tv_course_name);
        TextView tv_buy_courses = holder.getView(R.id.tv_buy_courses);

        RelativeLayout rl_item_order = holder.getView(R.id.rl_item_order);

        if(!StringUtils.isBlank(studentVO.getAvatar())){
            new GlideLoader().displayImage(mContext, studentVO.getAvatar(), iv_image);
        }
        tv_nick_name.setText(studentVO.getNickName());
        tv_course_name.setText(studentVO.getCourseName());
        tv_buy_courses.setText(String.format("购买%d课节", studentVO.getBuyCourses()));
        //0:待支付,1:已付款,2:已取消,3:已完成
        switch (studentVO.getStatus()){
            case "1":
                tv_status.setText("未完课");
                break;
            case "3":
                tv_status.setText("已完课");
                break;
        }
    }
}
