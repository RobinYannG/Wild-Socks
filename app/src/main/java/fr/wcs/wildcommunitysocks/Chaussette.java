package fr.wcs.wildcommunitysocks;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.ServerValue;

import java.util.HashMap;

public class Chaussette implements Parcelable {

    private String mImgChaussette;
    private String mLegende;
    private String mIdUser;
    private String mDisplayNameUser;
    private String mIdChaussette;
    private String mCategory;
    private float mNote;
    private float mSubNote;
    private int mTime;



    private Chaussette () {
    }

    public Chaussette (String idChaussette, String urlChaussette, String legende, String idUser, String displayNameUser,float note,String category, int time){
        mImgChaussette = urlChaussette;
        mLegende = legende;
        mIdChaussette =idChaussette;
        mIdUser = idUser;
        mDisplayNameUser = displayNameUser;
        mNote = note;
        mSubNote = -note;
        mCategory = category;
        mTime = time;

    }

    protected Chaussette(Parcel in) {
        mImgChaussette = in.readString();
        mLegende = in.readString();
        mIdUser = in.readString();
        mDisplayNameUser = in.readString();
        mIdChaussette = in.readString();
        mNote = in.readFloat();
        mSubNote = in.readFloat();
        mCategory=in.readString();
    }

    public static final Creator<Chaussette> CREATOR = new Creator<Chaussette>() {
        @Override
        public Chaussette createFromParcel(Parcel in) {
            return new Chaussette(in);
        }

        @Override
        public Chaussette[] newArray(int size) {
            return new Chaussette[size];
        }
    };

    public String getmImgChaussette() {
        return mImgChaussette;
    }

    public void setmImgChaussette(String mImgChaussette) {
        this.mImgChaussette = mImgChaussette;
    }

    public String getmLegende() {
        return mLegende;
    }

    public void setmLegende(String mLegende) {
        this.mLegende = mLegende;
    }

    public String getmIdUser() {
        return mIdUser;
    }

    public void setmIdUser(String mIdUser) {
        this.mIdUser = mIdUser;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public static Creator<Chaussette> getCREATOR() {
        return CREATOR;
    }

    public String getmDisplayNameUser() {
        return mDisplayNameUser;
    }

    public void setmDisplayNameUser(String mDisplayNameUser) {
        this.mDisplayNameUser = mDisplayNameUser;
    }

    public String getmIdChaussette() {
        return mIdChaussette;
    }

    public void setmIdChaussette(String mIdChaussette) {
        this.mIdChaussette = mIdChaussette;
    }

    public float getmNote() {
        return mNote;
    }

    public void setmNote(float mNote) {
        this.mNote = mNote;
    }

    public float getmSubNote() {
        return mSubNote;
    }

    public void setmSubNote(float mSubNote) {
        this.mSubNote = mSubNote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImgChaussette);
        dest.writeString(mLegende);
        dest.writeString(mIdUser);
        dest.writeString(mDisplayNameUser);
        dest.writeString(mIdChaussette);
        dest.writeFloat(mNote);
        dest.writeFloat(mSubNote);
        dest.writeString(mCategory);
    }




}


