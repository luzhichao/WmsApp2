package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.teacher.model.TeachTimeVO;

import java.util.List;

public class TeachTimeAdapter extends ListViewAdapter<TeachTimeVO> {
//    private List<TeachTimeVO> teachTimes;
//    private Context context;

    public TeachTimeAdapter(Context context, List<TeachTimeVO> teachTimes, int layoutId) {
        super( context, teachTimes, layoutId );
//        this.teachTimes = teachTimes;
//        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, TeachTimeVO teachTimeVO) {
        //1.获取对象并赋值
        TextView tv_teach_date = holder.getView( R.id.tv_teach_date );
        ImageView iv_teach_time = holder.getView( R.id.iv_teach_time );

        //2.对象初始化数据
        if ("0".equals( teachTimeVO.getType() )) {
            tv_teach_date.setVisibility( View.GONE );
        }else{
            iv_teach_time.setVisibility( View.GONE );
        }
        if("0".equals( teachTimeVO.getTeachTime() )){
            switch (teachTimeVO.getTeachWeek()){
                case "1":
                    tv_teach_date.setText("周一");
                    break;
                case "2":
                    tv_teach_date.setText("周二");
                    break;
                case "3":
                    tv_teach_date.setText("周三");
                    break;
                case "4":
                    tv_teach_date.setText("周四");
                    break;
                case "5":
                    tv_teach_date.setText("周五");
                    break;
                case "6":
                    tv_teach_date.setText("周六");
                    break;
                case "7":
                    tv_teach_date.setText("周日");
                    break;
            }
        }else{
            tv_teach_date.setText( teachTimeVO.getTeachWeek() + "\n" + teachTimeVO.getTeachTime() );
        }
        iv_teach_time.setImageResource( teachTimeVO.getIsConfirm() == "1" ? R.drawable.blue_confirm : R.drawable.gray_confirm );
        //3.监听初始化
        setOnclickListener( holder );
    }

    //设置监听
    private void setOnclickListener(final ViewHolder holder) {
        holder.setOnClickListener( R.id.iv_teach_time, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                TeachTimeVO model = getItem( position );
                String isConfirm=model.getIsConfirm();
                model.setIsConfirm( isConfirm=="1"?"0":"1" );
                notifyUpdate();
            }
        } );
    }
}
