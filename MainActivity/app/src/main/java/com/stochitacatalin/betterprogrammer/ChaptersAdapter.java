package com.stochitacatalin.betterprogrammer;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class ChaptersAdapter extends ArrayAdapter<ChapterItem> {
    ChaptersAdapter(@NonNull Context context, ArrayList<ChapterItem> chapters) {
        super(context, 0, chapters);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ChapterItem item = getItem(position);
        assert item != null;

        if(convertView == null){
            convertView = View.inflate(getContext(),R.layout.item_chapter,null);
        }

      //  ((ImageView)convertView.findViewById(R.id.imageView)).setImageDrawable(getDrawableByName(item.icon));
        ((TextView)convertView.findViewById(R.id.nameView)).setText(item.name);
        ((ProgressBar)convertView.findViewById(R.id.progressStages)).setMax(item.sections);
        ((ProgressBar)convertView.findViewById(R.id.progressStages)).setProgress(item.completed);

        return convertView;
    }
}
