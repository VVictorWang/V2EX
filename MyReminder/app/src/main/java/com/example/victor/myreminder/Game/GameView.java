package com.example.victor.myreminder.Game;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 2017/3/17.
 */

public class GameView extends GridLayout{
    private int downX,downY;
    private Card[][] mCards=new Card[4][4];
    private List<Point> mPoints=new ArrayList<>();
    private int score;

    public GameView(Context context) {
        this(context,null);
    }

    public GameView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    private void initData() {
        setBackgroundColor(0xffcc9ace);
        setColumnCount(4);
    }
    public void initGame(){

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mCards[i][j].setNum(0);
            }
        }

        score=0;
        Alarm.getAlarm().setScore(0);
        addBroad();
        addBroad();
    }

    private void addBroad() {
        //寻找空白区域 记录其位置
        mPoints.clear();
        for (int i = 0; i <4 ; i++) {
            for (int j = 0; j < 4; j++) {
                if (mCards[i][j].getNum()<=0){
                    Point point=new Point(i,j);
                    mPoints.add(point);
                }
            }
        }

        getRandomBroad();
    }

    private void completeGame() {

        boolean isCompleted=true;
        flag:
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(mPoints.size()!=0||
                        i>0&&mCards[i][j].equals(mCards[i-1][j])||
                        i<3&&mCards[i][j].equals(mCards[i+1][j])||
                        j>0&&mCards[i][j].equals(mCards[i][j-1])||
                        j<3&&mCards[i][j].equals(mCards[i][j+1])){
                    isCompleted=false;
                    break flag;
                }
            }
        }
        if (isCompleted){
            AlertDialog.Builder builder = new AlertDialog.Builder(Alarm.getAlarm());
            builder.setTitle("游戏结束!!!");
            builder.setMessage("您的得分:"+score);
            builder.setCancelable(false);
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    initGame();
                }
            });
            builder.show();
        }
    }

    private void getRandomBroad() {
        if (mPoints.size()!=0){

            Point point = mPoints.remove((int) (Math.random() * mPoints.size()));
            mCards[point.x][point.y].setNum(Math.random()>0.2?2:4);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                downX= (int) event.getX();
                downY= (int) event.getY();
                break;
            case MotionEvent.ACTION_UP:
                int offsetX= (int) (event.getX()-downX);
                int offsetY= (int) (event.getY()-downY);

                if (Math.abs(offsetX)-Math.abs(offsetY)>=0){

                    if (offsetX>5){
                        moveRight();
                    }else if (offsetX<-5){
                        moveLeft();
                    }
                }else{
                    if (offsetY>5){

                        moveDown();
                    }else if(offsetY<-5){
                        moveUp();
                    }
                }
                break;
        }
        return true;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int cardWidth= Math.min(w,h)/4-10;
        initView(cardWidth,cardWidth);
        initGame();
    }

    private void initView(int cardWidth, int cardHeight) {

        Card card;
        for (int i = 0; i <4; i++) {
            for (int j = 0; j <4 ; j++) {
                card=new Card(getContext());
                card.setNum(0);
                addView(card,cardWidth,cardHeight);
                mCards[i][j]=card;
            }
        }
    }

    private void moveUp() {

        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                for (int k = i+1; k < 4 ; k++) {
                    if(mCards[i][j].getNum()==0&&mCards[k][j].getNum()>0){
                        mCards[i][j].setNum(mCards[k][j].getNum());
                        mCards[k][j].setNum(0);
                        i--;
                        break;
                    }else if (mCards[i][j].getNum()!=0&&mCards[i][j].equals(mCards[k][j])&&vMiddleIsZero(j,i,k)){
                        mCards[i][j].setNum(mCards[i][j].getNum()*2);
                        mCards[k][j].setNum(0);
                        score+=mCards[i][j].getNum();
                        Alarm.getAlarm().setScore(score);
                        break;
                    }
                }
            }
        }

        addBroad();
        completeGame();
    }

    private void moveDown() {
        for (int j = 0; j < 4; j++) {
            for (int i = 3; i >=0; i--) {
                for (int k = i-1; k >=0 ; k--) {
                    if(mCards[i][j].getNum()==0&&mCards[k][j].getNum()>0){
                        mCards[i][j].setNum(mCards[k][j].getNum());
                        mCards[k][j].setNum(0);
                        i++;
                        break;
                    }else if (mCards[i][j].getNum()!=0&&mCards[i][j].equals(mCards[k][j])&&vMiddleIsZero(j,i,k)){
                        mCards[i][j].setNum(mCards[i][j].getNum()*2);
                        mCards[k][j].setNum(0);
                        score+=mCards[i][j].getNum();
                        Alarm.getAlarm().setScore(score);
                        break;
                    }
                }
            }
        }
        addBroad();
        completeGame();
    }

    private void moveLeft() {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                for (int k = j+1; k <4 ; k++) {
                    if (mCards[i][j].getNum()==0&&mCards[i][k].getNum()>0){
                        mCards[i][j].setNum(mCards[i][k].getNum());
                        mCards[i][k].setNum(0);
                        j--;
                        break;
                    }else if(mCards[i][j].getNum()!=0&&mCards[i][j].equals(mCards[i][k])&&hMiddleIsZero(i,j,k)){
                        mCards[i][j].setNum(mCards[i][j].getNum()*2);
                        mCards[i][k].setNum(0);
                        score+=mCards[i][j].getNum();
                        Alarm.getAlarm().setScore(score);
                        break;
                    }

                }
            }
        }
        addBroad();
        completeGame();
    }

    private void moveRight() {

        for (int i = 0; i < 4; i++) {
            for (int j = 3; j >=0; j--) {
                for (int k = j-1; k >=0; k--) {
                    if (mCards[i][j].getNum()==0&&mCards[i][k].getNum()>0){
                        mCards[i][j].setNum(mCards[i][k].getNum());
                        mCards[i][k].setNum(0);
                        j++;
                        break;
                    }else if(mCards[i][j].getNum()!=0&&mCards[i][j].equals(mCards[i][k])&&hMiddleIsZero(i,j,k)){
                        mCards[i][j].setNum(mCards[i][j].getNum()*2);
                        mCards[i][k].setNum(0);
                        score+=mCards[i][j].getNum();
                        Alarm.getAlarm().setScore(score);
                        break;
                    }
                }
            }
        }
        addBroad();
        completeGame();
    }


//     这个方法用来判断合并方块的时候中间的方块（如果有的话）是否为0
//     i 三层循环最外层
//     j 三层循环第二层
//     k 三层循环第三层
//     return true 表示中间的方块全部为零（如果有的话）

    private boolean hMiddleIsZero(int i, int j, int k) {
        if(Math.abs(j-k)==1){
            return true;
        }else if (Math.abs(j-k)==2){
            if (j>k){
                return mCards[i][k+1].getNum()==0;
            }else{
                return mCards[i][j+1].getNum()==0;
            }
        }else if(Math.abs(j-k)==3){
            if (j>k){
                return mCards[i][k+1].getNum()==0&&mCards[i][k+2].getNum()==0;
            }else{
                return mCards[i][j+1].getNum()==0&&mCards[i][j+2].getNum()==0;
            }
        }
        return false;
    }
    private boolean vMiddleIsZero(int j, int i, int k) {
        if(Math.abs(i-k)==1){
            return true;
        }else if (Math.abs(i-k)==2){
            if (i>k){
                return mCards[k+1][j].getNum()==0;
            }else{
                return mCards[i+1][j].getNum()==0;
            }
        }else if(Math.abs(i-k)==3){
            if (i>k){
                return mCards[k+1][j].getNum()==0&&mCards[k+2][j].getNum()==0;
            }else{
                return mCards[i+1][j].getNum()==0&&mCards[i+2][j].getNum()==0;
            }
        }
        return false;
    }
}
