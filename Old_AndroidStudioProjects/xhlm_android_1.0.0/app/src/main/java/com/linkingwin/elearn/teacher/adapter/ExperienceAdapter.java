package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.teacher.activities.ExperienceEdit;
import com.linkingwin.elearn.teacher.model.ExperienceVO;

import java.util.List;

public class ExperienceAdapter extends ListViewAdapter<ExperienceVO> {
    private TextView tv_start_date;
    private TextView tv_end_date;
    //    private LinearLayout ll_edit;
    private TextView tv_title;
    private EditText et_describe;

    //    private List<ExperienceVO> allExperience;
    private Context context;

    public ExperienceAdapter(Context context, List<ExperienceVO> allExperience, int layoutId) {
        super( context, allExperience, layoutId );
//        this.allExperience = allExperience;
        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, ExperienceVO experienceVO) {
        //1.获取对象
        tv_start_date = (TextView) holder.getView( R.id.tv_start_date );
        tv_end_date = (TextView) holder.getView( R.id.tv_end_date );
//        ll_edit = (LinearLayout) holder.getView( R.id.ll_edit );
        tv_title = (TextView) holder.getView( R.id.tv_title );
        et_describe = (EditText) holder.getView( R.id.et_describe );
        //2.对象赋值
        tv_start_date.setText( experienceVO.getBeginDate() );
        tv_end_date.setText( experienceVO.getEndDate() );
        tv_title.setText( experienceVO.getTitle() );
        et_describe.setText( experienceVO.getDescribe() );
        //3.设置监听
        setEditOnlickListener(holder);
    }

    /**
     * 设置编辑按钮的监听
     */
    public void setEditOnlickListener(final ViewHolder holder) {
        //编辑按钮监听设置
        holder.setOnClickListener( R.id.ll_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                ExperienceVO model = getItem( position );
                Intent intent = new Intent( context,
                        ExperienceEdit.class );
                intent.putExtra( "experienceVO", JSONObject.toJSONString( model ) );
                intent.putExtra( "allExperience", JSONObject.toJSONString( mDatas ) );
                intent.putExtra( "action","edit" );
                context.startActivity( intent );
            }
        } );
    }
}
