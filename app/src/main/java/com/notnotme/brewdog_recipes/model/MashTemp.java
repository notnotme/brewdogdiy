package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

public class MashTemp extends RealmObject implements Parcelable {

    @SerializedName("temp")
    private Temp mTemp;

    @SerializedName("duration")
    private float mDuration;

    public MashTemp() {
    }

    public Temp getTemp() {
        return mTemp;
    }

    public float getDuration() {
        return mDuration;
    }

    protected MashTemp(Parcel in) {
        mTemp = in.readParcelable(Temp.class.getClassLoader());
        mDuration = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mTemp, flags);
        dest.writeFloat(mDuration);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MashTemp> CREATOR = new Creator<MashTemp>() {
        @Override
        public MashTemp createFromParcel(Parcel in) {
            return new MashTemp(in);
        }

        @Override
        public MashTemp[] newArray(int size) {
            return new MashTemp[size];
        }
    };

}
