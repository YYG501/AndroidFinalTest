package com.homework.wucong.gobang.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.homework.wucong.gobang.CustomDialog;
import com.homework.wucong.gobang.R;
import java.util.ArrayList;


public class PlayView extends View {
    private final int BOARD_LINE = 15;//棋盘行数
    private final int WIN_COUNT = 5;//胜利连子数
    private float boardLineHeight;//棋盘行高
    private int boardHeight;//棋盘整体高度
    private Paint paint = new Paint();//用于绘制棋盘和棋子

    private Bitmap whitePiece;//白棋子
    private Bitmap blackPiece;//黑棋子
    private float ratioPieceWithLineHeight = 3 * 1.0f / 4;//棋子与棋盘格子大小的比例 固定为3/4

    private ArrayList<Point> whiteArray = new ArrayList<Point>();//存放已经走了的白棋
    private ArrayList<Point> blackArray = new ArrayList<Point>();//存放已经走了的黑棋
    private boolean isWhiteTurn = true;//当此变量为TRUE时，轮到白棋

    private boolean isGameOver = false;//判断游戏是否结束
    private SoundPool soundPool;///用于存放游戏音效
    private int peiceSound;//落子音效ID
    private CustomDialog dialog;

    Context context;//暂存context

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            super.handleMessage(msg);
            switch (msg.what){
                case 1://白棋胜利
                    dialog.show();//show必须在setState之前，因为dialog在初始化时不会调用onCreate，在show调用时才会调用，进而执行初始化
                    dialog.setState(true);
                    break;
                case 2://黑棋胜利
                    dialog.show();
                    dialog.setState(false);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + msg.what);
            }
        }
    };

    public PlayView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public PlayView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    /**
     * 初始化变量
     */
    private void init(){
        setBackgroundColor(0X44FFFFFF);
        paint.setColor(0x99ffffff);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);

        whitePiece = BitmapFactory.decodeResource(getResources(), R.drawable.white);
        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black);

        /* 初始化音效 */
        SoundPool.Builder builder = new SoundPool.Builder();
        //传入最多播放音频数量,
        builder.setMaxStreams(3);
        //AudioAttributes是一个封装音频各种属性的方法
        AudioAttributes.Builder attrBuilder = new AudioAttributes.Builder();
        //设置音频流的合适的属性
        attrBuilder.setLegacyStreamType(AudioManager.STREAM_MUSIC);
        //加载一个AudioAttributes
        builder.setAudioAttributes(attrBuilder.build());
        soundPool = builder.build();
        peiceSound = soundPool.load(context, R.raw.piece_sound, 1);

        dialog = new CustomDialog(context);
        dialog.setYesOnclickListener( new CustomDialog.onYesOnclickListener() {
            @Override
            public void onYesClick() {
                restart();
                dialog.dismiss();
            }
        });
        dialog.setNoOnclickListener(new CustomDialog.onNoOnclickListener() {
            @Override
            public void onNoClick() {
                ((PlayActivity)context).finish();
                dialog.dismiss();
            }
        });
    }

    /**
     *设置View大小
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        /* 设置棋盘大小 */
        int height = Math.min(widthSize,heightSize);
        if(widthMode == MeasureSpec.UNSPECIFIED) {
            height = heightSize;
        }else if(heightMode == MeasureSpec.UNSPECIFIED){
            height = widthSize;
        }
        setMeasuredDimension(height,height);
    }

    /**
     * 在app界面大小变化时回调，设置棋盘大小
     * @param w
     * @param h
     * @param oldw
     * @param oldh
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        boardHeight = h;
        boardLineHeight = boardHeight*1.0f / BOARD_LINE;

        int pieceWidth = (int) (boardLineHeight * ratioPieceWithLineHeight);
        whitePiece = Bitmap.createScaledBitmap(whitePiece,pieceWidth,pieceWidth,false);
        blackPiece = Bitmap.createScaledBitmap(blackPiece,pieceWidth,pieceWidth,false);
    }

    /**
     * 绘制棋盘与棋子
     * @param canvas
     */
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
        int h = boardHeight;
        float lineHeight = boardLineHeight;

        //绘制棋盘横线
        for(int i = 0;i < BOARD_LINE;i++){
            int startX = (int) (lineHeight / 2);
            int endX = (int) (h - lineHeight /2);
            int y = (int) ((0.5 + i) * lineHeight);
            canvas.drawLine(startX,y,endX,y,paint);
        }
        //绘制棋盘纵线
        for(int i = 0;i < BOARD_LINE;i++){
            int startY = (int) (lineHeight / 2);
            int endY = (int) (h - lineHeight /2);
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

            soundPool.play(peiceSound,
                    0.1f,   //左耳道音量【0~1】
                    0.5f,   //右耳道音量【0~1】
                    0,     //播放优先级【0表示最低优先级】
                    0,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                    1     //播放速度【1是正常，范围从0~2】
            );

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

    /**
     * 从用户触摸点坐标获取对应棋盘落子点
     * @param x
     * @param y
     * @return
     */
    private Point getValidPoint(int x, int y) {
        return new Point(x / (int)boardLineHeight, y / (int)boardLineHeight);
    }

    private void checkGameOver(int x,int y,boolean isWhiteTurn){//在落子后调用，检查是否游戏结束
        Message msg =new Message();
        if(isWhiteTurn){
            if(checkFiveInLine(x,y,whiteArray)){//白棋胜利
                addRecord();
                msg.what = 1;
                handler.sendMessage(msg);
            }
        }else{//黑棋胜利
            if(checkFiveInLine(x,y,blackArray)){
                msg.what = 2;
                handler.sendMessage(msg);
            }
        }
    }

    /**
     * 检查落子点是否使得五子（取决于获胜条件）连珠
     * @param x
     * @param y
     * @param array
     * @return
     */
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

    /**
     * 检查落子点的垂直方向是否五子连珠
     * @param x
     * @param y
     * @param points
     * @return
     */
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

    /**
     * 检查落子点的水平方向是否五子连珠
     * @param x
     * @param y
     * @param points
     * @return
     */
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

    /**
     * 检查落子点的从左上到右下方向是否五子连珠
     * @param x
     * @param y
     * @param points
     * @return
     */
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

    /**
     * 检查落子点的从右上到左下方向是否五子连珠
     * @param x
     * @param y
     * @param points
     * @return
     */
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

    /**
     * 再来一局
     */
    public void restart(){
        whiteArray.clear();
        blackArray.clear();
        isGameOver = false;
        isWhiteTurn = true;
        invalidate();
    }

    /**
     * 悔棋
     */
    public void regret(){
        if(isWhiteTurn){//isWhiteTurn的状态为true，表示前面走的是黑棋，也就是说黑棋要悔棋一步
            blackArray.remove(blackArray.size() - 1);
        }else{//表示白棋要悔棋
            whiteArray.remove(whiteArray.size() - 1);
        }
        //上一个走的棋继续下下一步
        isWhiteTurn = !isWhiteTurn;
        invalidate();

    }

    /**
     * 向排行榜中写入记录
     */
    private void addRecord(){

    }
}
