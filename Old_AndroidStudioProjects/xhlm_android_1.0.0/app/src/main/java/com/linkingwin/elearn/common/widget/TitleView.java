package com.linkingwin.elearn.common.widget;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.linkingwin.elearn.R;
import com.linkingwin.elearn.common.util.Tools;

import lombok.Data;


public class TitleView extends FrameLayout {
    private TextView titleText;
    private TextView subTitleText;
    private ImageView back;
    private TextView line;
    private Context context;
    private LinearLayout linearLayout;
    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        LayoutInflater.from(context).inflate(R.layout.title, this);
        titleText = (TextView) findViewById(R.id.title_text);
        subTitleText=(TextView)  findViewById(R.id.sub_title_text);
        back = (ImageView) findViewById(R.id.back);
        line=(TextView)findViewById(R.id.title_line);
        linearLayout=findViewById( R.id.rl_all_title );
        back.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity) getContext()).finish();
            }
        });
    }

    /**
     * 设置副标题，没有监听
     */
    public TitleView setSubTitleNoListener(String name, Integer color){
        subTitleText.setVisibility( VISIBLE );
        subTitleText.setText(name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            subTitleText.setTextColor( Tools.findActivity(context).getColor( color));
        }else{
            subTitleText.setTextColor( ContextCompat.getColor( context, color ));
        }
        return this;
    }

    /**
     * 设置副标题
     */

    public TitleView setSubTitle(String name, Integer color, View.OnClickListener listener){
        subTitleText.setVisibility( VISIBLE );
        subTitleText.setText(name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            subTitleText.setTextColor( Tools.findActivity(context).getColor( color));
        }else{
            subTitleText.setTextColor( ContextCompat.getColor( context, color ));
        }
        subTitleText.setOnClickListener(listener);
        return this;
    }

    /**
     * 设置title布局的背景
     */
    public TitleView setBackGround(@Nullable Integer resID){
        if( resID==null){
            linearLayout.setBackground( null );
        }else{
            linearLayout.setBackgroundResource( resID );
        }
        return this;
    }
    /**
     * 设置标题
     * @param text
     * @param color
     */
    public TitleView setTitleText(String text, Integer color) {
        titleText.setText(text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            titleText.setTextColor( Tools.findActivity(context).getColor( color));
        }else{
            titleText.setTextColor( ContextCompat.getColor( context, color ));
        }
        return this;
    }
    /**
     * 设置背景颜色
     */
    public TitleView setBackColor(Integer color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            titleText.setBackgroundColor( Tools.findActivity(context).getColor( color));
        }else{
            titleText.setBackgroundColor( ContextCompat.getColor( context, color ));
        }
        return this;
    }
    /**
     * 设置图标类型;
     * @param srcID
     */
    public TitleView setIconStyle(Integer srcID){
        back.setImageResource(srcID);
        return this;
    }
    /**
     * 设置title的下划线
     */
    public TitleView setLineStyle(int visibility,int color){
        line.setVisibility(visibility);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            line.setBackgroundColor( Tools.findActivity(context).getColor( color));
        }else{
            line.setBackgroundColor( ContextCompat.getColor( context, color ));
        }
        return this;
    }
    /**
     * 隐藏返回按钮
     */
    public TitleView hideBackIcon(){
        back.setVisibility(View.GONE);
        return this;
    }
}
