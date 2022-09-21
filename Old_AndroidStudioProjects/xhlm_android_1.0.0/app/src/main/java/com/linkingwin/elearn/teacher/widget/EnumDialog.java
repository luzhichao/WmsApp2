package com.linkingwin.elearn.teacher.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.widget.DialogFromBottom;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;

import java.util.Calendar;
import java.util.List;

import cn.finalteam.toolsfinal.StringUtils;
import lombok.Data;

@Data
public class EnumDialog {

    //底部弹出框
    private DialogFromBottom bottomDialog;
    //底部布局视图
    private View dialogView;
    //底部弹出框的title
    private TextView pupo_title;
    //日期选择dialog
    private LinearLayout linearLayoutDate;
    private WheelView year;
    private WheelView month;
    //学历选择dialog
    private LinearLayout linearLayoutEducation;
    private WheelView education;
    //取消和确认
    private TextView dialogCancle;
    private TextView dialogConfirm;
    //职级选择
    private WheelView jobLevel;
    private View linearLayoutJobLevel;

    /**
     * 初始化底部页面
     */
    public void initDialog(Context context) {
        //1.构造一个底部的dialog
        bottomDialog = new DialogFromBottom( context );
        //2.这一步骤很关键，是在TeacherBaseInfo这个Activity上面加载dialog的布局
        dialogView = LayoutInflater.from( context )
                .inflate( R.layout.wheel_picker, null );
        //3.初始化界面上的元素
        linearLayoutDate = dialogView.findViewById( R.id.ll_date );
        linearLayoutEducation = dialogView.findViewById( R.id.ll_education );
        linearLayoutJobLevel = dialogView.findViewById( R.id.ll_job_level );

        pupo_title = dialogView.findViewById( R.id.pupo_title );

        dialogCancle = dialogView.findViewById( R.id.cancel );
        dialogConfirm = dialogView.findViewById( R.id.confirm );

        year = dialogView.findViewById( R.id.wv_year );
        month = dialogView.findViewById( R.id.wv_month );
        education = dialogView.findViewById( R.id.wv_education );
        jobLevel = dialogView.findViewById( R.id.wv_job_level );
        //4初始化样式
        initWheelStyle( year, context );
        initWheelStyle( month, context );
        initWheelStyle( education, context );
        initWheelStyle( jobLevel, context );
        //5.设置年月日的枚举值
        year.setWheelData( ElearnApplication.listYear );
        month.setWheelData( ElearnApplication.listMonth );
        //6.dialog加载视图
        bottomDialog.setContentView( dialogView );
        bottomDialog.show();
    }

    /**
     * 设置滚动列表的枚举值
     */
    public void setWheelData(int resID, @Nullable List list) {
        switch (resID) {
            case R.id.wv_year:
                //todo
                break;
            case R.id.wv_month:
                //todo
                break;
            case R.id.wv_education:
                education.setWheelData( list );
                education.setLoop( false );
                break;
            case R.id.wv_job_level:
                jobLevel.setWheelData( list );
                break;
        }
    }

    /**
     * 设置需要显示或隐藏的view
     */
    public void setVisiable(int resID, int visibility) {
        switch (resID) {
            case R.id.ll_date:
                linearLayoutDate.setVisibility( visibility );
                break;
            case R.id.ll_education:
                linearLayoutEducation.setVisibility( visibility );
                break;
            case R.id.ll_job_level:
                linearLayoutJobLevel.setVisibility( visibility );
                break;
        }
    }

    /**
     * 设置title
     */
    public void setTitle(String titleName) {
        pupo_title.setText( titleName );
    }

    /**
     * 初始化滚轮显示的位置
     */
    public void setWheelPosition(int resID, int itemID) {
        switch (resID) {
            case R.id.wv_year:
                year.setSelection( itemID );
                break;
            case R.id.wv_month:
                month.setSelection( itemID );
                break;
            case R.id.wv_education:
                education.setSelection( itemID );
                education.setLoop( false );
                break;
            case R.id.wv_job_level:
                jobLevel.setSelection( itemID );
                break;

        }
    }

    /**
     * 初始化日期滚轮显示的位置
     *
     * @param date 只能处理的格式"yyyy-mm"
     */
    public void setDatePosition(String date) {
        int tmpYear;
        int tmpMonth;
        if (!StringUtils.isEmpty( date )) {
            String[] st = date.split( "-" );
            tmpYear = Integer.parseInt( st[0] ) - 1970;
            tmpMonth = Integer.parseInt( st[1] ) - 1;
        } else {
            Calendar cal = Calendar.getInstance();
            tmpYear = cal.get( Calendar.YEAR ) - 1970;
            tmpMonth = cal.get( Calendar.MONTH );
        }
        setWheelPosition( R.id.wv_year, tmpYear );
        setWheelPosition( R.id.wv_month, tmpMonth );
    }

    /**
     * 关闭dialog
     */
    public void dismissDialog() {
        bottomDialog.dismiss();
    }

    //设置取消按钮监听
    public void setCancelOnclickListener(View.OnClickListener onclickListener) {
        dialogCancle.setOnClickListener( onclickListener );
    }

    //设置确认按钮监听
    public void setConfirmOnclickListener(View.OnClickListener onclickListener) {
        dialogConfirm.setOnClickListener( onclickListener );
    }

    /**
     * 初始化wheel的样式
     */
    public void initWheelStyle(WheelView wheelView, Context context) {
        wheelView.setWheelAdapter( new ArrayWheelAdapter( context ) ); // 文本数据源
        wheelView.setSkin( WheelView.Skin.Holo ); // common皮肤
        WheelView.WheelViewStyle style = wheelView.getStyle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style.holoBorderColor = context.getColor( R.color.btBackground );
        }
        wheelView.setStyle( style );
        wheelView.setLoop( true );
    }
}
