package com.linkingwin.elearn.teacher.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jaiky.imagespickers.ImageConfig;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.constant.XhlmConstant;
import com.linkingwin.elearn.common.util.ImageSelectorUtil;
import com.linkingwin.elearn.teacher.activities.HonorAddOrEditActivity;
import com.linkingwin.elearn.teacher.model.HonorVO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HonorAdapter extends ListViewAdapter<HonorVO> {

    private List<HonorVO> allHonor;
    private Context context;
    public static final int REQUEST_CODE = XhlmConstant.HONOR_ADAPTER_UPLOAD_REQUEST_CODE;//图片上传请求参数

    public HonorAdapter(Context context, List<HonorVO> datas, int layoutId) {
        super(context, datas, layoutId);
        this.allHonor = datas;
        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, HonorVO honorVO) {
        LinearLayout ll_edit = holder.getView(R.id.ll_edit);
        TextView tv_title = holder.getView(R.id.tv_title);
        LinearLayout llContainer = holder.getView(R.id.llContainer);
        //init data and show image
        tv_title.setText(honorVO.getHonorIntro());
        ArrayList<String> pictureUrls = honorVO.getPictureUrls();
        ImageConfig imageConfig = ImageSelectorUtil.initImageMutiSelector(REQUEST_CODE, (Activity) context, 5, 5, false, pictureUrls, llContainer);
        ImageSelectorUtil.show(imageConfig, pictureUrls);
        //listener
        ll_edit.setOnClickListener(v -> {
            //跳转到编辑页面
            Intent intent = new Intent(context, HonorAddOrEditActivity.class);
            Bundle bundle=new Bundle();
            bundle.putSerializable("allData", (Serializable)allHonor);
            bundle.putSerializable("currData", honorVO);
            bundle.putBoolean("isEdit", true);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }
}
