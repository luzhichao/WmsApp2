package com.linkingwin.elearn.teacher.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.DialogFromBottom;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.wx.wheelview.widget.WheelView;

import java.util.Iterator;
import java.util.Set;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class DialogSubjectAge {
    //底部弹出框
    private DialogFromBottom bottomDialog;
    //底部取消
    private TextView dialogCancle;
    //底部布局视图
    private View dialogView;
    //底部课程和学部布局的父布局，初始状态隐藏
    private LinearLayout LinearLayout_dep_Sub;
    //底部老师教龄布局
    private LinearLayout linearLayoutTeachingAge;
    //底部滚动选择器，教龄选择
    private WheelView wheelViewTeachingAge;
    //设置dialog的标题
    private TextView dialogTitle;
    //本实例的teacherModel
    TeachBusinessInfo teachBusinessInfo;
    //本选中的dep和subject
    private JSONObject json_TeachGrade = new JSONObject();
    private JSONObject json_TeachSubject = new JSONObject();

    //要传进来的控件显示的内容
    private TextView subject;
    private TextView teachYear;

    private String type;
    private Boolean submitSubjects=false;

    private Context context;



    public DialogSubjectAge(Context context,JSONObject json_TeachGrade,JSONObject json_TeachSubject) {
        this.context = context;
        if(json_TeachGrade==null){
            this.json_TeachGrade=new JSONObject();
        }else{
            this.json_TeachGrade=json_TeachGrade;
        }

        if(json_TeachSubject==null){
            this.json_TeachSubject=new JSONObject();
        }else{
            this.json_TeachSubject=json_TeachSubject;
        }
        this.teachBusinessInfo=ElearnApplication.teachBusinessInfo;
    }

    public DialogSubjectAge setSubjectView(TextView subject) {
        this.subject = subject;
        return this;
    }

    public DialogSubjectAge setTeachYearView(TextView teachYear) {
        this.teachYear = teachYear;
        return this;
    }

    public DialogSubjectAge setTitle(String type) {
        this.type = type;
        dialogTitle = dialogView.findViewById(R.id.dialog_title);
        if ("teachYear".equals(type)) {
            dialogTitle.setText("教龄");
            //点击选择学部控件，打开学部学科的视图，隐藏教龄选择视图
            if (linearLayoutTeachingAge.getVisibility() == View.GONE) {
                linearLayoutTeachingAge.setVisibility( View.VISIBLE );
            }
        } else if ("subject".equals(type)) {
            dialogTitle.setText("授课科目");
            //点击选择学部控件，打开学部学科的视图，隐藏教龄选择视图
            if (LinearLayout_dep_Sub.getVisibility() == View.GONE) {
                LinearLayout_dep_Sub.setVisibility( View.VISIBLE );
            }
        } else {
            return null;
        }
        return this;
    }

    /**
     * bottomeDialog弹出框的初始化
     * 城市选择和课程选择的dialog
     */
    public DialogSubjectAge initDialog() {
        //使用BottomSheetDialog方式实现底部弹窗
        bottomDialog = new DialogFromBottom(context);
        //这一步骤很关键，是在TeacherBaseInfo这个Activity上面加载dialog的布局
        dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_select_subject, null);
        //初始化dialog布局上的需要设置的元素
        dialogCancle = dialogView.findViewById(R.id.cancel);
        //底部确定
        TextView dialogConfirm = dialogView.findViewById(R.id.confirm);
        LinearLayout_dep_Sub = dialogView.findViewById(R.id.ll_dep_Sub);
        //底部课程选择的条目布局
        LinearLayout linearLayoutSubjects = dialogView.findViewById(R.id.ll_subject_item);
        //底部选择主讲学部的布局
        LinearLayout linearLayoutDepartments = dialogView.findViewById(R.id.ll_depart_item);
        linearLayoutTeachingAge = dialogView.findViewById(R.id.ll_teaching_age);
        dialogCancle.setOnClickListener(new ClickAction());
        dialogConfirm.setOnClickListener(new ClickAction());
        //bottomDialog.setContentView(R.layout.dialog_select_subject); //这里一定不能重新加载layout
        initViewConfig(ElearnApplication.getDepartments(), linearLayoutDepartments, json_TeachGrade);
        initViewConfig(ElearnApplication.getSubjects(), linearLayoutSubjects, json_TeachSubject);
        //dialog加载视图
        bottomDialog.setContentView(dialogView);
        //展示dialog
        bottomDialog.show();
        return this;
    }

    /**
     * 设置监听
     */

    /**
     * 页面checkBox配置选项的动态初始化，主要是授课学部和科目
     *
     * @param subjects，当前配置的checkbox枚举值
     * @param parentLinearLayout，需要将动态布局增加到父布局上
     * @param checkedBox，被选中的checkbox
     */
    public void initViewConfig(JSONObject subjects, LinearLayout parentLinearLayout, final JSONObject checkedBox) {
        //清除父组件上的其他view元素
        if (parentLinearLayout != null) parentLinearLayout.removeAllViews();
        JSONObject mapSubject = new JSONObject();
        //获取所有配置checkbox的Key的集合
        Set<String> subjectKeySet = subjects.keySet();
        //获取集合大小
        int size = subjectKeySet.size();
        //获取迭代器
        Iterator<String> subjectItKey = subjectKeySet.iterator();
        //定义列，每行6列
        int colMaxNumber = 6;
        //计算行数，每个linearLayout最多水平放置6个科目
        int lines = size / colMaxNumber + (size % colMaxNumber == 0 ? 0 : 1);

        //循环行数，每行新建LinerLayout布局，并设置布局的样式
        for (int rowindex = 0; rowindex < lines; rowindex++) {
            LinearLayout ll = new LinearLayout(context);
            ll.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ll.setLayoutParams(params2);
            //循环列，每行6列
            for (int colIndex = 0; colIndex < colMaxNumber && subjectItKey.hasNext(); colIndex++) {
                ll.addView(buildCheckBox(subjects, subjectItKey, mapSubject, checkedBox));
            }
            assert parentLinearLayout != null;
            parentLinearLayout.addView(ll);
        }
    }

    /**
     * 生成checkBox，并定义checkBox的样式
     *
     * @param jsonobject，当前配置的checkbox枚举值
     * @param subjectItKey，当前配置的checkbox对应的key
     * @param mapSubject，被选中的checkbox对用的boxid和对应枚举ID，用来通过boxid获取枚举ID值
     * @param checkedBox，被选中的checkbox
     * @return CheckBox
     */
    public CheckBox buildCheckBox(final JSONObject jsonobject, Iterator<String> subjectItKey, final JSONObject mapSubject, final JSONObject checkedBox) {
        String id = subjectItKey.next();
        String text = jsonobject.getString(id);
        //1.获取科目配置JsonObject，并生成checkBox对象
        CheckBox cb = new CheckBox(context);
        int boxid = Tools.buildViewId();
        cb.setId(boxid);
        cb.setText(text);
        cb.setButtonDrawable(null);
        Drawable cbBackground = context.getResources().getDrawable( R.drawable.btn_checkbox_selector);
        cb.setBackground(cbBackground);
        cb.setChecked(false);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        int dp5 = Tools.dip2px(context, 5L);
        params.setMargins(dp5, dp5, dp5, dp5);
        cb.setPadding(dp5, dp5, dp5, dp5);
        cb.setLayoutParams(params);
        if (checkedBox != null)
            cb.setChecked(checkedBox.containsKey(id));
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //通过checkBOX的ID找到枚举值的ID
                String strID = mapSubject.getString("checkbox_" + buttonView.getId());
                //通过枚举值的ID找到对应枚举项
                String strText = jsonobject.getString(strID);
                if (isChecked) {
                    //在对应的对象中增加
                    checkedBox.put(strID, strText);
                } else {
                    //在对应的对象中删除
                    checkedBox.remove(strID);
                }
            }
        });
        mapSubject.put("checkbox_" + boxid, id);
        return cb;
    }

    public void setSubmitSubjects(Boolean submit){
        submitSubjects=submit;
    }

    /**
     * 定义dialog取消和确认点击事件的事务
     */
    public class ClickAction implements View.OnClickListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.cancel:
                    Log.d("diallogCancel", "=========这里执行取消操作=========");
                    if (dialogCancle != null) {
                        bottomDialog.dismiss();
                        LinearLayout_dep_Sub.setVisibility(View.GONE);
                        linearLayoutTeachingAge.setVisibility(View.GONE);
                    }
                    break;
                case R.id.confirm:
                    if ("subject".equals(type)) {
//                        Log.d("diallogConfirm", "=========执行Subject确认=========");
                        subject.setText(teachBusinessInfo.getSubDep( json_TeachSubject,json_TeachGrade));
                        teachBusinessInfo.setTeachSubject(json_TeachSubject.toJSONString());
                        teachBusinessInfo.setTeachGrade(json_TeachGrade.toJSONString());
                        //执行提交更新操作，提交服务端成功后，更新本地数据
                        if(submitSubjects){
                            submitSubjectToServer();
                        }
                        bottomDialog.dismiss();
                    } else if ("teachYear".equals(type)) {
//                        Log.d("diallogConfirm", "=========执行TeachingAge确认=========");
                        teachYear.setText("" + wheelViewTeachingAge.getSelectionItem());
                        //3.关闭dialog
                        bottomDialog.dismiss();
                    }
                    break;
            }
        }
    }

    private void submitSubjectToServer(){
        //发起网络请求
        RequestParams params = RequestParamsBuilder.buildRequestParams((HttpCycleContext) context);
        params.addFormDataPart( "username", teachBusinessInfo.getUsername() );
        params.addFormDataPart( "teachGrade", teachBusinessInfo.getTeachGrade() );
        params.addFormDataPart( "teachSubject", teachBusinessInfo.getTeachSubject() );
        //发起请求
        HttpRequest.post( Api.POST_BASE_INFO, params, new JsonHttpRequestCallback() {
            protected void onSuccess(JSONObject jsonObject) {
                super.onSuccess( jsonObject );
                String message = jsonObject.getString( "msg" );
                if (jsonObject.getBoolean( "success" )) {
                    //1.提交成功更新本地的shared数据
                    Tools.setShareInfo( context, "TeachBusinessInfo", teachBusinessInfo );
                    //2.提交成功更新全局变量
                    ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                    //3.提交成功
                    makeText( context, "保存成功", LENGTH_SHORT ).show();
                    //4.打印日志
                    Log.d( "DialogSubject", message + "" );
                } else {
                    makeText( context, "提交失败", LENGTH_SHORT ).show();
                    Log.d( "DialogSubject", message + "" );
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( context, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "LoginActivity", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "LoginActivity", "请求结束" );
            }
        } );
    }
}
