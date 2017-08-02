package com.notnotme.brewdog_recipes.controller.application.storage.realm;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.notnotme.brewdog_recipes.R;
import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.controller.application.storage.StorageProvider;
import com.notnotme.brewdog_recipes.model.Beer;
import com.notnotme.brewdog_recipes.model.FavoriteBeer;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.RealmModel;
import io.realm.RealmResults;

public final class RealmStorageControllerImpl implements StorageProvider.StorageController {

    private static final long REALM_SCHEMA_VERSION = 1L;

    private Realm mRealm;

    /* This is actually used by {@link com.notnotme.punkapi.controller.application.ControllerProvider} */
    @SuppressWarnings("unused")
    public static void applicationInit(Application application) {
        Realm.init(application);

        RealmConfiguration.Builder builder = new RealmConfiguration.Builder()
                .name(application.getString(R.string.app_name))
                .schemaVersion(REALM_SCHEMA_VERSION)
                .migration(getMigration());

        Realm.setDefaultConfiguration(builder.build());
    }

    @Override
    public void onCreate(Context context, Bundle savedInstanceState) {
        mRealm = Realm.getDefaultInstance();
    }

    @Override
    public void onDestroy() {
        mRealm.close();
    }

    private static RealmMigration getMigration() {
        return new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                // todo: migrate when needed
            }
        };
    }

    @Override
    public void saveFavoriteBeer(Beer beer, Callback<Beer, Throwable> callback) {
        mRealm.executeTransactionAsync(new TransactionCallback<>(new FavoriteBeer(beer.getId())),
                new SuccessCallback<>(beer, callback),
                new ErrorCallback(callback));
    }

    @Override
    public void deleteFavoriteBeer(final Beer beer, Callback<Beer, Throwable> callback) {
        mRealm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<FavoriteBeer> favoriteBeer = realm.where(FavoriteBeer.class).equalTo("mBeerId", beer.getId()).findAll();
                if (!favoriteBeer.isEmpty()) {
                    favoriteBeer.deleteAllFromRealm();
                }
            }
        },
        new SuccessCallback<>(beer, callback),
        new ErrorCallback(callback));
    }

    @Override
    public long getFavoriteBeerCount() {
        final AtomicInteger result = new AtomicInteger();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.set((int) realm.where(FavoriteBeer.class).count());
            }
        });
        return result.get();
    }

    @Override
    public boolean isFavoriteBeer(final Beer beer) {
        final AtomicBoolean result = new AtomicBoolean();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.set(realm.where(FavoriteBeer.class).equalTo("mBeerId", beer.getId()).count() > 0);
            }
        });
        return result.get();
    }

    @Override
    public void saveBeers(List<Beer> beers, Callback<List<Beer>, Throwable> callback) {
        mRealm.executeTransactionAsync(
                new TransactionListCallback(beers),
                new SuccessCallback<>(beers, callback),
                new ErrorCallback(callback));
    }

    @Override
    public void getBeers(Callback<List<Beer>, Throwable> callback) {
        final List<Beer> queryResults = new ArrayList<>();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Beer> results = realm.where(Beer.class).findAll();
                    queryResults.addAll(realm.copyFromRealm(results));
                }
            },
            new SuccessCallback<>(queryResults, callback),
            new ErrorCallback(callback));
    }

    @Override
    public void getFavoriteBeers(Callback<List<Beer>, Throwable> callback) {
        final List<Beer> queryResults = new ArrayList<>();
        mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<FavoriteBeer> results = realm.where(FavoriteBeer.class).findAll();
                    int favoriteSize = results.size();
                    Long ids[] = new Long[favoriteSize];
                    for (int i=0; i<favoriteSize; i++) {
                        ids[i] = results.get(i).getBeerId();
                    }

                    if (ids.length > 0) {
                        RealmResults<Beer> favoriteBeers = realm.where(Beer.class).in("mId", ids).findAll();
                        queryResults.addAll(realm.copyFromRealm(favoriteBeers));
                    }
                }
            },
            new SuccessCallback<>(queryResults, callback),
            new ErrorCallback(callback));
    }

    @Override
    public long getBeerCount() {
        final AtomicInteger result = new AtomicInteger();
        mRealm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                result.set((int) realm.where(Beer.class).count());
            }
        });
        return result.get();
    }

    @Override
    public Beer getRandomBeer() {
        final AtomicReference<Beer> queryResults = new AtomicReference<>();
        mRealm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    RealmResults<Beer> results = realm.where(Beer.class).findAll();
                    if (!results.isEmpty()) {
                        Beer beer = results.get(new Random(System.currentTimeMillis()).nextInt(results.size()));
                        queryResults.set(realm.copyFromRealm(beer));
                    }
                }
            });

        return queryResults.get();
    }

    private static class TransactionCallback<T extends RealmModel> implements Realm.Transaction {
        private T mEntity;

        TransactionCallback(T entity) {
            mEntity = entity;
        }

        @Override
        public void execute(Realm realm) {
            realm.copyToRealmOrUpdate(mEntity);
        }
    }

    private static class TransactionListCallback implements Realm.Transaction {
        private List<? extends RealmModel> mEntities;

        TransactionListCallback(List<? extends RealmModel> entities) {
            mEntities = entities;
        }

        @Override
        public void execute(Realm realm) {
            realm.copyToRealmOrUpdate(mEntities);
        }
    }

    private static class ErrorCallback implements Realm.Transaction.OnError {
        private Handler mHandler;
        private Callback<?, Throwable> mCallback;

        ErrorCallback(Callback<?, Throwable> callback) {
            mHandler = new Handler(Looper.getMainLooper());
            mCallback = callback;
        }

        @Override
        public void onError(final Throwable error) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.error(error);
                }
            });
        }
    }

    private static class SuccessCallback<T> implements Realm.Transaction.OnSuccess {
        private Handler mHandler;
        private Callback<T, Throwable> mCallback;
        private T mResult;

        SuccessCallback(T result, Callback<T, Throwable> callback) {
            mHandler = new Handler(Looper.getMainLooper());
            mResult = result;
            mCallback = callback;
        }

        @Override
        public void onSuccess() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mCallback.success(mResult);
                }
            });
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {}
    @Override public void onStart() {}
    @Override public void onStop() {}

}
