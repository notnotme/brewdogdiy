package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Amount extends RealmObject implements Parcelable {

    @SerializedName("value")
    private float mValue;

    @SerializedName("unit")
    private String mUnit;

    public Amount() {
    }

    public float getValue() {
        return mValue;
    }

    public String getUnit() {
        return mUnit;
    }

    protected Amount(Parcel in) {
        mValue = in.readFloat();
        mUnit = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(mValue);
        dest.writeString(mUnit);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Amount> CREATOR = new Creator<Amount>() {
        @Override
        public Amount createFromParcel(Parcel in) {
            return new Amount(in);
        }

        @Override
        public Amount[] newArray(int size) {
            return new Amount[size];
        }
    };

}
