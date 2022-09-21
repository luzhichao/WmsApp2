package com.linkingwin.elearn.teacher.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.adapter.ListViewAdapter;
import com.linkingwin.elearn.common.adapter.ViewHolder;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.teacher.activities.SchoolEdit;
import com.linkingwin.elearn.teacher.activities.TeacherBaseInfo;
import com.linkingwin.elearn.teacher.model.SchoolVO;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;

import java.util.List;

import cn.finalteam.okhttpfinal.HttpCycleContext;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;

public class SchoolAdapter extends ListViewAdapter<SchoolVO> {

    private List<SchoolVO> allSchool;
    private Context context;

    /**
     * layoutId是listViewItem的ID，listViewItem放入再listView中
     * Adapter需要一个Context，通过Context获得Layout.inflater，然后通过inflater加载item的布局
     * @param context
     * @param allSchool
     * @param layoutId
     */
    public SchoolAdapter(Context context, List<SchoolVO> allSchool, int layoutId) {
        super( context, allSchool, layoutId );
        this.allSchool = allSchool;
        this.context = context;
    }

    @Override
    public void bindView(ViewHolder holder, SchoolVO schoolVO) {
        //获取对象并赋值
        TextView tv_start_date = holder.getView( R.id.tv_start_date );
        TextView tv_end_date = holder.getView( R.id.tv_end_date );
        TextView tv_grad_school = holder.getView( R.id.tv_grad_school );
        TextView tv_degree = holder.getView( R.id.tv_degree );
        TextView tv_major = holder.getView( R.id.tv_major );
        ToggleButton tb_switch = holder.getView( R.id.tb_switch );
        ImageView iv_edit = holder.getView( R.id.iv_edit );
        //对象初始化数据
        tv_start_date.setText( schoolVO.getBeginDate() );
        tv_end_date.setText( schoolVO.getEndDate() );
        tv_grad_school.setText( schoolVO.getSchoolName() );
        tv_degree.setText( schoolVO.getEducation() );
        tv_major.setText( schoolVO.getMajor() );
        tb_switch.setChecked( Boolean.valueOf( schoolVO.getIsOpen() ));
        iv_edit.setImageResource( R.drawable.arrows_right );
        //监听初始化
        setEditListener( holder );
        setOpenListener(holder);
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
                SchoolVO model = getItem( position );
                Intent intent = new Intent( context,
                        SchoolEdit.class );
                intent.putExtra( "schoolVO", JSONObject.toJSONString( model ) );
                intent.putExtra( "allSchool", JSONObject.toJSONString( mDatas ) );
                intent.putExtra( "action","edit" );
                context.startActivity( intent );
            }
        } );
    }

    /**
     * 是否对外展示按钮监听设置
     * todo
     */
    private void setOpenListener(final ViewHolder holder) {
        //开关按钮监听设置
        holder.setOnClickListener( R.id.tb_switch, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getItemPosition();
                final SchoolVO school = getItem( position );
                school.setIsOpen( Boolean.toString( !Boolean.valueOf( school.getIsOpen() ) ) );
                int size = allSchool.size();
                if (size > 0) {
                    String id = school.getId();
                    for (int i = 0; i < size; i++) {
                        SchoolVO tmpVO = allSchool.get( i );
                        if (!StringUtils.isBlank( id ) && id.equals( tmpVO.getId() )) {
                            allSchool.remove( i );
                            break;
                        }
                        if (StringUtils.isBlank( id )) {
                            allSchool.remove( i );
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setGradSchool( JSONObject.toJSONString( allSchool ) );
                            ElearnApplication.teachBusinessInfo=teachBusinessInfo;
                            Tools.setShareInfo( context, "TeachBusinessInfo", teachBusinessInfo );
                            notifyUpdate();
                            return;
                        }
                    }
                }
                allSchool.add( school );
                final String allSchoolJson = JSONObject.toJSONString( allSchool );
                //2.获取请求参数
                RequestParams params = RequestParamsBuilder.buildRequestParams( (HttpCycleContext) context );
                params.addFormDataPart( "gradSchool", allSchoolJson );

                //3.发起请求
                HttpRequest.post( Api.POST_SAVE_TEACH_INFO, params, new JsonHttpRequestCallback() {
                    protected void onSuccess(JSONObject jsonObject) {
                        super.onSuccess( jsonObject );
                        String msg = jsonObject.getString( "msg" + "" );
                        if (jsonObject.getBoolean( "success" )) {
//                            TeachBusinessInfo teachBusinessInfo = Tools.getSharedInfo( "TeachBusinessInfo", Tools.getSharedPre( context ), TeachBusinessInfo.class );
                            TeachBusinessInfo teachBusinessInfo = ElearnApplication.teachBusinessInfo;
                            teachBusinessInfo.setGradSchool( allSchoolJson );
                            //1.提交成功更新本地的shared数据
                            Tools.setShareInfo( context, "TeachBusinessInfo", teachBusinessInfo );
                            ElearnApplication.teachBusinessInfo = teachBusinessInfo;
                            //2.提交成功提示
                            makeText( context, "保存成功", LENGTH_SHORT ).show();
                            //3.打印日志
                            Log.d( "SchoolEdit_Success", msg + "" );
                        } else {
                            makeText( context, "提交失败", LENGTH_SHORT ).show();
                            Log.d( "SchoolEdit_Fail", msg + "" );
                        }
                    }

                    //请求失败（服务返回非法JSON、服务器异常、网络异常）
                    @Override
                    public void onFailure(int errorCode, String msg) {
                        makeText( context, "网络原因，提交失败", LENGTH_SHORT ).show();
                        allSchool.remove( school );//没有保存成功则从list中还原修改的内容
                        school.setIsOpen( Boolean.toString( !Boolean.valueOf( school.getIsOpen() ) ) );
                        allSchool.add( school );
                        Log.d( "SchoolEdit_Fail", errorCode + ":" + msg );
                    }

                    //请求网络结束
                    @Override
                    public void onFinish() {
                        Log.d( "SchoolEdit_End", "请求结束" );
                    }
                } );
            }
        } );
    }

}
