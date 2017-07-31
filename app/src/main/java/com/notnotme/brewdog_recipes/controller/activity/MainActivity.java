package com.notnotme.brewdog_recipes.controller.activity;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.controller.fragment.AboutFragment;
import com.notnotme.brewdog_recipes.controller.fragment.FavoriteBeerListFragment;
import com.notnotme.brewdog_recipes.controller.fragment.FavoriteBeerListFragment.FavoriteBeerListFragmentListener;
import com.notnotme.brewdog_recipes.controller.fragment.SettingsFragment;
import com.notnotme.brewdog_recipes.controller.services.SynchronizationReceiver;
import com.notnotme.brewdog_recipes.controller.services.SynchronizationService;

import static com.notnotme.brewdog_recipes.controller.fragment.BeerListFragment.newInstance;

public final class MainActivity extends BaseActivity implements FavoriteBeerListFragmentListener {

    private final static String TAG = MainActivity.class.getSimpleName();
    private final static String STATE_NAVIGATION_ITEM_ID = TAG + ".navigation_menu_id";

    private DrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ImageView mDrawerLogo;
    private TextView mDrawerTitle;
    private NavigationView mNavigationView;
    private int mNavigationItemId;

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelected =
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.nav_share:
                            ShareCompat.IntentBuilder intentBuilder = ShareCompat.IntentBuilder.from(MainActivity.this);
                            intentBuilder.setType("text/plain")
                                    .setSubject(getString(R.string.share_subject))
                                    .setText(getString(R.string.share_text))
                                    .startChooser();
                            return true;

                        case R.id.nav_rate:
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=" + getPackageName()));
                            try {
                                startActivity(intent);
                            } catch (ActivityNotFoundException e) {
                                intent.setData(Uri.parse("https://play.google.com/store/apps/details?[Id]"));
                                startActivity(intent);
                            }
                            return true;
                    }

                    mDrawerLayout.closeDrawer(GravityCompat.START);
                    int navId = item.getItemId();

                    if (navId != mNavigationItemId) {
                        mNavigationItemId = navId;
                        setToolbarTitle(mNavigationItemId);
                        navigateTo(mNavigationItemId);
                    }

                    return true;
                }
            };

    private DrawerToggle.SlideCallback mDrawerSlideCallback =
            new DrawerToggle.SlideCallback() {
                @Override
                public void onDrawerSlide(View drawerView, float slideOffset) {
                    mDrawerLogo.setAlpha(slideOffset);
                    mDrawerLogo.setTranslationY(-(mDrawerLogo.getHeight() / 4.0f) * (1.0f - slideOffset));

                    mDrawerTitle.setAlpha(slideOffset);
                    mDrawerTitle.setTranslationY((mDrawerTitle.getHeight() / 4.0f) * (1.0f - slideOffset));
                }
            };

    private SynchronizationReceiver mSynchronizationReceiver =
            new SynchronizationReceiver() {
                @Override
                public void onSynchronizationDone() {
                    invalidateBeerCounters();
                }

                @Override
                public void onSynchronizationFailure(String error) {
                }
            };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            mNavigationItemId = R.id.nav_all_beers;
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.content_view, newInstance())
                    .commit();
        } else {
            mNavigationItemId = savedInstanceState.getInt(STATE_NAVIGATION_ITEM_ID);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setToolbarTitle(mNavigationItemId);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new DrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.setSlideCallback(mDrawerSlideCallback);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mNavigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNavigationView.setCheckedItem(mNavigationItemId);
        mNavigationView.setNavigationItemSelectedListener(mOnNavigationItemSelected);

        View headerView = mNavigationView.getHeaderView(0);
        mDrawerLogo = headerView.findViewById(R.id.drawer_logo);
        mDrawerTitle = headerView.findViewById(R.id.drawer_title);

        LocalBroadcastManager.getInstance(this)
                .registerReceiver(mSynchronizationReceiver, new IntentFilter(SynchronizationService.INTENT_ACTION));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateBeerCounters();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_NAVIGATION_ITEM_ID, mNavigationItemId);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mSynchronizationReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    private void setToolbarTitle(int navId) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            switch (navId) {
                // Ids that redirect to other activities
                case R.id.nav_all_beers:
                    actionBar.setTitle(R.string.all_beers);
                    break;
                case R.id.nav_favorite_beers:
                    actionBar.setTitle(R.string.favorite_beers);
                    break;
                case R.id.nav_settings:
                    actionBar.setTitle(R.string.settings);
                    break;
                case R.id.nav_about:
                    actionBar.setTitle(R.string.about);
                    break;
            }
        }
    }

    private void navigateTo(int navId) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (navId) {
            case R.id.nav_all_beers:
                transaction.replace(R.id.content_view, newInstance());
                break;
            case R.id.nav_favorite_beers:
                transaction.replace(R.id.content_view, FavoriteBeerListFragment.newInstance());
                break;
            case R.id.nav_settings:
                transaction.replace(R.id.content_view, SettingsFragment.newInstance());
                break;
            case R.id.nav_about:
                transaction.replace(R.id.content_view, AboutFragment.newInstance());
                break;
        }
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
    }

    @Override
    public void invalidateBeerCounters() {
        ((TextView) mNavigationView.getMenu().findItem(R.id.nav_all_beers).getActionView())
                .setText(String.valueOf(getStorageController().getBeerCount()));

        ((TextView) mNavigationView.getMenu().findItem(R.id.nav_favorite_beers).getActionView())
                .setText(String.valueOf(getStorageController().getFavoriteBeerCount()));
    }

}
