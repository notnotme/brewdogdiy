package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;

/**
 * A FoodPairing is just an object that keep a {@link String} of it's value.
 * Because {@link io.realm.Realm} can't save a list of String we have to wrap it into
 * this object.
 *
 * A custom adapter is needed for the Json deserializer used by the {@link com.notnotme.brewdog_recipes.controller.application.api.ApiProvider.ApiController}
 * (can depends of the implementation). This break the abstraction layer a bit but it
 * should not be hard to handle different implementation with that.
 */
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
