package com.notnotme.brewdog_recipes.controller.services;

import android.app.IntentService;

import com.notnotme.brewdog_recipes.controller.application.ControllerProvider;
import com.notnotme.brewdog_recipes.controller.application.api.ApiProvider;
import com.notnotme.brewdog_recipes.controller.application.storage.StorageProvider;

public abstract class BaseIntentService extends IntentService implements ApiProvider, StorageProvider {

    private ApiController mApiController;
    private StorageController mStorageController;

    public BaseIntentService(String name) {
        super(name);
        try {
            mApiController = ControllerProvider.createController(ApiController.class);
            mStorageController = ControllerProvider.createController(StorageController.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mApiController.onCreate(this, null);
        mApiController.onStart();

        mStorageController.onCreate(this, null);
        mStorageController.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mApiController.onStop();
        mApiController.onDestroy();

        mStorageController.onStop();
        mStorageController.onDestroy();
    }

    @Override
    public ApiProvider.ApiController getApiController() {
        return mApiController;
    }

    @Override
    public StorageProvider.StorageController getStorageController() {
        return mStorageController;
    }

}
