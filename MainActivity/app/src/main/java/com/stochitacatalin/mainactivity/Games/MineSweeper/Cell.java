package com.stochitacatalin.mainactivity.Games.MineSweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;


public class Cell extends View {
    public boolean visible = false;
    boolean marked = false;
    Paint paint;
    public CeilType type;
    int visibleColor;
    public int mines = 0;
    public Cell(Context context){
        super(context);
    }
    public Cell(Context context,CeilType type){
        this(context);
        init(type);
    }

    public Cell(Context context, AttributeSet attributeSet,CeilType type){
        super(context,attributeSet);
        init(type);
    }
    public void init(CeilType type){
        this.type = type;
        paint = new Paint();
        paint.setTypeface(Typeface.SERIF);
    }

    @Override
    public void onDraw(Canvas canvas) {
        if(visible) {
            paint.setColor(Color.LTGRAY);
        }
        else{
            paint.setColor(Color.DKGRAY);
        }
        canvas.drawRect(getWidth()*0.05f,getHeight()*0.05f,getWidth()*0.95f,getHeight()*0.95f,paint);
        if(marked){
            paint.setColor(Color.LTGRAY);
            canvas.drawRect(getWidth()*0.2f,getHeight()*0.2f,getWidth()*0.8f,getHeight()*0.8f,paint);
        }
    }

    public boolean onLongClick(View v) {
        if(!visible)
            marked = !marked;
        return true;
    }

    public boolean onClick(View v) {

        if (!marked && !visible) {
            visible = true;
            return true;
        }
        return false;
    }


    public enum CeilType{mine,empty}
}
