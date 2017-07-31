package com.notnotme.brewdog_recipes.controller.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.notnotme.brewdog_recipes.controller.application.ControllerProvider;
import com.notnotme.brewdog_recipes.controller.application.api.ApiProvider;
import com.notnotme.brewdog_recipes.controller.application.storage.StorageProvider;
import com.notnotme.brewdog_recipes.controller.helper.SettingsManager;


public abstract class BaseActivity extends AppCompatActivity implements ApiProvider, StorageProvider {

    private SettingsManager mSettingsManager;
    private ApiController mApiController;
    private StorageController mStorageController;

    public BaseActivity() {
        try {
            mApiController = ControllerProvider.createController(ApiController.class);
            mStorageController = ControllerProvider.createController(StorageController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsManager = new SettingsManager(this);
        mApiController.onCreate(this, savedInstanceState);
        mStorageController.onCreate(this, savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mApiController.onDestroy();
        mStorageController.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mApiController.onSaveInstanceState(outState);
        mStorageController.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mApiController.onStart();
        mStorageController.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mApiController.onStop();
        mStorageController.onStop();
    }

    @Override
    public StorageController getStorageController() {
        return mStorageController;
    }

    @Override
    public ApiController getApiController() {
        return mApiController;
    }

    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }

}
