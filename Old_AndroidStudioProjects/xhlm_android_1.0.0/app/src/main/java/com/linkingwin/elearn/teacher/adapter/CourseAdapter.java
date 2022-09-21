package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.teacher.activities.CourseEdit;
import com.linkingwin.elearn.teacher.activities.SchoolEdit;
import com.linkingwin.elearn.teacher.model.CourseVO;
import com.linkingwin.elearn.teacher.model.SchoolVO;

import java.util.List;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

public class CourseAdapter extends ListViewAdapter<CourseVO> {
    private String courseGrade = "小学";//默认显示小学
    private Context context;

    public CourseAdapter(Context context, List<CourseVO> datas, int layoutId) {
        super(context, datas, layoutId);
        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, CourseVO courseVO) {
        //获取控件
        TextView tv_course = holder.getView(R.id.tv_course);
        TextView tv_price = holder.getView(R.id.tv_price);
        TextView tv_type = holder.getView(R.id.tv_type);
        TextView tv_del = holder.getView(R.id.tv_del);
        TextView tv_close = holder.getView(R.id.tv_close);
        //填充数据
        tv_course.setText(courseVO.getCourseName() + "|" + courseVO.getCourseGrade() + "|" + courseVO.getCourseClass() + "|" + courseVO.getSubject());
        tv_price.setText(courseVO.getSourcePrice() + "元/小时");
        tv_type.setText(courseVO.getType());
        tv_del.setEnabled("0".equals(courseVO.getPageBuycount()) || courseVO.getPageBuycount()==null ? true : false);//有销售数量，则不能删除
        tv_close.setText("1".equals(courseVO.getIsAvaliable()) ? "关闭" : "打开");
        //设置监听
        setDelListener(holder);
        setCloseListener(holder);
        setEditListener(holder);
    }

    /**
     * 编辑监听
     *
     * @param holder
     */
    private void setEditListener(final ViewHolder holder) {
        holder.setOnClickListener(R.id.tv_course, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                CourseVO model = getItem(position);
                Intent intent = new Intent(context,
                        CourseEdit.class);
                intent.putExtra("courseVO", JSONObject.toJSONString(model));
                intent.putExtra("action", "edit");
                context.startActivity(intent);
            }
        });
    }

    /**
     * 删除监听
     *
     * @param holder
     */
    private void setDelListener(ViewHolder holder) {
        holder.setOnClickListener(R.id.tv_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                CourseVO courseVO = getItem(position);
                String del = "1";
                //2.提交保存请求
                RequestParams params = RequestParamsBuilder.buildRequestParams((HttpCycleContext) context);
                params.addFormDataPart("id", courseVO.getId());
                params.addFormDataPart("publisher", courseVO.getPublisher());
                params.addFormDataPart("courseName", courseVO.getCourseName());
                params.addFormDataPart("courseGrade", courseVO.getCourseGrade());
                params.addFormDataPart("courseClass", courseVO.getCourseClass());
                params.addFormDataPart("type", courseVO.getType());
                params.addFormDataPart("peopleNum", courseVO.getPeopleNum());
                params.addFormDataPart("subject", courseVO.getSubject());
                params.addFormDataPart("sourcePrice", courseVO.getSourcePrice());
                params.addFormDataPart("lessons", courseVO.getLessons());
                params.addFormDataPart("minBuy", courseVO.getMinBuy());
                params.addFormDataPart("duration", courseVO.getDuration());
                params.addFormDataPart("delFlag", del);
                //发起请求
                HttpRequest.post(Api.POST_COURSES_SAVE_API, params, new JsonHttpRequestCallback() {

                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess(jsonObject);
                        String message = "删除成功";
                        remove(position);
                        notifyUpdate();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        Toast.makeText(context, "网络异常，登陆失败", Toast.LENGTH_SHORT).show();
                        Log.d("CourseOpen", errorCode + ":" + msg);
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d("CourseOpen", "请求结束");
                    }
                });
            }
        });
    }

    /**
     * 关闭监听
     *
     * @param holder
     */
    private void setCloseListener(ViewHolder holder) {
        holder.setOnClickListener(R.id.tv_close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                CourseVO courseVO = getItem(position);
                String isAvaliable = "1".equals(courseVO.getIsAvaliable()) ? "0" : "1";
                //2.提交保存请求
                RequestParams params = RequestParamsBuilder.buildRequestParams((HttpCycleContext) context);
                params.addFormDataPart("id", courseVO.getId());
                params.addFormDataPart("publisher", courseVO.getPublisher());
                params.addFormDataPart("courseName", courseVO.getCourseName());
                params.addFormDataPart("courseGrade", courseVO.getCourseGrade());
                params.addFormDataPart("courseClass", courseVO.getCourseClass());
                params.addFormDataPart("type", courseVO.getType());
                params.addFormDataPart("peopleNum", courseVO.getPeopleNum());
                params.addFormDataPart("subject", courseVO.getSubject());
                params.addFormDataPart("sourcePrice", courseVO.getSourcePrice());
                params.addFormDataPart("lessons", courseVO.getLessons());
                params.addFormDataPart("minBuy", courseVO.getMinBuy());
                params.addFormDataPart("duration", courseVO.getDuration());
                params.addFormDataPart("isAvaliable", isAvaliable);
                //发起请求
                HttpRequest.post(Api.POST_COURSES_SAVE_API, params, new JsonHttpRequestCallback() {

                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess(jsonObject);
                        String message = "1".equals(isAvaliable) ? "打开成功" : "关闭成功";
                        courseVO.setIsAvaliable(isAvaliable);
                        notifyUpdate();
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        Toast.makeText(context, "网络异常，登陆失败", Toast.LENGTH_SHORT).show();
                        Log.d("CourseOpen", errorCode + ":" + msg);
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d("CourseOpen", "请求结束");
                    }
                });
            }
        });
    }
}
