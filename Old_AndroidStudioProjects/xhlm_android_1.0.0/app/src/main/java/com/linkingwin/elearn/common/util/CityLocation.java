package com.linkingwin.elearn.common.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.linkingwin.elearn.common.widget.BaseActivity;

import java.util.List;
import java.util.Locale;

public class CityLocation {
    private LocationManager lm;
    Context context;

    /**
     * 构造方法
     */
    public CityLocation(BaseActivity activity) {
        context=activity.getContext();
        lm = (LocationManager) context.getSystemService( Context.LOCATION_SERVICE );
    }

    /**
     * 判断GPS是否正常启动
     */

    public boolean isProviderEnabled (){
        return !lm.isProviderEnabled( LocationManager.GPS_PROVIDER );
    }

    /**
     * 判断应用是否有权限访问GPS(启动不一定有访问权限)
     */

    public boolean isProviderAccess(){
        return ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission( context, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED;
    }

    /**
     * 返回Location
     * 在使用之前需要对是否有权限访问GPS(isProviderAccess)进行判断，否则在没有权限时会抛出异常
     */
    public Location getLocation(){
        String bestProvider = lm.getBestProvider( getCriteria(), true );
        @SuppressLint("MissingPermission") Location location = lm.getLastKnownLocation( bestProvider );
        return location;
    }


    /**
     * 设置返回GPS的查询条件
     *
     * @return
     */
    private Criteria getCriteria() {
        Criteria criteria = new Criteria();
        //设置定位精确度 Criteria.ACCURACY_COARSE比较粗略，Criteria.ACCURACY_FINE则比较精细
//        criteria.setAccuracy( Criteria.ACCURACY_FINE );
        criteria.setAccuracy( Criteria.ACCURACY_COARSE );
        //设置是否要求速度
        criteria.setSpeedRequired( false );
        // 设置是否允许运营商收费
        criteria.setCostAllowed( false );
        //设置是否需要方位信息
        criteria.setBearingRequired( false );
        //设置是否需要海拔信息
        criteria.setAltitudeRequired( false );
        // 设置对电源的需求
        criteria.setPowerRequirement( Criteria.POWER_LOW );
        return criteria;
    }

}
