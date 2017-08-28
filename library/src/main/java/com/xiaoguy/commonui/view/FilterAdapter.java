package com.xiaoguy.commonui.view;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by Xiaoguy on 2017/7/30.
 */

public abstract class FilterAdapter<T> extends BaseAdapter implements Filterable {

    private List<T> mData;
    private List<T> mOriginalValues;

    public FilterAdapter() {
        mData = new ArrayList<>();
    }

    public FilterAdapter(@NonNull List<T> data) {
        mData = data;
    }

    public FilterAdapter(@NonNull T[] objects) {
        this(Arrays.asList(objects));
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
    public final Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();

                if (!TextUtils.isEmpty(constraint)) {
                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<>(mData);
                    }

                    List<T> list = FilterAdapter.this.performFiltering(constraint, Collections.unmodifiableList(mOriginalValues));
                    if (list != null && !list.isEmpty()) {
                        results.count = list.size();
                        results.values = list;
                    } else {
                        results.count = 0;
                        results.values = Collections.emptyList();
                    }

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

    /**
     * @param input 当前输入框中的内容
     * @param values Adapter 中的数据集，该数据集已经被包装为不可修改的集合
     * @return 被过滤后的结果
     */
    public abstract List<T> performFiltering(CharSequence input, List<T> values);
}
