package com.linkingwin.elearn.teacher.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.jaiky.imagespickers.ImageConfig;
import com.jaiky.imagespickers.ImageSelectorActivity;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.linkingwin.elearn.common.constant.XhlmConstant;
import com.linkingwin.elearn.common.http.Api;
import com.linkingwin.elearn.common.http.RequestParamsBuilder;
import com.linkingwin.elearn.common.util.CityAddress;
import com.linkingwin.elearn.common.util.CityLocation;
import com.linkingwin.elearn.common.util.GlideLoader;
import com.linkingwin.elearn.common.util.ImageSelectorUtil;
import com.linkingwin.elearn.common.util.Tools;
import com.linkingwin.elearn.common.widget.BaseActivity;
import com.linkingwin.elearn.common.widget.DialogFromBottom;
import com.linkingwin.elearn.common.widget.TitleView;
import com.linkingwin.elearn.teacher.LoginActivity;
import com.linkingwin.elearn.teacher.model.LocationModel;
import com.linkingwin.elearn.teacher.model.TeachBusinessInfo;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.zaaach.citypicker.CityPicker;
import com.zaaach.citypicker.adapter.OnPickListener;
import com.zaaach.citypicker.model.City;
import com.zaaach.citypicker.model.LocateState;
import com.zaaach.citypicker.model.LocatedCity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;

import static android.widget.Toast.*;
import static com.linkingwin.elearn.R.id.rb_man;


public class TeacherBaseInfo extends BaseActivity {
    @BindView(R.id.tv_title_teacher_base_info)
    TitleView titleView;
    @BindView(R.id.et_teacher_name)
    EditText realName;
    @BindView(R.id.et_teacher_nickname)
    EditText nickName;
    @BindView(R.id.rg_sex)
    RadioGroup sex;
    @BindView(rb_man)
    RadioButton man;
    @BindView(R.id.rb_woman)
    RadioButton woman;
    @BindView(R.id.tv_city)
    TextView tv_address;
    @BindView(R.id.tv_subject)
    TextView subject;
    @BindView(R.id.tv_teaching_age)
    TextView teachYear;
    @BindView(R.id.iv_my_icon)
    ImageView iv_my_icon;
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
    private JSONObject json_TeachGrade;
    private JSONObject json_TeachSubject;
    //页面跳转标识
    String fromtohere;

    private ImageConfig imageConfig;
    private boolean isChange = false;
    private ArrayList<String> newAvatarPath = new ArrayList<>();
    public static final int REQUEST_CODE = XhlmConstant.MY_AVATAR_UPLOAD_REQUEST_CODE;//图片上传请求参数

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_teacher_base_info );
        ButterKnife.bind( this );
        setTranslucentStatusBar( true );
        //1.定义title的样式
        initTitle();
        //2.初始化老师基本信息数据
        initViewData();
    }

    /**
     * 初始化title
     */
    private void initTitle() {
        //获得跳转的路由
        fromtohere = getIntent().getStringExtra( "fromtohere" );
        if (fromtohere == Api.REGISTER_ROUTER) {
            titleView.setTitleText( getString( R.string.title_teacher_base_info ), R.color.titleWhite ).hideBackIcon();
        } else {
            titleView.setTitleText( "基本信息", R.color.titleWhite );
        }
    }

    /**
     * 页面动态初始化页面数据
     */
    public void initViewData() {
        teachBusinessInfo = ElearnApplication.teachBusinessInfo;
        if (teachBusinessInfo == null) {
            teachBusinessInfo = new TeachBusinessInfo();
        }

        //头像
        String avatar =  "https://s1.ax1x.com/2018/05/19/CcdVQP.png";
        if(!StringUtils.isEmpty(ElearnApplication.teachBusinessInfo.getAvatar())){
            avatar=ElearnApplication.teachBusinessInfo.getAvatar();
        }
        new GlideLoader().displayImage( this, avatar, iv_my_icon );

        //获取老师基本信息,将基本信息填充到页面
        realName.setText( teachBusinessInfo.getRealName() );
        nickName.setText( teachBusinessInfo.getNickName() );
        if (Api.SEX_WOMAN.equals( teachBusinessInfo.getSex() )) {
            woman.setChecked( true );
        } else if (Api.SEX_MAN.equals( teachBusinessInfo.getSex() )) {
            man.setChecked( true );
        } else {
            woman.setChecked( false );
            man.setChecked( false );
        }
        tv_address.setText( teachBusinessInfo.getAddress() );

        json_TeachGrade = JSONObject.parseObject( teachBusinessInfo.getTeachGrade() );
        json_TeachSubject = JSONObject.parseObject( teachBusinessInfo.getTeachSubject() );

        if (json_TeachGrade != null && json_TeachSubject != null)
            subject.setText( teachBusinessInfo.getSubDep( json_TeachSubject, json_TeachGrade ) );
        else {
            json_TeachGrade = new JSONObject();
            json_TeachSubject = new JSONObject();
        }

        teachYear.setText( teachBusinessInfo.getTeachYear() );
    }


    /**
     * 基本信息提交
     */
    @OnClick(R.id.bt_teacher_info_submit)
    public void submit(View view) {
        //获取用户登陆账号和基本信息
        JSONObject jsonObject = ElearnApplication.getJsonLoinInfo();
        if (jsonObject == null) {
            makeText( getContext(), "请重新登陆", LENGTH_LONG ).show();
            Intent intent = new Intent( TeacherBaseInfo.this,
                    LoginActivity.class );
            TeacherBaseInfo.this.startActivity( intent );
            finish();
            return;
        }
        //修改了头像先上传头像再保存信息，否则异步处理
        if(isChange){
            uploadMyAvatar();
        } else {
            registerSubmit( this, null );
        }
    }

    /**
     * 完善信息提交执行业务逻辑
     *
     * @param context
     */
    public void registerSubmit(final Context context, String avatarPath) {
        //页面各个控件值的组装
        //初次登陆全局变量为空
        teachBusinessInfo = ElearnApplication.teachBusinessInfo;
        if (teachBusinessInfo == null) {
            teachBusinessInfo = new TeachBusinessInfo();
            teachBusinessInfo.setUsername( ElearnApplication.getJsonLoinInfo().getString( "username" ) );
        }
        if(!StringUtils.isBlank(avatarPath)){
            teachBusinessInfo.setAvatar(avatarPath);
        }
        teachBusinessInfo.setTeachSubject( json_TeachSubject.toJSONString() );
        teachBusinessInfo.setTeachGrade( json_TeachGrade.toJSONString() );
        teachBusinessInfo.setRealName( realName.getText() + "" );
        teachBusinessInfo.setNickName( nickName.getText() + "" );
        teachBusinessInfo.setSex( String.valueOf( woman.isChecked() ? 1 : (man.isChecked() ? 0 : -1) ) );
        teachBusinessInfo.setTeachYear( teachYear.getText().toString() );
        teachBusinessInfo.setAddress( tv_address.getText().toString() );
        String msg = checkedBaseInfo();
        if (!Boolean.valueOf( msg )) {
            makeText( TeacherBaseInfo.this, msg, LENGTH_SHORT );
            return;
        }
        //发起网络请求
        RequestParams params = RequestParamsBuilder.buildRequestParams( this );
        //添加请求参数
        params.addFormDataPart( "username", teachBusinessInfo.getUsername() );
        params.addFormDataPart( "realName", teachBusinessInfo.getRealName() );
        params.addFormDataPart( "teachGrade", teachBusinessInfo.getTeachGrade() );
        params.addFormDataPart( "teachSubject", teachBusinessInfo.getTeachSubject() );
        params.addFormDataPart( "nickName", teachBusinessInfo.getNickName() );
        params.addFormDataPart( "sex", teachBusinessInfo.getSex() );
        params.addFormDataPart( "avatar", teachBusinessInfo.getAvatar() );
        params.addFormDataPart( "teachYear", teachBusinessInfo.getTeachYear() );
        params.addFormDataPart( "address", teachBusinessInfo.getAddress() );
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
                    //3.提交成功提示并跳转
                    if (Api.REGISTER_ROUTER.equals( fromtohere )) {
                        makeText( TeacherBaseInfo.this, "提交成功", LENGTH_SHORT ).show();
                        //如果页面从注册页面跳转来，则需要跳转到注册成功页面
                        Tools.toOtherPage( context, RegisterSuccess.class, true, null );
                    } else {
                        //如果页面从我的中心->基本信息页面跳转到这里，则土司提醒
                        makeText( TeacherBaseInfo.this, "保存成功", LENGTH_SHORT ).show();
                    }
                    //3.打印日志
                    Log.d( "Login_Success", message + "" );
                } else {
                    makeText( TeacherBaseInfo.this, "提交失败", LENGTH_SHORT ).show();
                    Log.d( "Login_Failure", message + "" );
                }
            }

            //请求失败（服务返回非法JSON、服务器异常、网络异常）
            @Override
            public void onFailure(int errorCode, String msg) {
                makeText( TeacherBaseInfo.this, "网络原因，提交失败", LENGTH_SHORT ).show();
                Log.d( "LoginActivity", errorCode + ":" + msg );
            }

            //请求网络结束
            @Override
            public void onFinish() {
                Log.d( "LoginActivity", "请求结束" );
            }
        } );
    }

    /**
     * 用户基本信息填写判断
     */
    public String checkedBaseInfo() {
        String msg = "true";
        if (realName.getText().toString() == "") {
            msg = "请填写真实姓名";
        }
        if (nickName.getText().toString() == "") {
            msg = "请填写昵称";
        }
        if (tv_address.getText().toString() == "") {
            msg = "请选择所在城市";
        }
        if (json_TeachGrade == null) {
            msg = "请选择学部";
        }
        if (json_TeachSubject == null) {
            msg = "请选择学科";
        }
        if (teachYear.getText().toString() == "") {
            msg = "请选择教龄";
        }
        return msg;
    }

    /**
     * 打开图像选择器
     *
     * @param v
     */
    @OnClick(R.id.ll_my_icon)
    public void selectIcon(View v) {
        imageConfig = ImageSelectorUtil.initImageSelector(REQUEST_CODE, this);
    }

    /**
     * 打开城市选择页面
     */
    @OnClick(R.id.ll_city)
    public void selectCity(View view) {
        openPickCity();
    }

    /**
     * cityPicker初始化
     */
    public void openPickCity() {
        CityLocation cityLocation = new CityLocation( TeacherBaseInfo.this );
        Location location;
        final CityAddress cityAddress = new CityAddress( TeacherBaseInfo.this );//获取城市地址
        if (cityLocation.isProviderEnabled()) {
            Log.d( "GPS", "没有启动" );
//            return;
        } else if (cityLocation.isProviderAccess()) {
            Log.d( "GPS", "没有权限" );
//            return;
        } else {
            //只有当GPS启动和有权限才会调用接口获取位置信息
            location = cityLocation.getLocation();//获取经纬度
            cityAddress.getCityAddress( location );//开始通过经纬度解析到城市
        }

        CityPicker.from( TeacherBaseInfo.this )
                .enableAnimation( true )
                .setAnimationStyle( R.style.DefaultCityPickerAnimation )
                .setLocatedCity( null )
////                .setHotCities(hotCities)
                .setOnPickListener( new OnPickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onPick(int position, City data) {
                        //增加选择后的事件处理，将处理结果
                        ElearnApplication.getSelectCity().setCity( data.getName() );
                        ElearnApplication.getSelectCity().setProvince( data.getProvince() );
                        ElearnApplication.getSelectCity().setCityCod( data.getCode() );
                        tv_address.setText( ElearnApplication.getSelectCity().getProvince() + ElearnApplication.getSelectCity().getCity() );
                    }

                    @Override
                    public void onCancel() {
                        makeText( getApplicationContext(), "取消选择", LENGTH_SHORT ).show();
                    }

                    @Override
                    public void onLocate() {
                        //开始定位，这里模拟一下定位
                        new Handler().postDelayed( new Runnable() {
                            @Override
                            public void run() {
                                if (cityAddress.getResult() != null) {
                                    /*
                                      展示自动定位的结果
                                     */
                                    LocationModel loactionModel = new LocationModel();
//                                    Log.d( "loactionMode", cityAddress.getResult().toJSONString() );
                                    loactionModel = loactionModel.resloveBaiduMap( cityAddress.getResult() );
//                                    Log.d( "loactionMode", loactionModel.getCity()+loactionModel.getProvince()+loactionModel.getCityCod());
                                    CityPicker.from( TeacherBaseInfo.this ).locateComplete( new LocatedCity( loactionModel.getCity(), loactionModel.getProvince(), loactionModel.getCityCod() ), LocateState.SUCCESS );
                                }
                            }
                        }, 1000 );
                    }
                } )
                .show();
    }

    /**
     * 打开老师教龄页面
     */
    @OnClick(R.id.ll_teaching_year)
    public void openTeachingYear(View view) {
        //初始化bottomeDialog的相关视图
        initDialog();
        //设置dialog显示的标题
        dialogTitle = dialogView.findViewById( R.id.dialog_title );
        dialogTitle.setText( "选择教龄" );
        //加载滑动列表的的数据
        wheelViewTeachingAge = dialogView.findViewById( R.id.wv_teaching_age );
        //初始化滑动列表的数据内容
        Tools.initWheelStyle( wheelViewTeachingAge, this );
        //生成教龄列表list
        int index = 0;
        List<String> list = new ArrayList<>();
        while (index++ < 30) {
            list.add( "" + index );
        }
        wheelViewTeachingAge.setWheelData( list );
        //点击选择教龄控件，打开教龄的视图，隐藏学部、学科的视图
        if (linearLayoutTeachingAge.getVisibility() == View.GONE) {
            linearLayoutTeachingAge.setVisibility( View.VISIBLE );
        }
        //初始化显示的位置
        String age = teachYear.getText().toString();
        wheelViewTeachingAge.setSelection( Integer.valueOf( "".equals( age ) ? 1 + "" : age ) - 1 );
    }

    /**
     * 打开选择学部、学科dialog
     */
    @OnClick(R.id.ll_subject)
    public void openSubject(View view) {
        //初始化bottomeDialog的相关视图
        initDialog();
        //设置dialog显示的标题
        dialogTitle = dialogView.findViewById( R.id.dialog_title );
        dialogTitle.setText( "授课科目" );
        //点击选择学部控件，打开学部学科的视图，隐藏教龄选择视图
        if (LinearLayout_dep_Sub.getVisibility() == View.GONE) {
            LinearLayout_dep_Sub.setVisibility( View.VISIBLE );
        }
    }


    /**
     * bottomeDialog弹出框的初始化
     * 城市选择和课程选择的dialog
     */
//    @SuppressLint("InflateParams")
    public void initDialog() {
        //使用BottomSheetDialog方式实现底部弹窗
        bottomDialog = new DialogFromBottom( getContext() );
        //这一步骤很关键，是在TeacherBaseInfo这个Activity上面加载dialog的布局
        dialogView = LayoutInflater.from( TeacherBaseInfo.this )
                .inflate( R.layout.dialog_select_subject, null );
        //初始化dialog布局上的需要设置的元素
        dialogCancle = dialogView.findViewById( R.id.cancel );
        //底部确定
        TextView dialogConfirm = dialogView.findViewById( R.id.confirm );
        LinearLayout_dep_Sub = dialogView.findViewById( R.id.ll_dep_Sub );
        //底部课程选择的条目布局
        LinearLayout linearLayoutSubjects = dialogView.findViewById( R.id.ll_subject_item );
        //底部选择主讲学部的布局
        LinearLayout linearLayoutDepartments = dialogView.findViewById( R.id.ll_depart_item );
        linearLayoutTeachingAge = dialogView.findViewById( R.id.ll_teaching_age );
        dialogCancle.setOnClickListener( new ClickAction() );
        dialogConfirm.setOnClickListener( new ClickAction() );
        //bottomDialog.setContentView(R.layout.dialog_select_subject); //这里一定不能重新加载layout
        initViewConfig( ElearnApplication.getDepartments(), linearLayoutDepartments, json_TeachGrade );
        initViewConfig( ElearnApplication.getSubjects(), linearLayoutSubjects, json_TeachSubject );
        //dialog加载视图
        bottomDialog.setContentView( dialogView );
        //展示dialog
        bottomDialog.show();
    }

    /**
     * 页面checkBox配置选项的动态初始化，主要是授课学部和科目
     *
     * @param subjects，当前配置的checkbox枚举值
     * @param parentLinearLayout，需要将动态布局增加到父布局上
     * @param checkedBox，被选中的checkbox
     */
    public void initViewConfig(JSONObject subjects, LinearLayout parentLinearLayout, JSONObject checkedBox) {
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
            LinearLayout ll = new LinearLayout( getContext() );
            ll.setOrientation( LinearLayout.HORIZONTAL );
            LinearLayout.LayoutParams params2 = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT );
            ll.setLayoutParams( params2 );
            //循环列，每行6列
            for (int colIndex = 0; colIndex < colMaxNumber && subjectItKey.hasNext(); colIndex++) {
                ll.addView( buildCheckBox( subjects, subjectItKey, mapSubject, checkedBox ) );
            }
            assert parentLinearLayout != null;
            parentLinearLayout.addView( ll );
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
        String text = jsonobject.getString( id );
        //1.获取科目配置JsonObject，并生成checkBox对象
        CheckBox cb = new CheckBox( TeacherBaseInfo.this );
        int boxid = Tools.buildViewId();
        cb.setId( boxid );
        cb.setText( text );
        cb.setButtonDrawable( null );
        Drawable cbBackground = getContext().getResources().getDrawable( R.drawable.btn_checkbox_selector );
        cb.setBackground( cbBackground );
        cb.setChecked( false );
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        int dp5 = Tools.dip2px( getContext(), 5L );
        params.setMargins( dp5, dp5, dp5, dp5 );
        cb.setPadding( dp5, dp5, dp5, dp5 );
        cb.setLayoutParams( params );
        if (checkedBox != null)
            cb.setChecked( checkedBox.containsKey( id ) );
        cb.setOnCheckedChangeListener( new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //通过checkBOX的ID找到枚举值的ID
                String strID = mapSubject.getString( "checkbox_" + buttonView.getId() );
                //通过枚举值的ID找到对应枚举项
                String strText = jsonobject.getString( strID );
                if (isChecked) {
                    //在对应的对象中增加
                    checkedBox.put( strID, strText );
                } else {
                    //在对应的对象中删除
                    checkedBox.remove( strID );
                }
            }
        } );
        mapSubject.put( "checkbox_" + boxid, id );
        return cb;
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
                    Log.d( "diallogCancel", "=========这里执行取消操作=========" );
                    if (dialogCancle != null) {
                        bottomDialog.dismiss();
                        LinearLayout_dep_Sub.setVisibility( View.GONE );
                        linearLayoutTeachingAge.setVisibility( View.GONE );
                    }
                    break;
                case R.id.confirm:
                    if (LinearLayout_dep_Sub.getVisibility() == View.VISIBLE && linearLayoutTeachingAge.getVisibility() == View.GONE) {
                        Log.d( "diallogConfirm", "=========执行Subject确认=========" );
                        subject.setText( teachBusinessInfo.getSubDep( json_TeachSubject, json_TeachGrade ) );
                        bottomDialog.dismiss();
                    } else if (linearLayoutTeachingAge.getVisibility() == View.VISIBLE && LinearLayout_dep_Sub.getVisibility() == View.GONE) {
                        Log.d( "diallogConfirm", "=========执行TeachingAge确认=========" );
                        teachYear.setText( "" + wheelViewTeachingAge.getSelectionItem() );
                        //3.关闭dialog
                        bottomDialog.dismiss();
                    }
                    break;
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            isChange = true;
            newAvatarPath = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            //TODO 上传头像
            imageConfig.getImageLoader().displayImage(this.getContext(), newAvatarPath.get(0), iv_my_icon);
        }
    }

    /**
     * 上传我的头像
     */
    public void uploadMyAvatar(){
        if(isChange){
            RequestParams params = RequestParamsBuilder.buildRequestParams(this);
            params.addFormDataPart("file" , new File(newAvatarPath.get(0)));
            params.addFormDataPart("merchantId", XhlmConstant.UPLOAD_MERCHANTID);
            params.addFormDataPart("channel", XhlmConstant.UPLOAD_CHANNEL);
            HttpRequest.post(Api.POST_UPLOAD_FILE, params, new JsonHttpRequestCallback() {
                @Override
                protected void onSuccess(JSONObject jsonObject) {
                    super.onSuccess( jsonObject );
                    if (jsonObject.getBoolean( "success" )) {
                        String avatarPath = jsonObject.getString("result");
                        registerSubmit( getContext(), avatarPath );
                    }
                }
                //请求失败（服务返回非法JSON、服务器异常、网络异常）
                @Override
                public void onFailure(int errorCode, String msg) {
                    makeText(getContext(), "网络原因，提交失败", LENGTH_SHORT ).show();
                    Log.d( "submit_auth_fail", errorCode + ":" + msg );
                }
                //请求网络结束
                @Override
                public void onFinish() {
                    Log.d( "submit_auth_end", "请求结束" );
                }
            });
        }
    }
}
