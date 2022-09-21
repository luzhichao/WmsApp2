package com.linkingwin.education.studyonline.common.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.linkingwin.education.studyonline.common.utils.BeanUtils;

import org.xutils.view.annotation.ContentView;

import java.util.LinkedList;
import java.util.List;

public abstract class BaseViewAdapter<T extends Object> extends BaseAdapter {

    //为了让子类访问，于是将属性设置为protected
    private Context context;
    private List<T> datas;
    private LayoutInflater mInflater;
    private Integer layoutId; //不同的ListView的item布局肯能不同，所以要把布局单独提取出来

    public BaseViewAdapter(Context context, List<T> dataList) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.datas = dataList;
    }

    //获取listview的数量
    @Override
    public int getCount() {
        return datas.size();
    }

    //获取对应位置的对象
    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初始化ViewHolder,使用通用的ViewHolder，一行代码就搞定ViewHolder的初始化咯
        ViewHolder holder = ViewHolder.bind(context, convertView, parent, getLayoutId(), position);//layoutId就是单个item的布局
        bindView(holder, getItem(position));
        return holder.getItemView(); //这一行的代码要注意了
    }

    //添加一个元素
    public void append(T data) {
        if (datas == null) {
            datas = new LinkedList<> ();
        }
        datas.add(data);
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void insert(int position,T data){
        if (datas == null) {
            datas = new LinkedList<>();
        }
        datas.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if(datas != null) {
            datas.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if(datas != null) {
            datas.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if(datas != null) {
            datas.clear();
        }
        notifyDataSetChanged();
    }

    public void notifyUpdate() {
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }
    public List<T> getDatas() {
        return datas;
    }
    public int getLayoutId() {
        if (null == layoutId) {
            ContentView contentView = BeanUtils.getAnnotation(getClass(), ContentView.class);
            if (null == contentView) {
                throw new RuntimeException("该Adapter未设置ContentView");
            }
            this.layoutId = contentView.value();
        }
        return layoutId;
    }

    //将bindView方法公布出去
    public abstract void bindView(ViewHolder holder, T t);
}