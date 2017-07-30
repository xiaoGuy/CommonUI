package com.exmple.xiaoguy.demo;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by hua on 2017/7/30.
 */

public class FilterAdapter<T> extends BaseAdapter implements Filterable {

    private List<T> mData = new ArrayList<>();
    private List<T> mOriginalValues;

    public FilterAdapter() {
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(T data) {
        if (mOriginalValues != null) {
            mOriginalValues.add(data);
        }
        mData.add(data);
        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> collection) {
        if (mOriginalValues != null) {
            mOriginalValues.addAll(collection);
        }
        mData.addAll(collection);
        notifyDataSetChanged();
    }

    public void remove(T data) {
        if (mOriginalValues != null) {
            mOriginalValues.remove(data);
        }
        mData.remove(data);
        notifyDataSetChanged();
    }

    public void clear() {
        if (mOriginalValues != null) {
            mOriginalValues.clear();
        }
        mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tv;
        if (convertView == null) {
            tv = (TextView) LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        } else {
            tv = (TextView) convertView;
        }
        tv.setText(mData.get(position).toString());
        return tv;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (!TextUtils.isEmpty(constraint)) {
                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(mData);
                    }
                    List<T> list = new ArrayList<>();
                    for (T value : mOriginalValues) {
                        if (value.toString().contains(constraint)) {
                            list.add(value);
                        }
                    }
                    results.count = list.size();
                    results.values = list;
                } else {
                    results.count = 0;
                    results.values = new ArrayList<String>();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count > 0) {
                    mData = (List<T>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
    }
}
