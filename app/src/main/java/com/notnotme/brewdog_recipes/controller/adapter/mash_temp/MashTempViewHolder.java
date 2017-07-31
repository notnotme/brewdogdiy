package com.notnotme.brewdog_recipes.controller.adapter.mash_temp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.MashTemp;
import com.notnotme.brewdog_recipes.model.Temp;

final class MashTempViewHolder extends RecyclerView.ViewHolder {

    private TextView mDuration;
    private TextView mTemperature;

    MashTempViewHolder(View itemView) {
        super(itemView);
        mDuration = itemView.findViewById(R.id.duration);
        mTemperature = itemView.findViewById(R.id.temp);
    }

    void onBindViewHolder(MashTemp mashTemp) {
        mDuration.setText(String.valueOf(mashTemp.getDuration()));

        Temp temp = mashTemp.getTemp();
        mTemperature.setText(itemView.getContext().getString(R.string.temp, temp.getValue(), temp.getUnit()));
    }

}
