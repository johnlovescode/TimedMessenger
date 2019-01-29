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
    private static final String TABLE_RECIPIENTS = "recipients";
    private static final String TABLE_CONTENTS = "contents";

    //message table fields
    private static final String FIELD_MESSAGE_ID = "message_id";
    private static final String FIELD_TIME_TO_BE_SENT = "timetobesent";
    private static final String FIELD_SUBJECT = "subject";

    //contents table fields
    //id field that is a foreign key of message table primary key
    private static final String FIELD_MESSAGE_TEXT = "message_text";
    private static final String FIELD_ORDER = "order";

    //recipients table fields
    //id field that is a foreign key of message table primary key
    private static final String FIELD_CONTACT_NAME = "contact_name";
    private static final String FIELD_CONTACT_NUMBER = "contact_number";


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
                FIELD_SUBJECT+"text,"+
                FIELD_TIME_TO_BE_SENT+ " Date"+
                ");");
        db.execSQL("Create table "+TABLE_CONTENTS+" ("+
                "content_id integer ," +
                FIELD_ORDER+"integer,"+
                FIELD_MESSAGE_TEXT+ " text,"+
                "Foreign Key(content_id) References "+TABLE_MESSAGE+"("+FIELD_MESSAGE_ID+"));");
        db.execSQL("Create table "+TABLE_RECIPIENTS+" ("+
                "recipient_id integer ," +
                FIELD_CONTACT_NAME+"text,"+
                FIELD_CONTACT_NUMBER+ " text,"+
                "Foreign Key(recipient_id) References "+TABLE_MESSAGE+"("+FIELD_MESSAGE_ID+"));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {
        //TODO add upgrade
    }


    public int updateRecord(long id, String subject, String time,String[] contactNames,String[] contactNumber, String messageText)
    {
        SQLiteDatabase db = getWritableDatabase();

        Message m = new Message();
        //TODO change this from updating to deleting old and inserting new?
        //TODO change method arguments to accept message class instead of long list of arguments
        //TODO try using transactions?
        db.delete(TABLE_CONTENTS,"content_id=?",new String[]{String.valueOf(id)});
        db.delete(TABLE_RECIPIENTS,"recipient_id=?",new String[]{String.valueOf(id)});

        ContentValues values = new ContentValues();
        values.put(FIELD_MESSAGE_ID, id);
        values.put(FIELD_SUBJECT, subject);
        values.put(FIELD_TIME_TO_BE_SENT, time);
        db.update(TABLE_CONTENTS, values, "_id = ?", new String[]{String.valueOf(id)});
        for(int i = 0;i<contactNames.length;i++)
        {
            values = new ContentValues();
            values.put("recipient_id", id);
            values.put(FIELD_CONTACT_NAME, contactNames[i]);
            values.put(FIELD_CONTACT_NUMBER, contactNumber[i]);
            db.update(TABLE_RECIPIENTS, values,"recipient_id=?",new String[]{String.valueOf(id)});
        }
        m.setMessageText(messageText);
        for(int i = 0;i<m.getMessageText().length;i++)
        {
            values = new ContentValues();
            values.put("content_id", id);
            values.put(FIELD_ORDER, i+1);
            values.put(FIELD_MESSAGE_TEXT, m.getMessageText()[i]);
            db.update(TABLE_RECIPIENTS, values,"content_id=?",new String[]{String.valueOf(id)});
        }


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
