package com.stochitacatalin.mainactivity.Games.MineSweeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.stochitacatalin.mainactivity.R;

public class CellsAdapter extends BaseAdapter {

    private Context context;
    private Cell[] cells;
    private LayoutInflater inflater;
    public CellsAdapter(Context context, Cell[] cells) {
        this.context = context;
        this.cells = cells;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return cells.length;
    }

    @Override
    public Object getItem(int position) {
        return cells[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = inflater.inflate(R.layout.cell,parent,false);

        }

        return convertView;
    }
}
