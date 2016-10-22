package com.example.lovej.secretkeeper;

/**
 * Created by lovej on 2016/10/3 0003.
 * Description: This class is for create and control the items in game 2048
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class Game2048Item extends View {

    private Paint paint;
    private int number;
    private Rect textRect;
    private String numberValue;

    /**
     * @param context The context
     * @description: <This is constructor for Game2048Item>
     */
    public Game2048Item(Context context) {
        this(context, null);
    }

    /**
     * @param context The context context
     * @param attrs   The attrs AttributeSet
     * @description: <This is constructor for Game2048Item>
     */
    public Game2048Item(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context The username Context
     * @param attrs The password AttributeSet
     * @param defStyleAttr The defStyleAttr Int
     * @description: <This is constructor for Game2048Item>
     */
    public Game2048Item(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint = new Paint();
    }

    /**
     * @param number The number
     * @description: <This function is to set the number to display in a square>
     */
    public void setNumber(int number) {
        this.number = number;
        numberValue = "" + number;
        paint.setTextSize(50);
        textRect = new Rect();
        paint.getTextBounds(numberValue, 0, numberValue.length(), textRect);
        invalidate();
    }

    /**@return the Int number
     * @description: <This function is to return the number>
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param canvas The Canvas
     * @description: <This function is to set different color according to the different number>
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        String backColor;
        switch (number) {
            case 0:
                backColor = "#CCC0B3";
                break;
            case 2:
                backColor = "#EEE4DA";
                break;
            case 4:
                backColor = "#EDE0C8";
                break;
            case 8:
                backColor = "#F2B179";
                break;
            case 16:
                backColor = "#F49563";
                break;
            case 32:
                backColor = "#F5794D";
                break;
            case 64:
                backColor = "#F55D37";
                break;
            case 128:
                backColor = "#EEE863";
                break;
            case 256:
                backColor = "#EDB04D";
                break;
            case 512:
                backColor = "#ECB04D";
                break;
            case 1024:
                backColor = "#EB9437";
                break;
            case 2048:
                backColor = "#EA7821";
                break;
            default:
                backColor = "#EA7821";
                break;
        }

        paint.setColor(Color.parseColor(backColor));
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint);

        if (number != 0) {
            paint.setColor(Color.BLACK);
            float x = (getMeasuredWidth()-textRect.width())/2;
            float y = getMeasuredHeight()/2 + textRect.width()/2;
            canvas.drawText(numberValue, x, y, paint);
        }
    }
}