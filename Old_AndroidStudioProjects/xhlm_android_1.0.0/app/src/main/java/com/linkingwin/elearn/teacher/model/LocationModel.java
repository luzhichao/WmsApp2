package com.linkingwin.elearn.teacher.model;

import com.alibaba.fastjson.JSONObject;
import com.zaaach.citypicker.model.LocatedCity;

public class LocationModel {
    JSONObject location;
    String lng;
    String lat;
    String formatted_address;
    String business;
    JSONObject addressComponent;
    String city;
    String district;
    String province;
    String street;
    String cityCod;
    LocatedCity locatedCity;


    /*
     *百度map_api的返回解析
     */
    public LocationModel resloveBaiduMap( JSONObject result){
        String formatted_address= (String) result.get("formatted_address");
        JSONObject addressComponent=result.getJSONObject("addressComponent");
        city=(String)addressComponent.get( "city" );
        district=(String)addressComponent.get( "district" );
        province=(String)addressComponent.get( "province" );
        street=(String)addressComponent.get( "street" );
        cityCod=Integer.toString((Integer) result.get("cityCode"));
        return this;
    }

    public String getCity() {
        return city;
    }

    public String getCityCod() {
        return cityCod;
    }

    public String getProvince() {
        return province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCityCod(String cityCod) {
        this.cityCod = cityCod;
    }
}
