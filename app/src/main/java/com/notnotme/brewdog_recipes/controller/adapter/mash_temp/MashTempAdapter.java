package com.notnotme.brewdog_recipes.controller.adapter.mash_temp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.MashTemp;

import java.util.ArrayList;
import java.util.List;

public final class MashTempAdapter extends RecyclerView.Adapter<MashTempViewHolder> {

    private ArrayList<MashTemp> mItems;

    public MashTempAdapter(List<MashTemp> mashTemps) {
        mItems = new ArrayList<>(mashTemps);
    }

    @Override
    public MashTempViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new MashTempViewHolder(layoutInflater.inflate(R.layout.item_mash_temp, parent, false));
    }

    @Override
    public void onBindViewHolder(MashTempViewHolder holder, int position) {
        holder.onBindViewHolder(mItems.get(position));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

}
