package com.linkingwin.elearn.common.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.linkingwin.elearn.teacher.model.HonorVO;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by smyhvae on 2015/5/4.
 * 通用的ListView的BaseAdapter，所有的ListView的自定义adapter都可以继承这个类哦
 */
public abstract class ListViewAdapter<T> extends BaseAdapter {

    //为了让子类访问，于是将属性设置为protected
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    private int layoutId; //不同的ListView的item布局肯能不同，所以要把布局单独提取出来

    public ListViewAdapter(Context context, List<T> datas, int layoutId) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    //获取所有的List
    public List<T> getAllItem(){
        return this.mDatas;
    }

    //获取listview的数量
    @Override
    public int getCount() {
        return mDatas.size();
    }

    //获取对应位置的对象
    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //初始化ViewHolder,使用通用的ViewHolder，一行代码就搞定ViewHolder的初始化咯
        ViewHolder holder = ViewHolder.bind(mContext, convertView, parent, layoutId, position);//layoutId就是单个item的布局
        bindView(holder, getItem(position));
        return holder.getItemView(); //这一行的代码要注意了
    }

    //添加一个元素
    public void add(T data) {
        if (mDatas == null) {
            mDatas = new LinkedList<>();
        }
        mDatas.add(data);
        notifyDataSetChanged();
    }

    //往特定位置，添加一个元素
    public void add(int position,T data){
        if (mDatas == null) {
            mDatas = new LinkedList<>();
        }
        mDatas.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if(mDatas != null) {
            mDatas.remove(data);
        }
        notifyDataSetChanged();
    }

    public void remove(int position) {
        if(mDatas != null) {
            mDatas.remove(position);
        }
        notifyDataSetChanged();
    }

    public void clear() {
        if(mDatas != null) {
            mDatas.clear();
        }
        notifyDataSetChanged();
    }

    public void notifyUpdate() {
        notifyDataSetChanged();
    }

    //将bindView方法公布出去
    public abstract void bindView(ViewHolder holder, T t);
}
