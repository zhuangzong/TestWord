package com.zz.testword;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by kang on 2018/4/17.
 */

public class MainAdapter extends BaseAdapter {
    List<String> list;
    Context context;
    ViewHolder holder;
    public MainAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_main, null);
            holder=new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder= (ViewHolder) convertView.getTag();
        }
        holder.tvItemMain.setText(Html.fromHtml(list.get(position)));
        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.tv_item_main)
        TextView tvItemMain;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
