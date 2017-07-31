package com.notnotme.brewdog_recipes.controller.application.api.retrofit;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.notnotme.brewdog_recipes.BuildConfig;
import com.notnotme.brewdog_recipes.controller.application.Callback;
import com.notnotme.brewdog_recipes.controller.application.api.ApiProvider;
import com.notnotme.brewdog_recipes.model.Beer;
import com.notnotme.brewdog_recipes.model.FoodPairing;

import java.lang.reflect.Type;
import java.util.List;

import io.realm.RealmList;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitApiProviderImpl implements ApiProvider.ApiController {

    private static final String TAG = RetrofitApiProviderImpl.class.getSimpleName();

    private static Retrofit sRetrofit;
    private PunkApi mPunkApi;

    @SuppressWarnings("unused")
    public static void applicationInit(Application application) {
        Type token = new TypeToken<RealmList<FoodPairing>>(){}.getType();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(PunkApi.API_URL)
                .addConverterFactory(
                        GsonConverterFactory.create(
                                new GsonBuilder()
                                        .setDateFormat("MM/yyyy")
                                        .registerTypeAdapter(token, new FoodPairingTypeAdapter())
                                        .create()));

        if (BuildConfig.DEBUG) {
            builder.client(new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build());
        }

        sRetrofit = builder.build();
    }

    @Override
    public void onCreate(Context context, Bundle savedInstanceState) {
        mPunkApi = sRetrofit.create(PunkApi.class);
    }

    @Override
    public void getBeers(int page, Callback<List<Beer>, Throwable> callback) {
        mPunkApi.getBeers(page).enqueue(new RetroFitCallback<>(callback));
    }

    private static class RetroFitCallback<T> implements retrofit2.Callback<T> {
        private Callback<T, Throwable> mCallback;
        private Handler mHandler;

        RetroFitCallback(@Nullable Callback<T, Throwable> callback) {
            mCallback = callback;
            mHandler = new Handler(Looper.getMainLooper());
        }

        @Override
        public void onResponse(Call<T> call, final Response<T> response) {
            if (mCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        T responseObject = response.body();
                        if (responseObject == null) {
                            String error = response.message();
                            if (error == null || error.equals("")) {
                                error = "Unknown error";
                            }
                            mCallback.error(new Exception(error));
                        } else {
                            mCallback.success(response.body());
                        }
                    }
                });
            } else {
                Log.e(TAG, "Callback was null so nothing happened: " + call.request().url().toString());
            }
        }

        @Override
        public void onFailure(Call<T> call, final Throwable t) {
            if (mCallback != null) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(TAG, "Error: " + t.getMessage());
                        mCallback.error(t);
                    }
                });
            } else {
                String url = call.request().url().toString();
                Log.e(TAG, "Callback was null so nothing happened: " + url);

                if (t != null) {
                    String error = t.getMessage();
                    if (!error.isEmpty()) {
                        Log.e(TAG, url + ": " + error);
                    } else {
                        Log.e(TAG, url + ": no error message, will provide stacktrace...");
                        t.printStackTrace();
                    }
                } else {
                    Log.e(TAG, url + ": error is null, no stack trace :(");
                }
            }
        }
    }

    @Override public void onSaveInstanceState(Bundle outState) {}
    @Override public void onDestroy() {}
    @Override public void onStart() {}
    @Override public void onStop() {}

}
