package com.stochitacatalin.mainactivity.Games.MineSweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;

public class MineCell extends Cell {

    public MineCell(Context context) {
        super(context,CeilType.mine);
        visibleColor = Color.RED;
    }

    public MineCell(Context context, AttributeSet attributeSet){
        super(context,attributeSet,CeilType.mine);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(visible){
            paint.setColor(Color.RED);
            canvas.drawRect(getWidth()*0.2f,getHeight()*0.2f,getWidth()*0.8f,getHeight()*0.8f,paint);
        }
    }
}
