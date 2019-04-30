package com.stochitacatalin.mainactivity.Games.MineSweeper;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.view.MotionEvent;
import android.view.View;

public class GameStatusView extends View {
    Paint paint;
    String status;
    public GameStatusView(Context context) {
        super(context);
        paint = new Paint();
        paint.setTypeface(Typeface.SERIF);
        this.status = "";
    }

    public void setText(String text){
        status = text;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        drawRectWithStroke(canvas,0,0,getWidth(),getHeight());

        drawText(canvas,status,0,0,getWidth(), (int) (getHeight()/2f));

        drawRectWithStroke(canvas, (int) (getWidth()*0.1f), (int) (getHeight()*0.6f), (int) (getWidth()*0.9f), (int) (getHeight()*0.9f));

        drawText(canvas,"PLAY AGAIN", (int) (getWidth()*0.1f),(int) (getHeight()*0.6f), (int) (getWidth()*0.9f), (int) (getHeight()*0.9f));
    }

    public void drawText(Canvas canvas,String text,int left,int top,int right,int bottom){
        paint.setTextSize(getWidth()/(text.length()+1f));
        paint.setStyle(Paint.Style.FILL);
        Rect bounds = new Rect();
        paint.getTextBounds(text, 0, text.length(), bounds);
        int height = bounds.height();
        int width = bounds.width();
        canvas.drawText(text,(right + left - width)/2f,(bottom + top + height)/2f,paint);
    }

    public void drawRectWithStroke(Canvas canvas,int left,int top,int right,int bottom){

        // fill
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.LTGRAY);
        paint.setAlpha(200);
        canvas.drawRect(left,top,right,bottom,paint);

        // stroke
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(getHeight()/50f);
        paint.setAlpha(200);
        canvas.drawRect(left,top,right,bottom,paint);
    }

    boolean onClick(int x,int y){
        Rect rect = new Rect((int) (getWidth()*0.1f), (int) (getHeight()*0.6f), (int) (getWidth()*0.9f), (int) (getHeight()*0.9f));
        if(rect.contains(x,y)){
            return true;
        }
        return false;
    }

}
