package com.stochitacatalin.betterprogrammer.Games.MineSweeper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;

@SuppressLint("ViewConstructor")
public class EmptyCell extends Cell {
    public EmptyCell(Context context,int mines) {
        super(context,CeilType.empty);
        this.mines = mines;
    }

    public EmptyCell(Context context, AttributeSet attributeSet){
        super(context,attributeSet);/*,CeilType.empty);*/
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(visible) {
            paint.setTextSize(getHeight()*0.5f);
            paint.setColor(Color.BLACK);
            Rect bounds = new Rect();
            paint.getTextBounds(String.valueOf(mines), 0, 1, bounds);
            int height = bounds.height();
            int width = bounds.width();
            canvas.drawText(String.valueOf(mines), (getWidth() - width)/2f, getHeight() - (getHeight() - height)/2f, paint);
        }
    }
}
