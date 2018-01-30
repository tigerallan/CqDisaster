package com.nandi.cqdisaster.securitydao.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nandi.cqdisaster.R;
import com.nandi.cqdisaster.securitydao.bean.AreaName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by qingsong on 2018/1/9.
 */

public class AreaAdapter extends BaseAdapter {
    List<AreaName> datas = new ArrayList<>();
    Context mContext;

    public AreaAdapter(List<AreaName> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas == null ? null : datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_area, parent,false);
            hodler.mTextView = (TextView) convertView;
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.mTextView.setText(datas.get(position).getArea_name());

        return convertView;
    }
    private static class ViewHodler{
        TextView mTextView;
    }
}
