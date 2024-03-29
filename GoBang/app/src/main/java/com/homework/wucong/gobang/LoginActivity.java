package com.homework.wucong.gobang;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.homework.wucong.gobang.activities.PlayActivity;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intentLogin = new Intent(LoginActivity.this, HomePage.class);
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String user = preferences.getString("userName","");
        if(!user.equals("")){
            startActivity(intentLogin);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    /**
     * 设置用户名
     * @param view
     */
    public void loginIn(View view){
        Intent intentLogin = new Intent(LoginActivity.this, HomePage.class);
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        String user = preferences.getString("userName","");

        //判断是否已经存在用户名，是则直接跳转到主页
        if(user.equals("")){
            EditText editText = findViewById(R.id.UserName);
            String name = editText.getText().toString();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("userName",name);
            editor.commit();
        }
        startActivity(intentLogin);
    }
}
