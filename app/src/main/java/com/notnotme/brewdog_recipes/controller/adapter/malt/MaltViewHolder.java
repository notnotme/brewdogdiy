package com.notnotme.brewdog_recipes.controller.adapter.malt;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.Amount;
import com.notnotme.brewdog_recipes.model.Malt;

final class MaltViewHolder extends RecyclerView.ViewHolder {

    private TextView mName;
    private TextView mAmount;

    MaltViewHolder(View itemView) {
        super(itemView);
        mName = itemView.findViewById(R.id.name);
        mAmount = itemView.findViewById(R.id.amount);
    }

    void onBindViewHolder(Malt malt) {
        mName.setText(malt.getName());

        Amount amount = malt.getAmount();
        mAmount.setText(itemView.getContext().getString(R.string.amount, amount.getValue(), amount.getUnit()));
    }

}
