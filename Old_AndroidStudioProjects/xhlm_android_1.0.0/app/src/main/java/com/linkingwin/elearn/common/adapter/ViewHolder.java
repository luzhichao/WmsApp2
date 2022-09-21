package com.linkingwin.elearn.common.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;

/***
 * 我们先来看看前面我们的ViewHolder干了什么？
 * 答：findViewById，设置控件状态； 下面我们想在完成这个基础上，将getView()方法大部分的逻辑写到ViewHolder类里
 * 这个ViewHolder要做的事：
 * 定义一个查找控件的方法，我们的思路是通过暴露公共的方法，调用方法时传递过来 控件id，以及设置的内容.
 * 比如TextView设置文本： public ViewHolder setText(int id, CharSequence text){文本设置}
 * 将convertView复用部分搬到这里，那就需要传递一个context对象了，我们把需要获取 的部分都写到构造方法中！
 * 写一堆设置方法(public)，比如设置文字大小颜色，图片背景等！
 */
public class ViewHolder {
    private SparseArray<View> mViews;  //存储ListView 的 item的View
    private int mPosition;              //游标
    private View mConvertView;          //存放convertView
    private Context context;            //Context上下文

    //构造方法，完成相关初始化
    public ViewHolder(Context context, ViewGroup parent, int layoutResId, int position) {
        this.mPosition = position;
        this.mViews = new SparseArray<View>();
        this.context=context;
        mConvertView = LayoutInflater.from(context).inflate(layoutResId, parent, false);

        mConvertView.setTag(this);

    }



    //绑定ViewHolder与item
    public static ViewHolder bind(Context context, View convertView, ViewGroup parent, int layoutId, int position) {
        if (convertView == null) {
            return new ViewHolder(context, parent, layoutId, position);
        } else {
            ViewHolder holder = (ViewHolder) convertView.getTag();
            holder.mPosition = position; //即使ViewHolder是复用的，但是position记得更新一下
            return holder;
        }
    }

    /**
     *  根据viewId获取集合中保存的控件
     *  使用的是泛型T,返回的是View的子类
     */
    public <T extends View> T getView(int viewId) {
        View view = mViews.get(viewId);

        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;
    }

    /**
     * 获取当前条目
     */
    public View getItemView() {
        return mConvertView;
    }

    /**
     * 获取条目位置
     */
    public int getItemPosition() {
        return mPosition;
    }

    /**
     * 设置文字
     */
    public ViewHolder setText(int id, CharSequence text) {
        View view = getView(id);
        if(view instanceof TextView) {
            ((TextView) view).setText(text);
        }
        return this;
    }

    /**
     * 设置图片
     */
    public ViewHolder setImageResource(int id, int drawableRes) {
        View view = getView(id);
        if(view instanceof ImageView) {
            ((ImageView) view).setImageResource(drawableRes);
        } else {
            view.setBackgroundResource(drawableRes);
        }
        return this;
    }

    /**
     * 设置点击监听
     */
    public ViewHolder setOnClickListener(int id, View.OnClickListener listener) {
        getView(id).setOnClickListener(listener);
        return this;
    }

    /**
     * 设置可见
     */
    public ViewHolder setVisibility(int id, int visible) {
        getView(id).setVisibility(visible);
        return this;
    }

    /**
     * 设置标签
     */
    public ViewHolder setTag(int id, Object obj) {
        getView(id).setTag(obj);
        return this;
    }
}
