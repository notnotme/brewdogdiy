package com.notnotme.brewdog_recipes.controller.services;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.model.Beer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class SynchronizationService extends BaseIntentService {

    private final static String TAG = SynchronizationService.class.getSimpleName();

    public final static String INTENT_ACTION = "com.notnotme.brewdogs_recipes.controller.services.SynchronizationService";
    public final static String INTENT_ERROR = "com.notnotme.brewdogs_recipes.controller.services.SynchronizationService.error";

    private final static long SYNC_TIMEOUT_SECONDS = 60;

    private AtomicReference<String> mError;
    private CountDownLatch mCountDown;
    private IBinder mBinder;
    private boolean mIsStarted;

    private int mCurrentPage;
    private List<Beer> mAllBeers;

    private Callback<List<Beer>, Throwable> mGetBeersCallback =
            new Callback<List<Beer>, Throwable>() {
                @Override
                public void success(List<Beer> success) {
                    getStorageController().saveBeers(success, mSaveBeersCallback);
                }

                @Override
                public void error(Throwable error) {
                    mError.set(error.getMessage());
                    mCountDown.countDown();
                }
            };

    private Callback<List<Beer>, Throwable> mSaveBeersCallback =
            new Callback<List<Beer>, Throwable>() {
                @Override
                public void success(List<Beer> success) {
                    if (success.isEmpty()) {
                        mCountDown.countDown();
                    } else {
                        mCurrentPage++;
                        getApiController().getBeers(mCurrentPage, mGetBeersCallback);
                    }
                }

                @Override
                public void error(Throwable error) {
                    mError.set(error.getMessage());
                    mCountDown.countDown();
                }
            };

    public SynchronizationService() {
        super(TAG);
        mBinder = new ServiceBinder();
        mAllBeers = Collections.synchronizedList(new ArrayList<Beer>());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (mIsStarted) return;

        mIsStarted = true;
        mAllBeers.clear();
        mCurrentPage = 1;
        mCountDown = new CountDownLatch(1);
        mError = new AtomicReference<>();

        getApiController().getBeers(mCurrentPage, mGetBeersCallback);
        try {
            mCountDown.await(SYNC_TIMEOUT_SECONDS, TimeUnit.SECONDS);
        } catch (Exception e) {
            sendResult("Synchronization service timeout. (60 sec)");
            return;
        }

        sendResult(mError.get());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void sendResult(String error) {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(INTENT_ACTION);

        if (error == null || error.isEmpty()) {
            broadcastIntent.putExtra(INTENT_ACTION, true);
        } else {
            broadcastIntent.putExtra(INTENT_ACTION, false);
            broadcastIntent.putExtra(INTENT_ERROR, error);
        }

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
        mIsStarted = false;
    }

    public boolean isStarted() {
        return mIsStarted;
    }

    public class ServiceBinder extends Binder {
        public SynchronizationService getService() {
            return SynchronizationService.this;
        }
    }

}
