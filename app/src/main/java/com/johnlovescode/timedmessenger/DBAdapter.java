package com.johnlovescode.timedmessenger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.util.ArrayList;

public class DBAdapter extends SQLiteOpenHelper
{
    private static final String DATABASE_NAME = "TimedMessenger.db";
    private static final String TABLE_MESSAGE = "message";
    private static final String TABLE_RECIPIENTS = "recipients";
    private static final String TABLE_CONTENTS = "contents";

    //message table fields
    public static final String FIELD_MESSAGE_ID = "message_id";
    public static final String FIELD_TIME_TO_BE_SENT = "timetobesent";
    public static final String FIELD_SUBJECT = "subject";

    //contents table fields
    private static final String FIELD_CONTENT_ID = "content_id";
    private static final String FIELD_MESSAGE_TEXT = "message_text";
    private static final String FIELD_ORDER = "order";

    //recipients table fields
    private static final String FIELD_RECIPIENT_ID = "recipient_id";
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
                FIELD_MESSAGE_ID+" integer Primary Key Autoincrement," +
                FIELD_SUBJECT+"text,"+
                FIELD_TIME_TO_BE_SENT+ " text"+
                ");");
        db.execSQL("Create table "+TABLE_CONTENTS+" ("+
                FIELD_CONTENT_ID+" integer ," +
                FIELD_ORDER+"integer,"+
                FIELD_MESSAGE_TEXT+ " text,"+
                "Foreign Key("+FIELD_CONTENT_ID+") References "+TABLE_MESSAGE+"("+FIELD_MESSAGE_ID+"));");
        db.execSQL("Create table "+TABLE_RECIPIENTS+" ("+
                FIELD_RECIPIENT_ID+" integer ," +
                FIELD_CONTACT_NAME+"text,"+
                FIELD_CONTACT_NUMBER+ " text,"+
                "Foreign Key("+FIELD_RECIPIENT_ID+") References "+TABLE_MESSAGE+"("+FIELD_MESSAGE_ID+"));");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion)
    {
        //TODO add upgrade
    }


    public int updateRecord(Message m)
    {
        SQLiteDatabase db = getWritableDatabase();



        //TODO try using transactions?
        //TODO need try/catch
        db.delete(TABLE_CONTENTS,FIELD_CONTENT_ID+"=?",new String[]{String.valueOf(m.getId())});
        db.delete(TABLE_RECIPIENTS,FIELD_RECIPIENT_ID+"=?",new String[]{String.valueOf(m.getId())});

        ContentValues values = new ContentValues();
        values.put(FIELD_MESSAGE_ID, m.getId());
        values.put(FIELD_SUBJECT, m.getSubject());
        values.put(FIELD_TIME_TO_BE_SENT, m.getTimeToBeSent().toString());
        db.update(TABLE_MESSAGE, values, FIELD_MESSAGE_ID+" = ?", new String[]{String.valueOf(m.getId())});
        for(int i = 0;i<m.getContactNames().length;i++)
        {
            values = new ContentValues();
            values.put(FIELD_RECIPIENT_ID, m.getId());
            values.put(FIELD_CONTACT_NAME, m.getContactNames()[i]);
            values.put(FIELD_CONTACT_NUMBER, m.getContactNumber()[i]);
            db.insert(TABLE_RECIPIENTS,null, values);
        }

        for(int i = 0;i<m.getMessageText().length;i++)
        {
            values = new ContentValues();
            values.put(FIELD_CONTENT_ID, m.getId());
            values.put(FIELD_ORDER, i+1);
            values.put(FIELD_MESSAGE_TEXT, m.getMessageText()[i]);
            db.insert(TABLE_CONTENTS, null, values);
        }
        db.close();
        return 1;

    }
    public long addRecord(Message message)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        //TODO add parsing for DATETIME for ease of use
        //insert into main table

        //values.put(FIELD_MESSAGE_ID, message.getId());
        values.put(FIELD_TIME_TO_BE_SENT,message.getTimeToBeSent().toString());
        values.put(FIELD_SUBJECT,message.getSubject());
        message.setId(db.insert(TABLE_MESSAGE, null, values));

        //set recipients table
        for(int i=0;i<message.getContactNumber().length;i++)
        {
            values = new ContentValues();
            values.put(FIELD_RECIPIENT_ID, message.getId());
            values.put(FIELD_CONTACT_NAME,message.getContactNames()[i]);
            values.put(FIELD_CONTACT_NUMBER,message.getContactNumber()[i]);
            db.insert(TABLE_RECIPIENTS,null,values);
        }

        //set contents table
        for(int i=0;i<message.getMessageText().length;i++)
        {
            values = new ContentValues();
            values.put(FIELD_CONTENT_ID, message.getId());
            values.put(FIELD_MESSAGE_TEXT,message.getMessageText()[i]);
            values.put("order",i+1);
            db.insert(TABLE_CONTENTS,null,values);
        }
        db.close();

        return 1;
    }

    public int deleteRecord(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_MESSAGE, FIELD_MESSAGE_ID+"= ?", new String[]{String.valueOf(id)});
        db.delete(TABLE_CONTENTS,FIELD_CONTENT_ID+"=?", new String[]{String.valueOf(id)});
        db.delete(TABLE_RECIPIENTS,FIELD_RECIPIENT_ID+"=?", new String[]{String.valueOf(id)});
        db.close();
        return 1;
    }

    //TODO do i really need this?
    public void saveRecord(Message m) {


        if (m.getId()>0) {
            updateRecord(m);
        } else {
            addRecord(m);
        }
    }

    /*
    *   return values associated with this message id
    *
     */
    public Message getMessage(long id)
    {

        String query = "Select * from "+TABLE_MESSAGE+" where "+FIELD_MESSAGE_ID+"="+id;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor= db.rawQuery(query,null);
        Message retVal= new Message();
        retVal.setId(cursor.getLong(cursor.getColumnIndex(FIELD_MESSAGE_ID)));
        retVal.setSubject(cursor.getString(cursor.getColumnIndex(FIELD_SUBJECT)));
        retVal.setTimeToBeSent(cursor.getString(cursor.getColumnIndex(FIELD_TIME_TO_BE_SENT)));
        query = "Select * from "+TABLE_CONTENTS+" where "+FIELD_CONTENT_ID+"="+id+"Sort by order asc";
        cursor.close();
        cursor= db.rawQuery(query,null);
        String[] temp= new String[cursor.getCount()];
        for(int i =0;i<cursor.getCount();i++)
        {
            temp[i]=cursor.getString(cursor.getColumnIndex(FIELD_MESSAGE_TEXT));
            cursor.moveToNext();
        }
        retVal.setMessageText(temp);
        cursor.close();
        query = "Select * from "+TABLE_RECIPIENTS+" where "+FIELD_RECIPIENT_ID+"="+id;
        cursor= db.rawQuery(query,null);
        temp = new String[cursor.getCount()];
        String[] temp2 = new String[cursor.getCount()];
        for(int i = 0;i<cursor.getCount();i++)
        {
            temp[i]=cursor.getString(cursor.getColumnIndex(FIELD_CONTACT_NAME));
            temp2[i]=cursor.getString(cursor.getColumnIndex(FIELD_CONTACT_NUMBER));
            cursor.moveToNext();
        }
        cursor.close();
        retVal.setContactNames(temp);
        retVal.setContactNumber(temp2);
        db.close();
        return retVal;

    }
    /*
    *   return a list of all messages stored in the database
    *
     */
    public Message[] getMessageList()
    {

        //TODO add sortability and remove raw query
        String query = "Select * from Message;";
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Message> retVal=new ArrayList<>();
        Cursor cursor =db.rawQuery(query,null);
        //TODO is this needed everywhere?
        cursor.moveToFirst();
        for(int i = 0;i<cursor.getCount();i++)
        {
            Message temp = new Message();
            temp.setId(cursor.getLong(cursor.getColumnIndex(FIELD_MESSAGE_ID)));
            retVal.add(temp);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        for(int i =0;i<retVal.size();i++)
        {
            retVal.set(i,getMessage(retVal.get(i).getId()));
        }

        return retVal.toArray(new Message[0]);
    }

    public int getMessageListLength()
    {
        String query = "Select * from Message;";
        SQLiteDatabase db = getReadableDatabase();
        int temp =db.rawQuery(query,null).getCount();
        db.close();
        return temp;
    }

    public Cursor getMessageListShort()
    {
        String query = "Select * from Message;";
        SQLiteDatabase db = getReadableDatabase();

        return db.rawQuery(query,null);
    }
}
