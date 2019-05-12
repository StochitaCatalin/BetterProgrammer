package com.stochitacatalin.betterprogrammer.Games.MineSweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.annotation.NonNull;

import com.stochitacatalin.betterprogrammer.R;

public class MineSweeperAdapter extends BaseAdapter {
    Cell[][] cells;
    int size;
    Context context;
    public MineSweeperAdapter(@NonNull Context context,Cell[][] cells,int size) {
        this.cells = cells;
        this.size = size;
        this.context = context;
    }

    @Override
    public int getCount() {
        return size*size;
    }

    @Override
    public Cell getItem(int position) {
        return cells[position/size][position%size];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = getItem(position);
        }
        return convertView;
    }
}
