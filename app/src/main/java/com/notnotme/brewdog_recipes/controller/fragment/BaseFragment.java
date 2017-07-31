package com.notnotme.brewdog_recipes.controller.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.notnotme.brewdog_recipes.controller.application.api.ApiProvider;
import com.notnotme.brewdog_recipes.controller.application.storage.StorageProvider;
import com.notnotme.brewdog_recipes.controller.helper.SettingsManager;

public abstract class BaseFragment extends Fragment implements ApiProvider, StorageProvider {

    private SettingsManager mSettingsManager;
    private ApiController mApiController;
    private StorageController mStorageController;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mSettingsManager = new SettingsManager(context);
        mApiController = ((ApiProvider) context).getApiController();
        mStorageController = ((StorageProvider) context).getStorageController();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mApiController = null;
        mStorageController = null;
    }

    @Override
    public ApiController getApiController() {
        return mApiController;
    }

    @Override
    public StorageController getStorageController() {
        return mStorageController;
    }

    public SettingsManager getSettingsManager() {
        return mSettingsManager;
    }

}
