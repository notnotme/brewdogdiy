package com.notnotme.brewdog_recipes.controller.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.controller.adapter.hops.HopsAdapter;
import com.notnotme.brewdog_recipes.controller.adapter.malt.MaltAdapter;
import com.notnotme.brewdog_recipes.controller.adapter.mash_temp.MashTempAdapter;
import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.model.Beer;
import com.notnotme.brewdog_recipes.model.Fermentation;
import com.notnotme.brewdog_recipes.model.FoodPairing;
import com.notnotme.brewdog_recipes.model.Ingredients;
import com.notnotme.brewdog_recipes.model.Method;
import com.notnotme.brewdog_recipes.model.Temp;
import com.notnotme.brewdog_recipes.model.Volume;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class DetailsActivity extends BaseActivity {

    private static final String TAG = DetailsActivity.class.getSimpleName();

    public static final String ARG_BEER = TAG + ".beer";
    public static final String STATE_EXPANDED = TAG + ".expanded";

    private MenuItem mFavoriteMenuItem;
    private Beer mBeer;

    private Callback<Beer, Throwable> mSaveFavoriteBeerCallback =
            new Callback<Beer, Throwable>() {
                @Override
                public void success(Beer success) {
                    mFavoriteMenuItem.setEnabled(true);
                    mFavoriteMenuItem.setIcon(R.mipmap.heart_on);
                }

                @Override
                public void error(Throwable error) {
                    mFavoriteMenuItem.setEnabled(true);
                    Log.e(TAG, "Error while saving beer: " + error.getMessage());
                }
            };

    private Callback<Beer, Throwable> mDeleteFavoriteBeerCallback =
            new Callback<Beer, Throwable>() {
                @Override
                public void success(Beer success) {
                    mFavoriteMenuItem.setEnabled(true);
                    mFavoriteMenuItem.setIcon(R.mipmap.heart);
                }

                @Override
                public void error(Throwable error) {
                    mFavoriteMenuItem.setEnabled(true);
                    Log.e(TAG, "Error while saving favorite beer: " + error.getMessage());
                }
            };

    private View.OnClickListener mOnBrewSheetDetailsClickListener =
            new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.GONE);
                    showBrewSheetDetails();
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        mBeer = getIntent().getParcelableExtra(ARG_BEER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(mBeer.getName());
            actionBar.setSubtitle(mBeer.getTagLine());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Picasso.with(getApplicationContext())
                .load(mBeer.getImageUrl())
                .placeholder(R.mipmap.bottle_horiz)
                .rotate(-90)
                .into((ImageView) findViewById(R.id.bottle));

        ((TextView) findViewById(R.id.tagline)).setText(mBeer.getTagLine());
        ((TextView) findViewById(R.id.description)).setText(mBeer.getDescription());
        ((TextView) findViewById(R.id.brewers_tips)).setText(mBeer.getBrewersTips());
        ((TextView) findViewById(R.id.contributor)).setText(mBeer.getContributedBy());

        DateFormat dateFormat = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        ((TextView) findViewById(R.id.first_brewed)).setText(dateFormat.format(mBeer.getFirstBrewed()));

        String foodPairingString = "";
        for (FoodPairing foodPairing : mBeer.getFoodPairing()) {
            foodPairingString += "● " + foodPairing.getValue() + "\n";
        }
        ((TextView) findViewById(R.id.food_pairing)).setText(foodPairingString.substring(0, foodPairingString.lastIndexOf("\n")));

        View brewSheetDetailsButton = findViewById(R.id.brew_sheet_details_button);
        if (savedInstanceState != null && savedInstanceState.getBoolean(STATE_EXPANDED)) {
            brewSheetDetailsButton.setVisibility(View.GONE);
            showBrewSheetDetails();
        } else {
            brewSheetDetailsButton.setOnClickListener(mOnBrewSheetDetailsClickListener);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_beer_details, menu);
        mFavoriteMenuItem = menu.findItem(R.id.item_favorite);
        if (getStorageController().isFavoriteBeer(mBeer)) {
            mFavoriteMenuItem.setIcon(R.mipmap.heart_on);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_favorite:
                if (getStorageController().isFavoriteBeer(mBeer)) {
                    getStorageController().deleteFavoriteBeer(mBeer, mDeleteFavoriteBeerCallback);
                } else {
                    getStorageController().saveFavoriteBeer(mBeer, mSaveFavoriteBeerCallback);
                }
                mFavoriteMenuItem.setEnabled(false);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_EXPANDED, findViewById(R.id.brew_sheet_details) == null);
    }

    private void showBrewSheetDetails() {
        View brewSheetDetails = ((ViewStub) findViewById(R.id.brew_sheet_details)).inflate();

        ((TextView) brewSheetDetails.findViewById(R.id.abv)).setText(String.valueOf(mBeer.getABV()));
        ((TextView) brewSheetDetails.findViewById(R.id.ibu)).setText(String.valueOf(mBeer.getIBU()));
        ((TextView) brewSheetDetails.findViewById(R.id.og)).setText(String.valueOf(mBeer.getTargetOG()));
        ((TextView) brewSheetDetails.findViewById(R.id.fg)).setText(String.valueOf(mBeer.getTargetFG()));
        ((TextView) brewSheetDetails.findViewById(R.id.ebc)).setText(String.valueOf(mBeer.getEBC()));
        ((TextView) brewSheetDetails.findViewById(R.id.srm)).setText(String.valueOf(mBeer.getSRM()));
        ((TextView) brewSheetDetails.findViewById(R.id.ph)).setText(String.valueOf(mBeer.getPH()));
        ((TextView) brewSheetDetails.findViewById(R.id.attenuation_level)).setText(String.valueOf(mBeer.getAttenuationLevel()));

        Volume volume = mBeer.getVolume();
        ((TextView) brewSheetDetails.findViewById(R.id.volume)).setText(getString(R.string.volume, volume.getValue(), volume.getUnit()));

        Volume boilVolume = mBeer.getBoilVolume();
        ((TextView) brewSheetDetails.findViewById(R.id.boil_volume)).setText(getString(R.string.volume, boilVolume.getValue(), boilVolume.getUnit()));

        Ingredients ingredients = mBeer.getIngredients();
        ((TextView) brewSheetDetails.findViewById(R.id.yeast)).setText(String.valueOf(ingredients.getYeast()));

        RecyclerView maltRecycler = brewSheetDetails.findViewById(R.id.recycler_malt);
        maltRecycler.setAdapter(new MaltAdapter(ingredients.getMalt()));
        maltRecycler.setHasFixedSize(true);
        maltRecycler.setNestedScrollingEnabled(false);

        RecyclerView hopsRecycler = brewSheetDetails.findViewById(R.id.recycler_hops);
        hopsRecycler.setAdapter(new HopsAdapter(ingredients.getHops()));
        hopsRecycler.setHasFixedSize(true);
        hopsRecycler.setNestedScrollingEnabled(false);

        Method method = mBeer.getMethod();

        RecyclerView mashTempRecycler = brewSheetDetails.findViewById(R.id.recycler_mash_temp);
        mashTempRecycler.setAdapter(new MashTempAdapter(method.getMashTemp()));
        mashTempRecycler.setHasFixedSize(true);
        mashTempRecycler.setNestedScrollingEnabled(false);

        Fermentation fermentation = method.getFermentation();
        Temp temp = fermentation.getTemp();

        ((TextView) brewSheetDetails.findViewById(R.id.fermentation)).setText(
                getString(R.string.temp, temp.getValue(), temp.getUnit()));


        if (method.getTwist() == null || method.getTwist().isEmpty()) {
            brewSheetDetails.findViewById(R.id.twist_layout).setVisibility(View.GONE);
        } else {
            ((TextView) brewSheetDetails.findViewById(R.id.twist)).setText(method.getTwist());
        }
    }

}
