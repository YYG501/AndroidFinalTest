package com.homework.wucong.gobang;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.homework.wucong.gobang.activities.PlayActivity;


public class HomePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

    }

    /**
     * 跳转到游戏界面
     * @param view
     */
    public void playOne(View view){
        Intent intentPlay = new Intent(HomePage.this,PlayActivity.class);
        startActivity(intentPlay);
    }

    public void playTwo(View view){
        Intent intentPlay = new Intent(HomePage.this,PlayActivity.class);
        startActivity(intentPlay);
        //test
    }
}
