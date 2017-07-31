package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class Fermentation extends RealmObject implements Parcelable {

    @SerializedName("temp")
    private Temp mTemp;

    public Fermentation() {
    }

    public Temp getTemp() {
        return mTemp;
    }

    public void setTemp(Temp temp) {
        mTemp = temp;
    }

    protected Fermentation(Parcel in) {
        mTemp = in.readParcelable(Temp.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mTemp, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Fermentation> CREATOR = new Creator<Fermentation>() {
        @Override
        public Fermentation createFromParcel(Parcel in) {
            return new Fermentation(in);
        }

        @Override
        public Fermentation[] newArray(int size) {
            return new Fermentation[size];
        }
    };

}
