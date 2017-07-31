package com.notnotme.brewdog_recipes.controller.adapter.beer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.Beer;
import com.squareup.picasso.Picasso;

final class BeerViewHolder extends RecyclerView.ViewHolder {

    private ImageView mImage;
    private TextView mName;
    private TextView mTagLine;
    private ImageView mFavorite;

    BeerViewHolder(View itemView) {
        super(itemView);
        mImage = itemView.findViewById(R.id.bottle);
        mName = itemView.findViewById(R.id.name);
        mTagLine = itemView.findViewById(R.id.tagline);
        mFavorite = itemView.findViewById(R.id.favorite);
    }

    void onBindViewHolder(Beer beer, boolean isFavorite) {
        mName.setText(beer.getName());
        mTagLine.setText(beer.getTagLine());
        mFavorite.setVisibility(isFavorite ? View.VISIBLE : View.GONE);
        Picasso.with(itemView.getContext().getApplicationContext())
                .load(beer.getImageUrl())
                .placeholder(R.mipmap.bottle)
                .error(R.mipmap.bottle)
                .into(mImage);

    }

}
