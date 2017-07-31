package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FavoriteBeer extends RealmObject implements Parcelable {

    @PrimaryKey
    private long mBeerId;

    public FavoriteBeer() {
    }

    public FavoriteBeer(long beerId) {
        mBeerId = beerId;
    }

    public FavoriteBeer(Beer beer) {
        this(beer.getId());
    }

    protected FavoriteBeer(Parcel in) {
        mBeerId = in.readLong();
    }

    public long getBeerId() {
        return mBeerId;
    }

    public void setBeerId(long beerId) {
        mBeerId = beerId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mBeerId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FavoriteBeer> CREATOR = new Creator<FavoriteBeer>() {
        @Override
        public FavoriteBeer createFromParcel(Parcel in) {
            return new FavoriteBeer(in);
        }

        @Override
        public FavoriteBeer[] newArray(int size) {
            return new FavoriteBeer[size];
        }
    };

}
