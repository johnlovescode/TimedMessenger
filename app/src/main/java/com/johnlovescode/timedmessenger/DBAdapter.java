package com.johnlovescode.timedmessenger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DBAdapter extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "TimedMessenger.db";
    private static final String TABLE_MESSAGE = "message";

    private static final String FIELD_MESSAGE_ID = "message_id";
    private static final String FIELD_MESSAGE = "message_text";
    private static final String FIELD_TIME_TO_BE_SENT = "timetobesent";
    private static final String FIELD_NEXT_MESSAGE = "nextmessage";
    private static final int DATABASE_VERSION = 1;

    public DBAdapter(@Nullable Context context)
    {
        super(context, DATABASE_NAME, null  , DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("Create table "+TABLE_MESSAGE+" ("+
                FIELD_MESSAGE_ID+" integer Primary Key," +
                FIELD_MESSAGE +" text,"+
                FIELD_NEXT_MESSAGE+ " integer,"+
                FIELD_TIME_TO_BE_SENT+ " Date,"+
                "Foreign Key("+FIELD_NEXT_MESSAGE+") References "+TABLE_MESSAGE+"("+FIELD_MESSAGE_ID+")" +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        //TODO add upgrade
    }

    public void saveRecord()
    {

    }
    public int update()
    {
        return 0;
    }
    public int addRecord()
    {
        return 0;
    }
    public int delete()
    {
        return 0;
    }






}
