package com.johnlovescode.timedmessenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.database.Cursor;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Cursor cursor;

    public Adapter(Cursor cursor)
    {
        this.cursor = cursor;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View row = inflater.inflate(R.layout.costume_row, null,false);
        Item item = new Item(row);
        return item;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        viewHolder.setText();
    }

    @Override
    public int getItemCount() {
        return items.length;
    }






    public class Item extends RecyclerView.ViewHolder
    {
        private TextView textView;
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
