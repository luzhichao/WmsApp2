package com.linkingwin.elearn.common.util;


import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.ElearnApplication;
import com.wx.wheelview.adapter.ArrayWheelAdapter;
import com.wx.wheelview.widget.WheelView;
import com.youth.banner.Banner;


import com.linkingwin.elearn.common.model.Params;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Tools {
    /**
     * 使用SharedPreferences用教师的信息，方便后面读取
     * @param context
     * @param map
     */
    public static void saveInfo2Preferences(Context context,Map<String,String> map){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=getSharedPre(context);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        //设置参数
        for (String key : map.keySet()) {  //通过foreach方法来遍历
//            System.out.println("key= "+ key + " and value= " + map.get(key));
            editor.putString(key,map.get(key));
        }
        //提交
        editor.commit();
    }

    /**
     * 使用SharedPreferences用
     * @param context
     * @param key
     * @param value
     */
    public static void saveInfo2SharedForStr(Context context,String key,String value){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=getSharedPre(context);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.putString( key,value );
        //提交
        editor.commit();
    }

    /**
     * 获取本地Shared共享信息,通过泛型直接转化对应的类
     *
     */
    public static  <T> T  getSharedInfo(String key,SharedPreferences sharedPre,Class<T> clazz){
        String json=sharedPre.getString(key, "");
        if (json == "" || json == null){
            return null;
        }
        return JSONObject.parseObject(json,clazz );
    }

    /**
     * 获取本地Shared共享信息，通过泛型直接转化对应的list<T>
     */
    public static <T> List<T> getSharedInfoList(String key,SharedPreferences sharedPre,Class<T> clazz){
        String arrayJson=sharedPre.getString( key,"" );
        if(arrayJson == "" || arrayJson == null){
            return null;
        }
        return JSONArray.parseArray( arrayJson,clazz );
    }


    /**
     * 保存VO信息到本地sharedInfo文件中
     */
    public static void setShareInfo(Context context,String key,Object object){
        //获取SharedPreferences对象
        SharedPreferences sharedPre=getSharedPre(context);
        //获取Editor对象
        SharedPreferences.Editor editor=sharedPre.edit();
        editor.putString(key,JSONObject.toJSONString( object ));
        //提交
        editor.commit();
    }

    /**
     * 定义静态方法获取SharedPreferences对象，使用方法如下
     *         SharedPreferences sharedPre=getSharedPre(getContext());
     *         String teachYear=sharedPre.getString("teachYear", "");
     *         Toast.makeText(getContext(),"教龄"+teachYear,Toast.LENGTH_LONG).show();
     */
    public static SharedPreferences getSharedPre(Context context){
        return context.getSharedPreferences("config", android.content.Context.MODE_PRIVATE);
    }

    /**
     *  动态生成布局控件的唯一ID
     */
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger( 1 );

    public static int buildViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
        // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet( result, newValue )) {
                return result;
            }
        }
    }
    /**
     * 获取屏幕宽
     */
    public static int displayWidth(Context context){
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 获取屏幕高
     */
    public static int displayHeight(Context context){
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 加载banner布局
     */
    public static void initBannerView(Banner banner, List imagePath, List imageTitle){
        //设置图片加载器
        GlideImageLoader myImageLoader=new GlideImageLoader();
        banner.setImageLoader(myImageLoader);
        //设置图片集合
        banner.setImages(imagePath);
        // 设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(imageTitle);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3600);
        banner.start();
    }

    /**
     * 保存登陆信息到本地
     * @param map，登陆信息
     * @param context
     */

    public static void saveAccount(HashMap map, Context context){
        //保存账号到本地的shared文件中
        Tools.saveInfo2Preferences(context,map);
        //获取全局的登陆信息
        ElearnApplication.getLoginInfo(Tools.getSharedPre(context));
    }

    /**
     * 输入是否是正确的电话号码校验
     */
    public static boolean isMobileNO(String mobiles) {
        /*
         * 移动:139、138、137、136、135、134、159、158、157、150、151、152、147（数据卡）、188、187、182、183、184、178
         * 联通:130、131、132、156、155、186、185、145（数据卡）、176
         * 电信:133、153、189、180、181、177、173（待放）
         * 总结起来就是第一位必定为1,第二位必定为3或5或8,其他位置的可以为0-9
         */
        //正则表达式 "[1]"代表第1位为数字1,"[358]"代表第二位可以为3、5、8中的一个,"//d{9}"代表后面是可以是0~9的数字,有9位。
        String telRegex = "^1[3|5|7|8][0-9]\\d{8}$";
        //String telRegex = getString(R.string.telRegex);
        return (!TextUtils.isEmpty(mobiles))&&mobiles.matches(telRegex);
    }

    /**
     * 通过上下文获取activity
     * @param context
     * @return
     */
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    /**
     * 页面跳转，传递字符串
     * @param context 上下文
     * @param T       目标activity
     * @param finish  结束当前activity，为空不结束
     * @param Extra   传参，为空不传参
     */
    public static void toOtherPage(Context context, Class T, boolean finish,@Nullable JSONObject Extra) {
        Activity activity=Tools.findActivity( context );
        Intent intent = new Intent( activity, T );
        if( Extra!=null){
            for (Map.Entry<String, Object> entry : Extra.entrySet()) {
                intent.putExtra( entry.getKey(),String.valueOf( entry.getValue()));
            }
        }
        activity.startActivity( intent );
        if(finish) activity.finish();
    }

    public static void go(Context context, Class T, boolean finish, Params params) {
        Activity activity=Tools.findActivity( context );
        Intent intent = new Intent( activity, T );
        if (null != params && !params.isEmpty()) {
            for (Map.Entry<String, Serializable> entry : params.entrySet()) {
                intent.putExtra( entry.getKey(), entry.getValue() );
            }
        }
        activity.startActivity( intent );
        if(finish) activity.finish();
    }

    /**
     * 初始化wheel的样式
     */
    public static void initWheelStyle(WheelView wheelView, Context context){
        wheelView.setWheelAdapter( new ArrayWheelAdapter( context ) ); // 文本数据源
        wheelView.setSkin( WheelView.Skin.Holo ); // common皮肤
        WheelView.WheelViewStyle style = wheelView.getStyle();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style.holoBorderColor = context.getColor( R.color.btBackground );
        }
        wheelView.setStyle( style );
        wheelView.setLoop( true );
    }

    /**
     * 全局唯一ID生成
     */
    public static String createID(){
      return    String.valueOf(SnowFlakeUtil.getFlowIdInstance().nextId());
    }

}
