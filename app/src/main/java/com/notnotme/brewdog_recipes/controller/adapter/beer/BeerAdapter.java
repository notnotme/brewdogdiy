package com.notnotme.brewdog_recipes.controller.adapter.beer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.model.Beer;


public final class BeerAdapter extends RecyclerView.Adapter<BeerViewHolder> {

    public interface AdapterCallback {
        boolean isFavorite(Beer beer);
        Beer getItem(int position);
        int getItemCount();
        void onItemClicked(Beer beer, View view);
        void onItemLongClicked(Beer beer, View view);
        void onItemSwiped(Beer beer);
    }

    private AdapterCallback mAdapterCallback;

    private View.OnClickListener mOnItemClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    BeerViewHolder holder = (BeerViewHolder) view.getTag();
                    mAdapterCallback.onItemClicked(
                            mAdapterCallback.getItem(holder.getAdapterPosition()), view);
                }
            };

    private View.OnLongClickListener mOnItemLongClickListener =
            new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    BeerViewHolder holder = (BeerViewHolder) view.getTag();
                    mAdapterCallback.onItemLongClicked(
                            mAdapterCallback.getItem(holder.getAdapterPosition()), view);

                    return true;
                }
            };

    private ItemTouchHelper.SimpleCallback mItemTouchHelperCallback =
            new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                Paint mPaint = new Paint();

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(RecyclerView.ViewHolder holder, int direction) {
                    mAdapterCallback.onItemSwiped(mAdapterCallback.getItem(holder.getAdapterPosition()));
                }

                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                    View itemView = viewHolder.itemView;
                    float value =  Math.abs(dX) / itemView.getWidth();

                    if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                        mPaint.setColor(ColorUtils.setAlphaComponent(Color.LTGRAY, 64 - (int) (value * 63)));
                        c.drawRect(new RectF((float) itemView.getLeft(), (float) itemView.getTop(),
                                itemView.getRight(),(float) itemView.getBottom()), mPaint);
                    }
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
                }
            };

    public BeerAdapter(RecyclerView recyclerView, AdapterCallback adapterCallback) {
        mAdapterCallback = adapterCallback;
        if (recyclerView != null) {
            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mItemTouchHelperCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }
    }

    @Override
    public BeerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BeerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_beer, parent, false));
    }

    @Override
    public void onBindViewHolder(BeerViewHolder holder, int position) {
        Beer beer = mAdapterCallback.getItem(position);
        holder.onBindViewHolder(beer, mAdapterCallback.isFavorite(beer));
        holder.itemView.setTag(holder);
        holder.itemView.setOnClickListener(mOnItemClickListener);
        holder.itemView.setOnLongClickListener(mOnItemLongClickListener);
    }

    @Override
    public int getItemCount() {
        return mAdapterCallback.getItemCount();
    }

}
