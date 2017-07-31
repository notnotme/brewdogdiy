package com.notnotme.brewdog_recipes.controller.adapter.hops;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.Hops;

import java.util.ArrayList;
import java.util.List;

public final class HopsAdapter extends RecyclerView.Adapter<HopsViewHolder> {

    private ArrayList<Hops> mItems;

    public HopsAdapter(List<Hops> hops) {
        mItems = new ArrayList<>(hops);
    }

    @Override
    public HopsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new HopsViewHolder(layoutInflater.inflate(R.layout.item_hops, parent, false));
    }

    @Override
    public void onBindViewHolder(HopsViewHolder holder, int position) {
        holder.onBindViewHolder(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
