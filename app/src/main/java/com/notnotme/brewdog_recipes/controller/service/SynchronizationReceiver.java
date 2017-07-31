package com.notnotme.brewdog_recipes.controller.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public abstract class SynchronizationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getBooleanExtra(SynchronizationService.INTENT_ACTION, true)) {
           onSynchronizationDone();
        } else {
            String error = intent.getStringExtra(SynchronizationService.INTENT_ERROR);
            onSynchronizationFailure(error != null ? error : "Unknown error");
        }
    }

    public abstract void onSynchronizationDone();
    public abstract void onSynchronizationFailure(String error);

}
