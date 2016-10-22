package com.example.lovej.secretkeeper;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by lovej on 2016/10/3 0003.
 * Description: This class is for create and control the GridLayout in game 2048
 */

public class Game2048GridLayout extends GridLayout {

    private int childWidth;
    private int padding;
    private int childRow = 4;
    private int margin = 10;
    private boolean isLayout = false;
    private Game2048Item[][] game2048Items = null;
    private GestureDetector gestureDetector;
    private boolean isMoveHappen = false;
    private boolean isMergeHappen = false;
    private boolean isFirst = true;
    private int score;
    private OnGame2048Listener onGame2048Listener;

    private enum Action {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * @param context The context
     * @description: <This is constructor for Game2048GridLayout>
     */
    public Game2048GridLayout(Context context) {
        this(context, null);
    }

    /**
     * @param context The context context
     * @param attrs   The attrs AttributeSet
     * @description: <This is constructor for Game2048GridLayout>
     */
    public Game2048GridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * @param context The username Context
     * @param attrs The password AttributeSet
     * @param defStyleAttr The defStyleAttr Int
     * @description: <This is constructor for Game2048GridLayout>
     */
    public Game2048GridLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        padding = Math.min(getPaddingBottom(), getPaddingTop());
        gestureDetector = new GestureDetector(context, new MyGestureListener());
        score = 0;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int length = Math.min(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.getSize(heightMeasureSpec));
        childWidth = (length - 2 * padding - (childRow - 1) * margin) / childRow;
        setMeasuredDimension(length, length);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (!isLayout) {
            if (game2048Items == null) {
                game2048Items = new Game2048Item[childRow][childRow];
            }

            for (int i = 0; i < childRow; i++) {
                for (int j = 0; j < childRow; j++) {
                    Game2048Item child = new Game2048Item(getContext());
                    game2048Items[i][j] = child;

                    Spec row = GridLayout.spec(i);
                    Spec column = GridLayout.spec(j);
                    GridLayout.LayoutParams lp = new LayoutParams(row, column);
                    lp.width = childWidth;
                    lp.height = childWidth;
                    if ((j + 1) != childRow) {
                        lp.rightMargin = margin;
                    }
                    if (i > 0) {
                        lp.topMargin = margin;
                    }
                    lp.setGravity(Gravity.FILL);
                    addView(child, lp);
                }
            }

            generateNum();
        }
        isLayout = true;
    }

    /**
     * @description: <This function is to generate random number based on game rules>
     */
    private void generateNum() {
        if (isGameOver()) {
            onGame2048Listener.onGameOver();
            return;
        }

        if (isFirst) {
            for (int i = 0; i < 4; i++) {
                int randomRow = new Random().nextInt(childRow);
                int randomCol = new Random().nextInt(childRow);
                Game2048Item item = game2048Items[randomRow][randomCol];
                while (item.getNumber() != 0) {
                    randomRow = new Random().nextInt(childRow);
                    randomCol = new Random().nextInt(childRow);
                    item = game2048Items[randomRow][randomCol];
                }
                item.setNumber(Math.random() > 0.75 ? 4 : 2);
                Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                item.startAnimation(scaleAnimation);
                isMoveHappen = isMergeHappen = false;
            }
            isFirst = false;
        }

        if (!isFull()) {
            if (isMoveHappen || isMergeHappen) {
                int randomRow = new Random().nextInt(childRow);
                int randomCol = new Random().nextInt(childRow);
                Game2048Item item = game2048Items[randomRow][randomCol];
                while (item.getNumber() != 0) {
                    randomRow = new Random().nextInt(childRow);
                    randomCol = new Random().nextInt(childRow);
                    item = game2048Items[randomRow][randomCol];
                }
                item.setNumber(Math.random() > 0.75 ? 4 : 2);
                Animation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                scaleAnimation.setDuration(200);
                item.startAnimation(scaleAnimation);
                isMoveHappen = isMergeHappen = false;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        final int MIN_DISTANCE = 50;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            float absX = Math.abs(x);
            float absY = Math.abs(y);

            if (x > MIN_DISTANCE && absX > absY) {
                action(Action.RIGHT);
            }
            if (x < -MIN_DISTANCE && absX > absY) {
                action(Action.LEFT);
            }
            if (y > MIN_DISTANCE && absY > absX) {
                action(Action.DOWN);
            }
            if (y < -MIN_DISTANCE && absY > absX) {
                action(Action.UP);
            }
            return true;
        }
    }

    private void action(Action action) {
        for (int i = 0; i < childRow; i++) {
            List<Game2048Item> rowList = new ArrayList<>();
            for (int j = 0; j < childRow; j++) {
                int rowIndex = getRowIndexByAction(action, i, j);
                int colIndex = getColIndexByAction(action, i, j);
                Game2048Item item = game2048Items[rowIndex][colIndex];
                if (item.getNumber() != 0) {
                    rowList.add(item);
                }
            }
            for (int j = 0; j < rowList.size(); j++) {
                int rowIndex = getRowIndexByAction(action, i, j);
                int colIndex = getColIndexByAction(action, i, j);
                Game2048Item item = game2048Items[rowIndex][colIndex];
                if (item.getNumber() != rowList.get(j).getNumber()) {
                    isMoveHappen = true;
                }
            }

            mergeItem(rowList);

            for (int j = 0; j < childRow; j++) {

                if (rowList.size() > j) {
                    switch (action) {
                        case LEFT :
                            game2048Items[i][j].setNumber(rowList.get(j).getNumber());
                            break;
                        case RIGHT:
                            game2048Items[i][childRow - 1 - j].setNumber(rowList.get(j).getNumber());
                            break;
                        case UP:
                            game2048Items[j][i].setNumber(rowList.get(j).getNumber());
                            break;
                        case DOWN:
                            game2048Items[childRow - 1 - j][i].setNumber(rowList.get(j).getNumber());
                            break;
                    }
                } else {
                    switch (action) {
                        case LEFT :
                            game2048Items[i][j].setNumber(0);
                            break;
                        case RIGHT:
                            game2048Items[i][childRow - 1 - j].setNumber(0);
                            break;
                        case UP:
                            game2048Items[j][i].setNumber(0);
                            break;
                        case DOWN:
                            game2048Items[childRow - 1 - j][i].setNumber(0);
                            break;
                    }
                }
            }
        }
        generateNum();
    }

    private int getRowIndexByAction(Action action, int i, int j) {
        int rowIndex = -1;

        switch (action) {
            case LEFT:
            case RIGHT:
                rowIndex = i;
                break;
            case UP:
                rowIndex = j;
                break;
            case DOWN:
                rowIndex = childRow - 1 - j;
                break;
        }
        return rowIndex;

    }

    private int getColIndexByAction(Action action, int i, int j) {
        int colIndex = -1;
        switch (action) {
            case LEFT:
                colIndex = j;
                break;
            case RIGHT:
                colIndex = childRow - 1 - j;
                break;
            case UP:
            case DOWN:
                colIndex = i;
                break;
        }
        return colIndex;
    }

    /**
     * @param rowList The List<Game2048Item>
     * @description: <This function is to merge the same number to one number>
     */
    private void mergeItem(List<Game2048Item> rowList) {
        if (rowList.size() < 2) {
            return;
        }
        for (int i = 0; i < rowList.size() - 1; i++) {
            Game2048Item item1 = rowList.get(i);
            Game2048Item item2 = rowList.get(i + 1);
            if (item1.getNumber() == item2.getNumber()) {
                isMergeHappen = true;
                score += item1.getNumber() * 2;
                item1.setNumber(item1.getNumber() * 2);
                onGame2048Listener.onScoreChange(score);
                for (int j = i + 1; j < rowList.size() - 1; j++) {

                    rowList.get(j).setNumber(rowList.get(j + 1).getNumber());
                }

                rowList.get(rowList.size() - 1).setNumber(0);
                return;
            }
        }
    }

    /**
     * @description: <This function is to detect is there any possible that game can still continue to play>
     */
    private boolean isGameOver() {

        if (!isFull()) {
            return false;
        }

        for (int i = 0; i < childRow; i++) {
            for (int j = 0; j < childRow; j++) {
                Game2048Item item = game2048Items[i][j];

                if ((j + 1) != childRow) {
                    Game2048Item itemRight = game2048Items[i][j + 1];
                    if (item.getNumber() == itemRight.getNumber())
                        return false;
                }

                if ((i + 1)  != childRow) {
                    Log.e("TAG", "DOWN");
                    Game2048Item itemBottom = game2048Items[i + 1][j];
                    if (item.getNumber() == itemBottom.getNumber())
                        return false;
                }

                if (j != 0) {
                    Log.e("TAG", "LEFT");
                    Game2048Item itemLeft = game2048Items[i][j - 1];
                    if (itemLeft.getNumber() == item.getNumber())
                        return false;
                }

                if (i != 0) {
                    Log.e("TAG", "UP");
                    Game2048Item itemTop = game2048Items[i - 1][j];
                    if (item.getNumber() == itemTop.getNumber())
                        return false;
                }
            }
        }
        return true;
    }

    /**
     * @description: <This function is to detect is the layout been full>
     */
    public boolean isFull() {
        for (int i = 0; i < childRow; i++) {
            for (int j = 0; j < childRow; j++) {
                Game2048Item game2048Item = game2048Items[i][j];
                if (game2048Item.getNumber() == 0)
                    return false;
            }
        }
        return true;
    }


    public interface OnGame2048Listener {
        void onScoreChange(int score);

        void onGameOver();
    }

    public void setOnGame2048Listener(OnGame2048Listener onGame2048Listener) {
        this.onGame2048Listener = onGame2048Listener;
    }

    /**
     * @description: <This function is to allow user to start a new game>
     */
    public void reStart() {
        for (int i = 0; i < childRow; i++) {
            for (int j = 0; j < childRow; j++) {
                Game2048Item item = game2048Items[i][j];
                item.setNumber(0);
            }
        }
        score = 0;
        onGame2048Listener.onScoreChange(0);
        isFirst = true;
        generateNum();
    }

    /**
     * @return The score Int
     * @description: <This function is to merge the same number to one number>
     */
    public int getScore(){
        return score;
    }
}
