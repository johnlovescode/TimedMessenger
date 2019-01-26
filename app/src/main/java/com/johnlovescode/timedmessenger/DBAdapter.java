package com.johnlovescode.timedmessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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


    public int updateRecord(long id, String message, String time)
    {
        SQLiteDatabase db = getWritableDatabase();
        //TODO add message parsing to overwrite long messages

        ContentValues values = new ContentValues();
        values.put(FIELD_MESSAGE_ID, id);
        values.put(FIELD_MESSAGE, message);
        values.put(FIELD_TIME_TO_BE_SENT, time);
        return db.update(TABLE_MESSAGE, values, "_id = ?", new String[]{String.valueOf(id)});
    }
    public long addRecord(String message, String time)
    {
        SQLiteDatabase db = getWritableDatabase();
        //TODO add message parsing to add long messages to the db in 160 character chunks



        ContentValues values = new ContentValues();

        values.put(FIELD_MESSAGE, message);
        values.put(FIELD_TIME_TO_BE_SENT, time);
        return db.insert(TABLE_MESSAGE, null, values);
    }

    public int deleteRecord(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        return db.delete(TABLE_MESSAGE, "_id = ?", new String[]{String.valueOf(id)});
    }

    //TODO do i really need this?
    public void saveRecord(long id, String message, String date) {
        //long id = findMessageID(message);

        if (id>0) {
            updateRecord(id, message,date);
        } else {
            addRecord(message,date);
        }
    }

    /*
    *   return values associated with this message id
    *
     */
    public Cursor getMessage(long id)
    {
        //TODO add ability to return long messages associated with this id
        String query = "Select * from Message where message_id="+id+";";
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query,null);

    }
    /*
    *   return a list of all messages stored in the database
    *
     */
    public Cursor getMessageList()
    {
        //TODO add sortability and remove raw query
        String query = "Select * from Message;";
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery(query,null);
    }

}
