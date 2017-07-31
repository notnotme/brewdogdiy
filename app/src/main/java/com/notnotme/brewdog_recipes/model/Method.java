package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;

public class Method extends RealmObject implements Parcelable {

    @SerializedName("mash_temp")
    private RealmList<MashTemp> mMashTemp;

    @SerializedName("fermentation")
    private Fermentation mFermentation;

    @SerializedName("twist")
    private String mTwist;

    public Method() {
    }

    public RealmList<MashTemp> getMashTemp() {
        return mMashTemp;
    }

    public Fermentation getFermentation() {
        return mFermentation;
    }

    public String getTwist() {
        return mTwist;
    }

    protected Method(Parcel in) {
        mMashTemp = new RealmList<>();
        in.readTypedList(mMashTemp, MashTemp.CREATOR);

        mFermentation = in.readParcelable(Fermentation.class.getClassLoader());
        mTwist = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mMashTemp);
        dest.writeParcelable(mFermentation, flags);
        dest.writeString(mTwist);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Method> CREATOR = new Creator<Method>() {
        @Override
        public Method createFromParcel(Parcel in) {
            return new Method(in);
        }

        @Override
        public Method[] newArray(int size) {
            return new Method[size];
        }
    };

}
