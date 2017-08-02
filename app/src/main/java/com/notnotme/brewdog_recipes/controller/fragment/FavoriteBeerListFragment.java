package com.notnotme.brewdog_recipes.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.controller.activity.DetailsActivity;
import com.notnotme.brewdog_recipes.controller.adapter.beer.BeerAdapter;
import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.model.Beer;

import java.util.ArrayList;
import java.util.List;

public final class FavoriteBeerListFragment extends BaseFragment {

    private static final String TAG = FavoriteBeerListFragment.class.getSimpleName();
    private static final String STATE_LIST_POSITION = TAG + ".list_position";

    public interface FavoriteBeerListFragmentListener {
        void invalidateBeerCounters();
        void showAllBeers();
    }

    private RecyclerView mBeerRecycler;
    private ArrayList<Beer> mBeerList;
    private View mEmptyState;
    private FavoriteBeerListFragmentListener mFavoriteBeerListFragmentListener;

    private Callback<List<Beer>, Throwable> mGetBeersCallback =
            new Callback<List<Beer>, Throwable>() {
                @Override
                public void success(List<Beer> success) {
                    if (isDetached()) return;
                    mEmptyState.setVisibility(success.isEmpty() ? View.VISIBLE : View.GONE);

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

    private Callback<Beer, Throwable> mDeleteFavoriteBeerCallback =
            new Callback<Beer, Throwable>() {
                @Override
                public void success(Beer success) {
                    int beerIndex = mBeerList.indexOf(success);

                    mBeerList.remove(beerIndex);
                    mFavoriteBeerListFragmentListener.invalidateBeerCounters();

                    RecyclerView.Adapter adapter = mBeerRecycler.getAdapter();
                    adapter.notifyItemRemoved(beerIndex);
                    mEmptyState.setVisibility(adapter.getItemCount() == 0 ? View.VISIBLE : View.GONE);
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
                    // That's just we don't want to show the "fav" icon
                    // But by definition all beers that belong in the list IS a favorite
                    return false;
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

                @Override
                public void onItemLongClicked(Beer beer, View view) {
                }

                @Override
                public void onItemSwiped(Beer beer) {
                    getStorageController().deleteFavoriteBeer(beer, mDeleteFavoriteBeerCallback);
                }
            };

    private View.OnClickListener mShowAllBeersButtonListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mFavoriteBeerListFragmentListener.showAllBeers();
                }
            };

    public static FavoriteBeerListFragment newInstance() {
        return new FavoriteBeerListFragment();
    }

    public FavoriteBeerListFragment() {
        mBeerList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_beers, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEmptyState = view.findViewById(R.id.empty_state);
        mEmptyState.setVisibility(getStorageController().getFavoriteBeerCount() == 0 ? View.VISIBLE : View.GONE);

        Button showAllBeersButton = view.findViewById(R.id.show_all_beers);
        showAllBeersButton.setOnClickListener(mShowAllBeersButtonListener);

        mBeerRecycler = view.findViewById(R.id.beer_recycler);
        mBeerRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mBeerRecycler.setAdapter(new BeerAdapter(mBeerRecycler, mBeerAdapterCallback));
        mBeerRecycler.setHasFixedSize(true);

        if (savedInstanceState != null) {
            mBeerRecycler.setTag(savedInstanceState.getParcelable(STATE_LIST_POSITION));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getStorageController().getFavoriteBeers(mGetBeersCallback);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFavoriteBeerListFragmentListener = (FavoriteBeerListFragmentListener) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mFavoriteBeerListFragmentListener = null;
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
