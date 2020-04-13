package com.bsuuv.grocerymanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class GroceryListAdapter extends RecyclerView.Adapter<GroceryListAdapter.GroceryViewHolder> {

    private LinkedList<String> mLabels;
    private LayoutInflater mInflator;

    public GroceryListAdapter(Context context, LinkedList<String> labels) {
        mInflator = LayoutInflater.from(context);
        mLabels = labels;
    }

    @NonNull
    @Override
    public GroceryListAdapter.GroceryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflator.inflate(R.layout.cardlist_item, parent, false);

        return new GroceryViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull GroceryListAdapter.GroceryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mLabels.size();
    }

    class GroceryViewHolder extends RecyclerView.ViewHolder {
        public final TextView groceryItemView;
        final GroceryListAdapter mAdapter;

        public GroceryViewHolder(View itemView, GroceryListAdapter adapter) {
            super(itemView);
            groceryItemView = itemView.findViewById(R.id.grocery_item_label);
            this.mAdapter = adapter;
        }
    }
}
