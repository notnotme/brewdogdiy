package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Volume extends RealmObject implements Parcelable {

    @SerializedName("value")
    private float mValue;

    @SerializedName("unit")
    private String mUnit;

    public Volume() {
    }

    public float getValue() {
        return mValue;
    }

    public String getUnit() {
        return mUnit;
    }

    protected Volume(Parcel in) {
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

    public static final Creator<Volume> CREATOR = new Creator<Volume>() {
        @Override
        public Volume createFromParcel(Parcel in) {
            return new Volume(in);
        }

        @Override
        public Volume[] newArray(int size) {
            return new Volume[size];
        }
    };

}
