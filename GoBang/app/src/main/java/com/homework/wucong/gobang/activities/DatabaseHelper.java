package com.homework.wucong.gobang.activities;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    //数据库变量
    public static final String DATABASE_NAME = "goBnag.db";
    public static final String RANKING_TABLE_NAME = "ranking_table";
    private static int version = 1;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建排行榜表
        String sql = " create table " + RANKING_TABLE_NAME + "(id integer primary key autoincrement,name varchar(64), peice_count varchar(64))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
