package com.stochitacatalin.betterprogrammer;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class TopicsAdapter extends ArrayAdapter<TopicItem> {
    TopicsAdapter(@NonNull Context context, ArrayList<TopicItem> topics) {
        super(context, 0, topics);
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TopicItem item = getItem(position);
        assert item != null;

        if(convertView == null){
            convertView = View.inflate(getContext(),R.layout.item_topic,null);
        }

        ((ImageView)convertView.findViewById(R.id.imageView)).setImageDrawable(getDrawableByName(item.icon));
        ((TextView)convertView.findViewById(R.id.nameView)).setText(item.name);
        ((ProgressBar)convertView.findViewById(R.id.progressStages)).setMax(item.chapters);
        ((ProgressBar)convertView.findViewById(R.id.progressStages)).setProgress(item.completed);

        return convertView;
    }

    private Drawable getDrawableByName(String name){
        Resources resources = getContext().getResources();
        final int resourceId = resources.getIdentifier(name, "drawable",
                getContext().getPackageName());
        return resources.getDrawable(resourceId);
    }

}
