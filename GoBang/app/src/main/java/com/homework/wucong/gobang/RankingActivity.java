package com.homework.wucong.gobang;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.style.FontStyle;
import com.homework.wucong.gobang.activities.DatabaseHelper;

import java.util.ArrayList;

public class RankingActivity extends AppCompatActivity {
    //数据库变量
    private SQLiteOpenHelper databaseHelper;
    SQLiteDatabase db;
    SmartTable rankingTable;
    ArrayList<RankingItem> itemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        rankingTable = findViewById(R.id.ranking_table);
        itemList = new ArrayList<>();
        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getReadableDatabase();
        queryRanking();
        showRanking();
    }

    private void showRanking(){
        FontStyle fontStyle = new FontStyle(40, Color.WHITE);
        rankingTable.getConfig().setMinTableWidth((int)(627*2.5));
        rankingTable.getConfig().setContentStyle(fontStyle);
        rankingTable.getConfig().setXSequenceStyle(fontStyle);
        rankingTable.getConfig().setYSequenceStyle(fontStyle);
        rankingTable.getConfig().setColumnTitleStyle(fontStyle);
        rankingTable.getConfig().setShowTableTitle(false);
        rankingTable.getConfig().setShowXSequence(false);
        rankingTable.getConfig().setShowYSequence(false);
        rankingTable.setData(itemList);
    }

    public void queryRanking(){
        Cursor cs= db.query(DatabaseHelper.RANKING_TABLE_NAME, null, null, null, null, null, "peice_count desc");
        if(cs.moveToNext()){
            for( int i=0;i<cs.getCount();i++){
                cs.move(i);
                String name = cs.getString(cs.getColumnIndex("name" ));
                int peice_count = cs.getInt(cs.getColumnIndex( "peice_count"));
                itemList.add(new RankingItem(i + 1,name, peice_count));
            }
        }

//        for(int i = 0;i < itemList.size() - 1;i++){
//            for(int j = i;j < itemList.size() - i;j++){
//                if((itemList.get(j)).getPeiceCount() > (itemList.get(j+1)).getPeiceCount()) {
//                    RankingItem item = itemList.get(j);
//                    itemList.set(j,itemList.get(j+1));
//                    itemList.set(j+1,item);
//                }
//            }
//        }
    }
}
