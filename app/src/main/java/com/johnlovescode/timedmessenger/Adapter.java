package com.johnlovescode.timedmessenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;

public class Adapter extends RecyclerView.Adapter<Adapter.Item> {


    private DBAdapter db;
    private Cursor c;

    public Adapter(DBAdapter db)
    {
        this.db = db;
        c = db.getMessageListShort();
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.costume_row, viewGroup,false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull Item viewHolder, int position) {
        //set the text to the message from the database

        c.moveToPosition(position);
        viewHolder.setText(c.getString(c.getColumnIndex(DBAdapter.FIELD_SUBJECT)),c.getLong(c.getColumnIndex(DBAdapter.FIELD_MESSAGE_ID)),c.getString(c.getColumnIndex(DBAdapter.FIELD_TIME_TO_BE_SENT)));

    }

    @Override
    public int getItemCount() {
        return db.getMessageListLength();
    }






    public class Item extends RecyclerView.ViewHolder
    {
        private TextView textView;
        private TextView id;
        private TextView date;
        public Item(@NonNull View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.item);
            id = (TextView) itemView.findViewById(R.id.messageId);
            date = (TextView) itemView.findViewById(R.id.dateLabel);
        }

        public void setText(String text, long id,String date)
        {
            textView.setText(text);
            this.id.setText(String.valueOf(id));
            this.id.setVisibility(View.INVISIBLE);
            this.date.setText(date);
        }


    }
}
