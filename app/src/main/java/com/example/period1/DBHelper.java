package com.example.period1;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

public class DBHelper extends SQLiteOpenHelper{
    static String DBNAME = "Users.db";
    static int VERSION = 1;
    static String TABLE_NAME = "UserInfo";
    static String T1Col1 = "id";
    static String T1Col2 = "name";
    static String T1Col3 = "email";
    static String TABLE2_NAME = "PersonalInfo";
    static String T2Col1 = "id";
    static String T2Col2 = "periodDuration";
    static String T2Col3 = "lastPeriod";
    static String T2Col4 = "weight";
    static String T2Col5 = "height";
    static String T2Col6 = "cycleWindow";

    static String CREATE_TABLE_ONE = "create table " + TABLE_NAME + " (" + T1Col1 + "INTEGER PRIMARY KEY AUTOINCREMENT, " + T1Col2 + " TEXT NOT NULL, " + T1Col3 + " TEXT); ";
    static String DROP_TABLE_ONE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    static String CREATE_TABLE_TWO = "create table " + TABLE2_NAME + " (" + T2Col1 + "INTEGER PRIMARY KEY AUTOINCREMENT, " + T2Col2 + " INT, " + T2Col3 + " STRING, " + T2Col4 + " INT, " + T2Col5 + " INT, " + T2Col6 + " INT); ";
    static String DROP_TABLE_TWO = "DROP TABLE IF EXISTS " + TABLE2_NAME;


    public DBHelper (Context context) {
        super(context, DBNAME, null, VERSION);
    }

    public boolean InsertUser(UserData usrObj) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues cv1=new ContentValues();
        ContentValues cv2=new ContentValues();
        cv1.put (T1Col2, usrObj.getName()) ;
        cv2.put(T2Col2, usrObj.getPeriodDuration());
        cv2.put(T2Col3,usrObj.getLastPeriodDate());
        cv2.put(T2Col4,usrObj.getWeight());
        cv2.put(T2Col5,usrObj.getHeight());
        cv2.put(T2Col6,usrObj.getCycleWindow());
        long result1 = db.insert(TABLE_NAME, null, cv1);
        long result2 = db.insert(TABLE2_NAME, null, cv2);

        return (((result1 == -1) && (result2 == -1)) ? false : true);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ONE);
        db.execSQL(CREATE_TABLE_TWO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_ONE);
        db.execSQL(DROP_TABLE_TWO);
        onCreate(db);
    }
}
