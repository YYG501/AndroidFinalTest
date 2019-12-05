package com.homework.wucong.gobang.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.homework.wucong.gobang.R;

import java.util.ArrayList;

public class PlayView extends View {

    private final int BOARD_LINE = 15;//棋盘行数
    private final int WIN_COUNT = 5;
    private float boardLineHeight;//棋盘行高
    private int boardWidth;//棋盘整体宽度
    private Paint paint = new Paint();

    private Bitmap whitePiece;
    private Bitmap blackPiece;
    private float ratioPieceWithLineHeight = 3 * 1.0f / 4;

    private ArrayList<Point> whiteArray = new ArrayList<Point>();
    private ArrayList<Point> blackArray = new ArrayList<Point>();
    private boolean isWhiteTurn = true;//当此变量为TRUE时，轮到白棋

    private boolean isGameOver = false;

    public PlayView(Context context) {
        super(context);
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setBackgroundColor(0X66000000);
        paint.setColor(0x99ffffff);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);

        whitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int heightSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(widthMeasureSpec);

        int width = Math.min(widthSize,heightSize);

        if(widthMode == MeasureSpec.UNSPECIFIED) {
            width = heightSize;
        }else if(heightMode == MeasureSpec.UNSPECIFIED){
            width = widthSize;
        }

        setMeasuredDimension(width,width);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardWidth = w;
        boardLineHeight = boardWidth*1.0f / BOARD_LINE;

        int pieceWidth = (int) (boardLineHeight * ratioPieceWithLineHeight);
        whitePiece = Bitmap.createScaledBitmap(whitePiece,pieceWidth,pieceWidth,false);
        blackPiece = Bitmap.createScaledBitmap(blackPiece,pieceWidth,pieceWidth,false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBoard(canvas);
        drawPieces(canvas);
    }

    private void drawPieces(Canvas canvas) {
        //绘制白棋
        for(int i = 0, n = whiteArray.size();i < n;i++) {
            Point whitePoint = whiteArray.get(i);
            canvas.drawBitmap(whitePiece,
                    (whitePoint.x + (1 - ratioPieceWithLineHeight) / 2) * boardLineHeight,
                    (whitePoint.y + (1 - ratioPieceWithLineHeight) / 2) * boardLineHeight,
                    null
            );
        }
        //绘制黑棋
        for(int i = 0, n = blackArray.size();i < n;i++) {
            Point blackPoint = blackArray.get(i);
            canvas.drawBitmap(blackPiece,
                    (blackPoint.x + (1 - ratioPieceWithLineHeight) / 2) * boardLineHeight,
                    (blackPoint.y + (1 - ratioPieceWithLineHeight) / 2) * boardLineHeight,
                    null
            );
        }
    }

    private void drawBoard(Canvas canvas) {
        int w = boardWidth;
        float lineHeight = boardLineHeight;

        //绘制棋盘横线
        for(int i = 0;i < BOARD_LINE;i++){
            int startX = (int) (lineHeight / 2);
            int endX = (int) (w - lineHeight /2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX,y,endX,y,paint);
        }
        //绘制棋盘纵线
        for(int i = 0;i < BOARD_LINE;i++){
            int startY = (int) (lineHeight / 2);
            int endY = (int) (w - lineHeight /2);
            int x = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(x,startY,x,endY,paint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        super.onTouchEvent(event);

        if(isGameOver){
            return false;
        }

        if (action == MotionEvent.ACTION_UP){
            int x = (int) event.getX();
            int y = (int) event.getY();
            Point p = getValidPoint(x,y);

            if(whiteArray.contains(p) || blackArray.contains(p)){
                return false;
            }

            if(isWhiteTurn){
                whiteArray.add(p);
            }else{
                blackArray.add(p);
            }
            invalidate();
            checkGameOver(p.x,p.y,isWhiteTurn);
            isWhiteTurn = !isWhiteTurn;
            return true;
        }
        return true;
    }

    private Point getValidPoint(int x, int y) {
        return new Point(x / (int)boardLineHeight, y / (int)boardLineHeight);
    }

    private void checkGameOver(int x,int y,boolean isWhite){
        if(isWhite){
            if(checkFiveInLine(x,y,whiteArray)){
                Toast.makeText(getContext(), "白棋胜利", Toast.LENGTH_SHORT).show();
            }
        }else{
            if(checkFiveInLine(x,y,blackArray)){
                Toast.makeText(getContext(), "黑棋胜利", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkFiveInLine(int x ,int y,ArrayList<Point> array) {
        boolean win = false;
        win = checkHorizontal(x,y,array);
        if(win) return true;
        win = checkVertical(x,y,array);
        if(win) return true;
        win = checkLeftDiagonal(x,y,array);
        if(win) return true;
        win = checkRightDiagonal(x,y,array);
        if(win) return true;

        return false;
    }

    private boolean checkVertical(int x,int y, ArrayList<Point> points) {
        int count = 1;
        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x - i, y))) {
                count++;
            } else {
                break;
            }
        }

        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x + i, y))) {
                count++;
            } else {
                break;
            }
        }

        if(count == WIN_COUNT)
            return true;
        else
            return false;
    }

    private boolean checkHorizontal(int x,int y, ArrayList<Point> points) {
        int count = 1;
        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x , y - i))) {
                count++;
            } else {
                break;
            }
        }

        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if(count == WIN_COUNT)
            return true;
        else
            return false;
    }

    private boolean checkLeftDiagonal(int x,int y, ArrayList<Point> points) {
        int count = 1;
        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point( x - i , y - i))) {
                count++;
            } else {
                break;
            }
        }

        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x + i, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if(count == WIN_COUNT)
            return true;
        else
            return false;
    }

    private boolean checkRightDiagonal(int x,int y, ArrayList<Point> points) {
        int count = 1;
        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x + i , y - i))) {
                count++;
            } else {
                break;
            }
        }

        for(int i = 1;i < WIN_COUNT;i++) {
            if (points.contains(new Point(x - i, y + i))) {
                count++;
            } else {
                break;
            }
        }

        if(count == WIN_COUNT)
            return true;
        else
            return false;
    }

    public void restart(){
        whiteArray.clear();
        blackArray.clear();
        isGameOver = false;
        isWhiteTurn = true;
        invalidate();
    }
}
