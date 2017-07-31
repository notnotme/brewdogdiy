package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Temp extends RealmObject implements Parcelable {

    @SerializedName("value")
    private float mValue;

    @SerializedName("unit")
    private String mUnit;

    public Temp() {
    }

    public float getValue() {
        return mValue;
    }

    public String getUnit() {
        return mUnit;
    }

    protected Temp(Parcel in) {
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

    public static final Creator<Temp> CREATOR = new Creator<Temp>() {
        @Override
        public Temp createFromParcel(Parcel in) {
            return new Temp(in);
        }

        @Override
        public Temp[] newArray(int size) {
            return new Temp[size];
        }
    };

}
