package com.example.hp.xmoblie.Helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hp.xmoblie.Items.FileItem;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by HP on 2017-11-10.
 */

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 MONEYBOOK이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        item 문자열 컬럼, price 정수형 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE HISTROY (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "filename TEXT, " +
                "size INTEGER, " +
                "owner TEXT, " +
                "ggid INTEGER, " +
                "gid INTEGER, " +
                "CreateDate DATE, " +
                "LastWriteDate DATE," +
                "type INTEGER);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(FileItem fileItem) throws ParseException {
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("INSERT INTO HISTORY VALUES(null, '" + fileItem.getFilename()
                + "', " + fileItem.getSize()
                + ", '" + fileItem.getOwner()
                + "', " + fileItem.getGgid()
                + ", " + fileItem.getGid()
                + ", '" + fileItem.getCreateDate()
                + "', '" + fileItem.getLastWriteDate()
                + "', " + fileItem.getType() + ");");

        db.close();
    }

    private void delete(String item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM HISTORY WHERE item='" + item + "';");
        db.close();
    }

    private void delete(int item) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM HISTORY WHERE item='" + item + "';");
        db.close();
    }

    private int count() {
        SQLiteDatabase db = getReadableDatabase();
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(filename) FROM HISTORY", null);
        result = cursor.getInt(0);
        return result;
    }


    // 모든 Data 읽기
    public void selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FileItem> fileItemArrayList = new ArrayList<FileItem>();

        Cursor cursor = db.rawQuery("SELECT * FROM HISTORY", null);
        while (cursor.moveToNext()) {
            FileItem fileItem = new FileItem();

        }

    }
}

