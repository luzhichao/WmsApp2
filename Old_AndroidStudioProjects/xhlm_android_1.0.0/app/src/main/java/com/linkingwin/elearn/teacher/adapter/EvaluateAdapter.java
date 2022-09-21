package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.teacher.model.EvaluateVO;

import java.util.List;

import cn.finalteam.toolsfinal.StringUtils;

/**
 * 老师评价中心适配器
 */
public class EvaluateAdapter extends ListViewAdapter<EvaluateVO> {

//    private List<EvaluateVO> allEvaluate;
    private Context context;


    public EvaluateAdapter(Context context, List<EvaluateVO> datas, int layoutId) {
        super(context, datas, layoutId);
//        this.allEvaluate = datas;
        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, EvaluateVO vo) {
        ImageView head_icon = holder.getView(R.id.head_icon);
        TextView name = holder.getView(R.id.name);
        TextView grade = holder.getView(R.id.grade);
        TextView evaluate_time = holder.getView(R.id.evaluate_time);
        TextView lessons = holder.getView(R.id.lessons);
        TextView duration = holder.getView(R.id.duration);
        TextView evaluate_content = holder.getView(R.id.evaluate_content);

        if(!StringUtils.isBlank(vo.getAvatar())){
            new GlideLoader().displayImage(context, vo.getAvatar(), head_icon);
        }
        name.setText(vo.getRealName());
        grade.setText(vo.getStudentClass());
        evaluate_time.setText(vo.getEvaluateTime());
        lessons.setText(vo.getLessons());
        duration.setText(vo.getDuration());
        evaluate_content.setText(vo.getEvaluateContent());

    }
}
