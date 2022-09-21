package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.teacher.activities.SchoolEdit;
import com.linkingwin.elearn.teacher.activities.SuccessCase;
import com.linkingwin.elearn.teacher.activities.SuccessCaseEdit;
import com.linkingwin.elearn.teacher.model.SchoolVO;
import com.linkingwin.elearn.teacher.model.SuccessCaseVO;

import java.util.List;

public class SuccessAdapter extends ListViewAdapter<SuccessCaseVO> {
    private List<SuccessCaseVO> allSuccess;
    private Context context;

    public SuccessAdapter(Context context, List<SuccessCaseVO> allSuccess, int layoutId) {
        super( context, allSuccess, layoutId );
        this.allSuccess = allSuccess;
        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, SuccessCaseVO successCaseVO) {
        //1.获取对象并赋值
        TextView tv_title = holder.getView( R.id.tv_title );
        TextView et_describe = holder.getView( R.id.et_describe );
        LinearLayout ll_edit =holder.getView( R.id.ll_edit );
        //2.对象初始化数据
        tv_title.setText( successCaseVO.getTitle() );
        et_describe.setText( successCaseVO.getDescribe() );
        //3.监听初始化
        setEditListener(holder);
    }

    /**
     * 编辑按钮，监听设置
     */
    private void setEditListener(final ViewHolder holder) {
        //编辑按钮监听设置
        holder.setOnClickListener( R.id.ll_edit, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                SuccessCaseVO model = getItem( position );
                Intent intent = new Intent( context,
                        SuccessCaseEdit.class );
                intent.putExtra( "successCaseVO", JSONObject.toJSONString( model ) );
                intent.putExtra( "allSuccess", JSONObject.toJSONString( mDatas ) );
                intent.putExtra( "action","edit" );
                context.startActivity( intent );
            }
        } );
    }
}
