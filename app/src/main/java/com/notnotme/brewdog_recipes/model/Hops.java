package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Hops extends RealmObject implements Parcelable {

    @SerializedName("name")
    private String mName;

    @SerializedName("amount")
    private Amount mAmount;

    @SerializedName("add")
    private String mAdd;

    @SerializedName("attribute")
    private String mAttribute;

    public Hops() {
    }

    public String getName() {
        return mName;
    }

    public Amount getAmount() {
        return mAmount;
    }

    public String getAdd() {
        return mAdd;
    }

    public String getAttribute() {
        return mAttribute;
    }

    protected Hops(Parcel in) {
        mName = in.readString();
        mAmount = in.readParcelable(Amount.class.getClassLoader());
        mAdd = in.readString();
        mAttribute = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeParcelable(mAmount, flags);
        dest.writeString(mAdd);
        dest.writeString(mAttribute);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Hops> CREATOR = new Creator<Hops>() {
        @Override
        public Hops createFromParcel(Parcel in) {
            return new Hops(in);
        }

        @Override
        public Hops[] newArray(int size) {
            return new Hops[size];
        }
    };

}
