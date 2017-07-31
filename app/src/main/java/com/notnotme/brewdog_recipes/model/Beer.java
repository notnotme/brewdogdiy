package com.notnotme.brewdog_recipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class Beer extends RealmObject implements Parcelable {

    @PrimaryKey
    @SerializedName("id")
    private long mId;

    @Index
    @SerializedName("name")
    private String mName;

    @SerializedName("tagline")
    private String mTagLine;

    @SerializedName("first_brewed")
    private Date mFirstBrewed;

    @SerializedName("description")
    private String mDescription;

    @SerializedName("image_url")
    private String mImageUrl;

    @SerializedName("abv")
    private float mABV;

    @SerializedName("ibu")
    private float mIBU;

    @SerializedName("target_fg")
    private float mTargetFG;

    @SerializedName("target_og")
    private float mTargetOG;

    @SerializedName("ebc")
    private float mEBC;

    @SerializedName("srm")
    private float mSRM;

    @SerializedName("ph")
    private float mPH;

    @SerializedName("attenuation_level")
    private float mAttenuationLevel;

    @SerializedName("volume")
    private Volume mVolume;

    @SerializedName("boil_volume")
    private Volume mBoilVolume;

    @SerializedName("method")
    private Method mMethod;

    @SerializedName("ingredients")
    private Ingredients mIngredients;

    @SerializedName("brewers_tips")
    private String mBrewersTips;

    @SerializedName("contributed_by")
    private String mContributedBy;

    @SerializedName("food_pairing")
    private RealmList<FoodPairing> mFoodPairing;

    public Beer() {
    }

    public long getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getTagLine() {
        return mTagLine;
    }

    public Date getFirstBrewed() {
        return mFirstBrewed;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public float getABV() {
        return mABV;
    }

    public float getIBU() {
        return mIBU;
    }

    public float getTargetFG() {
        return mTargetFG;
    }

    public float getTargetOG() {
        return mTargetOG;
    }

    public float getEBC() {
        return mEBC;
    }

    public float getSRM() {
        return mSRM;
    }

    public float getPH() {
        return mPH;
    }

    public float getAttenuationLevel() {
        return mAttenuationLevel;
    }

    public Volume getVolume() {
        return mVolume;
    }

    public Volume getBoilVolume() {
        return mBoilVolume;
    }

    public Method getMethod() {
        return mMethod;
    }

    public Ingredients getIngredients() {
        return mIngredients;
    }

    public String getBrewersTips() {
        return mBrewersTips;
    }

    public String getContributedBy() {
        return mContributedBy;
    }

    public List<FoodPairing> getFoodPairing() {
        return mFoodPairing;
    }

    protected Beer(Parcel in) {
        mId = in.readLong();
        mName = in.readString();
        mTagLine = in.readString();
        mFirstBrewed = new Date(in.readLong());
        mDescription = in.readString();
        mImageUrl = in.readString();
        mABV = in.readFloat();
        mIBU = in.readFloat();
        mTargetFG = in.readFloat();
        mTargetOG = in.readFloat();
        mEBC = in.readFloat();
        mSRM = in.readFloat();
        mPH = in.readFloat();
        mAttenuationLevel = in.readFloat();
        mVolume = in.readParcelable(Volume.class.getClassLoader());
        mBoilVolume = in.readParcelable(Volume.class.getClassLoader());
        mMethod = in.readParcelable(Method.class.getClassLoader());
        mIngredients = in.readParcelable(Ingredients.class.getClassLoader());
        mBrewersTips = in.readString();
        mContributedBy = in.readString();

        mFoodPairing = new RealmList<>();
        in.readTypedList(mFoodPairing, FoodPairing.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mName);
        dest.writeString(mTagLine);
        dest.writeLong(mFirstBrewed.getTime());
        dest.writeString(mDescription);
        dest.writeString(mImageUrl);
        dest.writeFloat(mABV);
        dest.writeFloat(mIBU);
        dest.writeFloat(mTargetFG);
        dest.writeFloat(mTargetOG);
        dest.writeFloat(mEBC);
        dest.writeFloat(mSRM);
        dest.writeFloat(mPH);
        dest.writeFloat(mAttenuationLevel);
        dest.writeParcelable(mVolume, flags);
        dest.writeParcelable(mBoilVolume, flags);
        dest.writeParcelable(mMethod, flags);
        dest.writeParcelable(mIngredients, flags);
        dest.writeString(mBrewersTips);
        dest.writeString(mContributedBy);
        dest.writeTypedList(mFoodPairing);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Beer> CREATOR = new Creator<Beer>() {
        @Override
        public Beer createFromParcel(Parcel in) {
            return new Beer(in);
        }

        @Override
        public Beer[] newArray(int size) {
            return new Beer[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Beer) {
            return mId == ((Beer) obj).mId;
        }
        return super.equals(obj);
    }
}
