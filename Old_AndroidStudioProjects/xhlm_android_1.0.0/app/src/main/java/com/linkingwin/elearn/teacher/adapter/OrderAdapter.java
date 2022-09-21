package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.teacher.model.OrderVO;

import java.util.List;

import cn.finalteam.toolsfinal.StringUtils;

public class OrderAdapter extends ListViewAdapter<OrderVO> {

    public OrderAdapter(Context context, List<OrderVO> datas, int layoutId) {
        super(context, datas, layoutId);
    }

    @Override
    public void bindView(ViewHolder holder, OrderVO orderVO) {
        //获取控件
        ImageView iv_image = holder.getView(R.id.iv_image);
        TextView tv_course_name = holder.getView(R.id.tv_course_name);
        TextView tv_current_price = holder.getView(R.id.tv_current_price);
        TextView tv_buy_courses = holder.getView(R.id.tv_buy_courses);
        TextView tv_order_status = holder.getView(R.id.tv_order_status);

        RelativeLayout rl_item_order = holder.getView(R.id.rl_item_order);

        if(!StringUtils.isBlank(orderVO.getLogo())){
            new GlideLoader().displayImage(mContext, orderVO.getLogo(), iv_image);
        }
        tv_course_name.setText(orderVO.getCourseName());
        tv_current_price.setText(String.format("%s元/课节", orderVO.getCurrentPrice()));
        tv_buy_courses.setText(String.format("购买%d课节", orderVO.getBuyCourses()));
        //0:待支付,1:已付款,2:已取消,3:已完成
        switch (orderVO.getStatus()){
            case "0":
                tv_order_status.setText("待支付");
                break;
            case "1":
                tv_order_status.setText("已付款");
                break;
            case "2":
                tv_order_status.setText("已取消");
                break;
            case "3":
                tv_order_status.setText("已完成");
                break;
        }

        //TODO 设置监听,跳转到详情页
        rl_item_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
//                makeText( mContext, "点击跳转详情页面", LENGTH_SHORT ).show();
            }
        });

    }
}
