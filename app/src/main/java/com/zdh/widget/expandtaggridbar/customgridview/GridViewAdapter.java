package com.zdh.widget.expandtaggridbar.customgridview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TableRow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zdh on 16/3/26.
 */
public abstract class GridViewAdapter extends BaseAdapter {
    public abstract int getColumnCount();

    public abstract int getItemCount();

    public abstract View getItemView(int position, View convertView, ViewGroup parent);

    public abstract Object getItemValue(int position);

    private int getTrueColumnCount() {
        int count = getItemCount();
        if (getColumnCount() > 0) {
            count = getColumnCount();
        }
        return count;
    }

    @Override
    public final int getCount() {
        if (getColumnCount() <= 0) {
            return 1;
        }

        return (getItemCount() % getColumnCount() == 0) ?
                getItemCount() / getColumnCount() :
                getItemCount() / getColumnCount() + 1;
    }

    @Override
    public final long getItemId(int position) {
        return position;
    }

    @Override
    public final Object getItem(int position) {
        int count = getTrueColumnCount();
        List<Object> items = new ArrayList<>();
        int start = count * position;
        int end = start + count >= getItemCount() ? getItemCount() - 1 : start + count;
        for (int k = start; k <= end; k++) {
            items.add(getItemValue(k));
        }
        return items;
    }

    @Override
    public final View getView(int position, View convertView, ViewGroup parent) {
        TableRow tableRow;
        if (convertView == null) {
            tableRow = new TableRow(parent.getContext());
        } else {
            tableRow = (TableRow) convertView;
        }
        tableRow.removeAllViews();

        int count = getTrueColumnCount();
        int start = count * position;
        int end = start + count > getItemCount() ? getItemCount() : start + count;

        for (int k = start; k < end; k++) {
            //不提供复用的能力
            View itemView = getItemView(k, null, tableRow);
            tableRow.addView(itemView);
        }
        return tableRow;
    }
}
