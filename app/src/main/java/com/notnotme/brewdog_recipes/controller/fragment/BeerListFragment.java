package com.notnotme.brewdog_recipes.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.controller.activity.DetailsActivity;
import com.notnotme.brewdog_recipes.controller.adapter.beer.BeerAdapter;
import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.controller.services.SynchronizationReceiver;
import com.notnotme.brewdog_recipes.controller.services.SynchronizationService;
import com.notnotme.brewdog_recipes.model.Beer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class BeerListFragment extends BaseFragment {

    private static final String TAG = BeerListFragment.class.getSimpleName();
    private static final String STATE_LIST_POSITION = TAG + ".list_position";

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mBeerRecycler;
    private ArrayList<Beer> mBeerList;

    private SynchronizationReceiver mSynchronizationReceiver =
            new SynchronizationReceiver() {
                @Override
                public void onSynchronizationDone() {
                    if (isDetached()) return;
                    mSwipeRefreshLayout.setRefreshing(false);
                    getSettingsManager().setLastSyncDate(new Date());
                    getStorageController().getBeers(mGetBeersCallback);
                }

                @Override
                public void onSynchronizationFailure(String error) {
                    if (isDetached()) return;
                    mSwipeRefreshLayout.setRefreshing(false);
                    String message = "Beers synchronization error: " + error;
                    Log.e(TAG, message);
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout),
                            message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                }
            };

    private Callback<List<Beer>, Throwable> mGetBeersCallback =
            new Callback<List<Beer>, Throwable>() {
                @Override
                public void success(List<Beer> success) {
                    if (isDetached()) return;
                    mBeerList.clear();
                    mBeerList.addAll(success);
                    mBeerRecycler.getAdapter().notifyDataSetChanged();

                    if (mBeerRecycler.getTag() != null) {
                        mBeerRecycler.getLayoutManager()
                                .onRestoreInstanceState((Parcelable) mBeerRecycler.getTag());

                        mBeerRecycler.setTag(null);
                    }
                }

                @Override
                public void error(Throwable error) {
                    if (isDetached()) return;
                    String message = "Beers error: " + error.getMessage();
                    Snackbar.make(getActivity().findViewById(R.id.coordinator_layout),
                            message, BaseTransientBottomBar.LENGTH_INDEFINITE).show();
                }
            };

    private BeerAdapter.AdapterCallback mBeerAdapterCallback =
            new BeerAdapter.AdapterCallback() {
                @Override
                public boolean isFavorite(Beer beer) {
                    return getStorageController().isFavoriteBeer(beer);
                }

                @Override
                public Beer getItem(int position) {
                    return mBeerList.get(position);
                }

                @Override
                public int getItemCount() {
                    return mBeerList.size();
                }

                @Override
                public void onItemClicked(Beer beer, View view) {
                    Intent beerDetailsIntent = new Intent(getContext(), DetailsActivity.class);
                    beerDetailsIntent.putExtra(DetailsActivity.ARG_BEER, beer);
                    startActivity(beerDetailsIntent);
                }

                @Override public void onItemLongClicked(Beer beer, View view) {}
                @Override public void onItemSwiped(Beer beer) {}
            };

    public static BeerListFragment newInstance() {
        return new BeerListFragment();
    }

    public BeerListFragment() {
        mBeerList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_beer_list, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(mSynchronizationReceiver, new IntentFilter(SynchronizationService.INTENT_ACTION));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = getContext();

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh);
        mBeerRecycler = view.findViewById(R.id.beer_recycler);
        mBeerRecycler.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mBeerRecycler.setAdapter(new BeerAdapter(null, mBeerAdapterCallback));
        mBeerRecycler.setHasFixedSize(true);

        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(context, android.R.color.holo_blue_bright),
                ContextCompat.getColor(context, android.R.color.holo_orange_light),
                ContextCompat.getColor(context, android.R.color.holo_red_light),
                ContextCompat.getColor(context, android.R.color.holo_purple));

        if (getSettingsManager().getLastSyncDate().getTime() == 0) {
            mSwipeRefreshLayout.setRefreshing(true);
            getActivity().startService(new Intent(context, SynchronizationService.class));
        }

        if (savedInstanceState != null) {
            mBeerRecycler.setTag(savedInstanceState.getParcelable(STATE_LIST_POSITION));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getStorageController().getBeers(mGetBeersCallback);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mSynchronizationReceiver);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mBeerRecycler.getAdapter().getItemCount() != 0) {
            outState.putParcelable(STATE_LIST_POSITION,
                    mBeerRecycler.getLayoutManager().onSaveInstanceState());
        }
    }

}
