package com.notnotme.brewdog_recipes.controller.adapter.hops;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.Amount;
import com.notnotme.brewdog_recipes.model.Hops;

final class HopsViewHolder extends RecyclerView.ViewHolder {

    private TextView mAdd;
    private TextView mName;
    private TextView mAmount;
    private TextView mAttribute;

    HopsViewHolder(View itemView) {
        super(itemView);
        mAdd = itemView.findViewById(R.id.add);
        mName = itemView.findViewById(R.id.name);
        mAmount = itemView.findViewById(R.id.amount);
        mAttribute = itemView.findViewById(R.id.attribute);
    }

    void onBindViewHolder(Hops hops) {
        mAdd.setText(hops.getAdd());
        mName.setText(hops.getName());
        mAttribute.setText(hops.getAttribute());

        Amount amount = hops.getAmount();
        mAmount.setText(itemView.getContext().getString(R.string.amount, amount.getValue(), amount.getUnit()));
    }

}
