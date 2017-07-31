package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Malt extends RealmObject implements Parcelable {

    @SerializedName("name")
    private String mName;

    @SerializedName("amount")
    private Amount mAmount;

    public Malt() {
    }

    public String getName() {
        return mName;
    }

    public Amount getAmount() {
        return mAmount;
    }

    protected Malt(Parcel in) {
        mName = in.readString();
        mAmount = in.readParcelable(Amount.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeParcelable(mAmount, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Malt> CREATOR = new Creator<Malt>() {
        @Override
        public Malt createFromParcel(Parcel in) {
            return new Malt(in);
        }

        @Override
        public Malt[] newArray(int size) {
            return new Malt[size];
        }
    };

}
