package com.homework.wucong.gobang;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.homework.wucong.gobang.activities.PlayActivity;
import java.io.IOException;


public class HomePage extends AppCompatActivity {
    private static MediaPlayer mp;//用于播放背景音乐
    private SoundPool soundPool;//用于存放按钮触发音效
    private int buttonSound;//按钮触摸音效

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        /* 初始化音效 */
        mp = MediaPlayer.create(this, R.raw.background_music);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mp.setLooping(true); // Set looping
        mp.setVolume(100,100);
        mp.start();

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
        buttonSound = soundPool.load(this, R.raw.home_button_sound, 1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mp.stop();
        try {
            mp.prepare();
        }catch(IllegalStateException e){
            e.printStackTrace();
        }catch( IOException e){
            e.printStackTrace();
        }
        mp.seekTo(0);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mp.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mp.stop();
        mp.release();
    }

    /**
     * 跳转到游戏界面
     * @param view
     */
    public void playOne(View view){
        mp.stop();
        Intent intentPlay = new Intent(HomePage.this,PlayActivity.class);
        //播放音效
        soundPool.play(buttonSound,
                0.1f,   //左耳道音量【0~1】
                0.5f,   //右耳道音量【0~1】
                0,     //播放优先级【0表示最低优先级】
                0,     //循环模式【0表示循环一次，-1表示一直循环，其他表示数字+1表示当前数字对应的循环次数】
                1     //播放速度【1是正常，范围从0~2】
        );
        startActivity(intentPlay);
    }

    public void playTwo(View view){
        Intent intentPlay = new Intent(HomePage.this,PlayActivity.class);
        startActivity(intentPlay);
    }

    /**
     * 跳转到排行榜
     * @param view
     */
    public void openRanking(View view){
        Intent intentPlay = new Intent(HomePage.this,RankingActivity.class);
        startActivity(intentPlay);
    }
}
