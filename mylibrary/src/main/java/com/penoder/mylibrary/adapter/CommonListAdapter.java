package com.penoder.mylibrary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * @author Penoder
 * @date 2017/11/16
 */
public abstract class CommonListAdapter<T> extends BaseAdapter {

    private List<T> datas;
    private int layoutId;

    public CommonListAdapter(List<T> datas, int layoutId) {
        this.datas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonListAdapter.ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (CommonListAdapter.ViewHolder) convertView.getTag();
        }


        onConvertView(datas.get(position), holder, position);

        return convertView;
    }

    protected abstract void onConvertView(T t, CommonListAdapter.ViewHolder holder, int position);

    public class ViewHolder {

        private View itemView;

        private ViewHolder(View itemView) {
            this.itemView = itemView;
        }

        public View getItemView() {
            return itemView;
        }

        public <V extends View> V getView(int viewId) {
            return itemView.findViewById(viewId);
        }
    }
}