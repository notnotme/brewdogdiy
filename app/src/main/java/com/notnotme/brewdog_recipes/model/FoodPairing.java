package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

public class FoodPairing extends RealmObject implements Parcelable {

    private String mValue;

    public FoodPairing() {
    }

    public FoodPairing(String value) {
        mValue = value;
    }

    public String getValue() {
        return mValue;
    }

    protected FoodPairing(Parcel in) {
        mValue = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mValue);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FoodPairing> CREATOR = new Creator<FoodPairing>() {
        @Override
        public FoodPairing createFromParcel(Parcel in) {
            return new FoodPairing(in);
        }

        @Override
        public FoodPairing[] newArray(int size) {
            return new FoodPairing[size];
        }
    };

}
