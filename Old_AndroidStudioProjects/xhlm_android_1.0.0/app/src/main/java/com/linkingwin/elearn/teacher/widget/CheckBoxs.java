package com.linkingwin.elearn.teacher.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton;

import com.alibaba.fastjson.JSONObject;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.Tools;

public class CheckBoxs extends CompoundButton implements CompoundButton.OnCheckedChangeListener {
    JSONObject checkedBox;
    String id="";
    String text = "";
    JSONObject jsonSubject;
    public CheckBoxs(Context context) {
        super( context );
    }

    public void setTextID(String text,String id){
        this.text=text;
        this.id=id;
    }

    public void setCheckedBox(JSONObject checkedBox){
        this.checkedBox=checkedBox;
    }

    public void setJsonSubject(JSONObject jsonSubject){
        this.jsonSubject=jsonSubject;
    }

    public void initStyle(){
        int boxid = Tools.buildViewId();
        this.setId( boxid );
        this.setText( text );
        this.setButtonDrawable( null );
        Drawable cbBackground = getContext().getResources().getDrawable( R.drawable.btn_checkbox_selector );
        this.setBackground( cbBackground );
        this.setChecked( false );
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT );
        int dp5 = Tools.dip2px( getContext(), 5L );
        params.setMargins( dp5, dp5, dp5, dp5 );
        this.setPadding( dp5, dp5, dp5, dp5 );
        this.setLayoutParams( params );
        this.setChecked( checkedBox.containsKey( id ) );
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        //通过枚举值的ID找到对应枚举项
        String strText = jsonSubject.getString( id );
        if (isChecked) {
            //在对应的对象中增加
            checkedBox.put( id,strText );
        } else {
            //在对应的对象中删除
            checkedBox.remove( id );
        }
    }
}
