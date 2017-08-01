package com.notnotme.brewdog_recipes.controller.activity;

import android.app.Activity;
import android.support.annotation.StringRes;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;

final class DrawerToggle extends ActionBarDrawerToggle {

    interface SlideCallback {
        void onDrawerSlide(View drawerView, float slideOffset);
    }

    private SlideCallback mSlideCallback;

    DrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
        super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        if (mSlideCallback != null) {
            mSlideCallback.onDrawerSlide(drawerView, slideOffset);
        }
    }

    void setSlideCallback(SlideCallback slideCallback) {
        mSlideCallback = slideCallback;
    }

}
