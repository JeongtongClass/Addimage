package com.example.addimage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "imageDB.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table imagedata (image blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists imagedata");
        // create new table
        onCreate(sqLiteDatabase);
    }

    //데이터베이스에서 바이트 배열 데이터를 얻는 데 사용.
  public  long storeImage(byte b[]) {
        ContentValues cv =new ContentValues();
        cv.put("image",b);
        return  getWritableDatabase().insert("imagedata",null,cv);
     }

     //비트맵 변환.
  public Bitmap getImage() {
        Bitmap bp = null;
        Cursor cursor = getReadableDatabase().query("imagedata",null,null,null,null,null,null);
        if(cursor.moveToNext()) {
            byte b[] = cursor.getBlob(0);
            ByteArrayInputStream bis = new ByteArrayInputStream(b);
            bp = BitmapFactory.decodeStream(bis);
        }
        return bp;
    }

}
