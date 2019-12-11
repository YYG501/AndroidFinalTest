package com.homework.wucong.gobang;

import android.graphics.Paint;

import com.bin.david.form.annotation.SmartColumn;
import com.bin.david.form.annotation.SmartTable;

@SmartTable(name="")
public class RankingItem {
    @SmartColumn(id=0,name="ID",align = Paint.Align.CENTER)
    private int id;
    @SmartColumn(id=1,name="用户名",align = Paint.Align.CENTER)
    private String userName;
    @SmartColumn(id=2,name="胜利棋子数",align = Paint.Align.CENTER)
    private int peice;

    public RankingItem(int id, String userName, int peiceCount){
        this.id = id;
        this.userName = userName;
        this.peice = peiceCount;
    }

    public int getPeiceCount(){
        return peice;
    }
}