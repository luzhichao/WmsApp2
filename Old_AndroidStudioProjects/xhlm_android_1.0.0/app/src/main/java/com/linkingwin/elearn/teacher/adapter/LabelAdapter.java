package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.teacher.activities.TeachTraitTag;
import com.linkingwin.elearn.teacher.model.LabelVO;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import java.util.ArrayList;
import java.util.List;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class LabelAdapter extends ListViewAdapter<LabelVO> {
    boolean deleteButton=false;//是否展示删除按钮，默认不删除
    boolean deleteSubmitHttp=false;//刪除按鈕，是否提交到服務器
    LabelAdapter allTagStatus=null;
    LabelAdapter selectedStatus=null;
    String type="all";//all，表示所有枚举；selected表示被选中枚举
    Context context;

    public LabelAdapter(Context context, List<LabelVO> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context=context;
    }

    public LabelAdapter setDeleteSubmitHttp(boolean deleteSubmitHttp){
        this.deleteSubmitHttp=deleteSubmitHttp;
        return this;
    }

    public void setAllTagStatus(LabelAdapter allTagStatus){
        if (selectedStatus != null) {
            return;
        }else{
            this.allTagStatus=allTagStatus;
        }
    }

    public void setSelectedStatus(LabelAdapter selectedStatus){
        if(allTagStatus!=null){
            return;
        }else {
            this.selectedStatus=selectedStatus;
        }
    }

    public void setType(String type){
        this.type=type;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void bindView(ViewHolder holder, LabelVO labelVO) {
        TextView tag=holder.getView(R.id.tv_tag);
        ImageView del=holder.getView(R.id.iv_tag_del);
        if(!deleteButton){
            del.setVisibility(View.GONE);
        }
        if(labelVO.isSelected()){
            tag.setBackgroundResource(R.color.blue);
        }else{
            tag.setBackgroundResource(R.color.backgroundColor);
        }
        tag.setText(labelVO.getValue());
        setDelListener(holder);
        if("all".equals(type)){
            setSelectListener(holder);
        }
    }

    public LabelAdapter setDeleteButton(boolean visible){
        this.deleteButton=visible;
        return this;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setDelListener(ViewHolder holder){
        holder.setOnClickListener(R.id.iv_tag_del, v -> {
            int position=holder.getItemPosition();
            LabelVO vo=getItem(position);
            if(allTagStatus!=null){
                List<LabelVO> allList=allTagStatus.mDatas;
                for(LabelVO tmp:allList){
                    if(vo.getValue().equals(tmp.getValue())){
                        tmp.setSelected(false);
                    }
                }
            }
            //更新删除的状态
            remove(position);
            notifyUpdate();
            //更新全部的状态
            if(allTagStatus!=null) allTagStatus.notifyUpdate();
            if(deleteSubmitHttp){
                //提交到服务端
                submitSelectedList(mDatas,vo.getTagType());
            }
        });
    }

    /**
     * 提交到服务端
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void submitSelectedList(List<LabelVO> listVO, String tagType){
        List<String> value = new ArrayList<>();
        for (LabelVO tmp : listVO) {
            value.add(tmp.getValue());
        }
        String key;
        if("peculiar".equals(tagType)){
            key="peculiarTag";
        }else if("school".equals(tagType)){
            key="schoolTag";
        }else{
            return;
        }
        //发起网络请求
        RequestParams params = RequestParamsBuilder.buildRequestParams((HttpCycleContext) context);
        if (value.size() >= 0) {
            String tag = String.join(",", value);
            params.addFormDataPart(key, StringUtils.isEmpty(tag)?"@" : tag);
            HttpRequest.post(Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                protected void onSuccess(JSONObject jsonObject) {
                    super.onSuccess(jsonObject);
                    String message = jsonObject.getString("msg");
                    if (jsonObject.getBoolean("success")) {
                        TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                        if("peculiar".equals(tagType)){
                            teachBusinessInfo.setPeculiarTag(tag);
                        }else if("school".equals(tagType)){
                            teachBusinessInfo.setSchoolTag(tag);
                        }
                        //1.提交成功更新本地的shared数据
                        Tools.setShareInfo(context, "TeachBusinessInfo", teachBusinessInfo);
                        //2.提交成功更新全局变量
                        ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                        //3.提交成功
                        makeText(context, "保存成功", LENGTH_SHORT).show();
                        //4.打印日志
                        Log.d("TeachTraitTagSave", message + "");
                    }else {
                        makeText(context, "提交失败", LENGTH_SHORT).show();
                        Log.d("TeachTraitTagSave", message + "");
                    }
                }

                //请求失败（服务返回非法JSON、服务器异常、网络异常）
                @Override
                public void onFailure(int errorCode, String msg) {
                    makeText(context, "网络原因，提交失败", LENGTH_SHORT).show();
                    Log.d("TeachTraitTagSave", errorCode + ":" + msg);
                }
                //请求网络结束
                @Override
                public void onFinish() {
                    Log.d("LoginActivity", "请求结束");
                }
            });
        }

    }

    private void setSelectListener(ViewHolder holder){
        holder.setOnClickListener(R.id.tv_tag, v -> {
            int position=holder.getItemPosition();
            LabelVO vo=getItem(position);
            //1.更新标签枚举的状态
            if(vo.isSelected()){
                return;
            }else{
                vo.setSelected(true);
                notifyUpdate();
                if(selectedStatus!=null) selectedStatus.add(vo);
            }
        });
    }
}
