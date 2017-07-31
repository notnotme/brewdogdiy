package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Ingredients extends RealmObject implements Parcelable {

    @SerializedName("malt")
    private RealmList<Malt> mMalt;

    @SerializedName("hops")
    private RealmList<Hops> mHops;

    @SerializedName("yeast")
    private String mYeast;

    public Ingredients() {
    }

    public RealmList<Malt> getMalt() {
        return mMalt;
    }

    public RealmList<Hops> getHops() {
        return mHops;
    }

    public String getYeast() {
        return mYeast;
    }

    protected Ingredients(Parcel in) {
        mMalt = new RealmList<>();
        in.readTypedList(mMalt, Malt.CREATOR);

        mHops = new RealmList<>();
        in.readTypedList(mHops, Hops.CREATOR);

        mYeast = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mMalt);
        dest.writeTypedList(mHops);
        dest.writeString(mYeast);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Ingredients> CREATOR = new Creator<Ingredients>() {
        @Override
        public Ingredients createFromParcel(Parcel in) {
            return new Ingredients(in);
        }

        @Override
        public Ingredients[] newArray(int size) {
            return new Ingredients[size];
        }
    };

}
