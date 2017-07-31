package com.notnotme.brewdog_recipes.controller.adapter.malt;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.Malt;

import java.util.ArrayList;
import java.util.List;

public final class MaltAdapter extends RecyclerView.Adapter<MaltViewHolder> {

    private ArrayList<Malt> mItems;

    public MaltAdapter(List<Malt> items) {
        mItems = new ArrayList<>(items);
    }

    @Override
    public MaltViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MaltViewHolder(layoutInflater.inflate(R.layout.item_malt, parent, false));
    }

    @Override
    public void onBindViewHolder(MaltViewHolder holder, int position) {
        holder.onBindViewHolder(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
