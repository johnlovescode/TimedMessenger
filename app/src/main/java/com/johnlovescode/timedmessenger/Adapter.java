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

    public Adapter(DBAdapter db)
    {
        this.db = db;

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
        //TODO figure out a way to add a non visible field to store message_ID so it can be used instead of position for querying
        //TODO this wont work fix quick
        viewHolder.setText(db.getMessage((long)position).getString(1));

    }

    @Override
    public int getItemCount() {
        return db.getMessageList().getCount();
    }






    public class Item extends RecyclerView.ViewHolder
    {
        public TextView textView;
        public Item(@NonNull View itemView) {
            super(itemView);
            textView=(TextView) itemView.findViewById(R.id.item);
        }

        public void setText(String text)
        {
            textView.setText(text);
        }
    }
}
