package fr.wcs.wildcommunitysocks;

import android.os.Parcel;
import android.os.Parcelable;

public class Chaussette implements Parcelable {

    private String mImgChaussette;
    private String mLegende;
    private String mIdUser;
    private String mDisplayNameUser;
    private String mIdChaussette;
    private float mNote;
    private float mSubNote;


    private Chaussette () {
    }

    public Chaussette (String urlChaussette, String legende, String idUser, String displayNameUser,float note){
        mImgChaussette = urlChaussette;
        mLegende = legende;
        mIdChaussette ="";
        mIdUser = idUser;
        mDisplayNameUser = displayNameUser;
        mNote=note;
        mSubNote=-note;
    }

    protected Chaussette(Parcel in) {
        mImgChaussette = in.readString();
        mLegende = in.readString();
        mIdChaussette = in.readString();
        mIdUser = in.readString();
        mDisplayNameUser = in.readString();
        mNote = in.readFloat();
        mSubNote=in.readFloat();
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

    public void setmImgChaussette(String mUrlChaussette) {
        this.mImgChaussette = mUrlChaussette;
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

    public String getmIdChaussette() {
        return mIdChaussette;
    }

    public void setmIdChaussette(String mIdChaussette) {
        this.mIdChaussette = mIdChaussette;
    }

    public float getmNote() {
        return mNote;
    }

    public String getmDisplayNameUser(){
        return mDisplayNameUser;
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
        dest.writeString(mIdChaussette);
        dest.writeString(mIdUser);
        dest.writeString(mDisplayNameUser);
        dest.writeFloat(mNote);
        dest.writeFloat(mSubNote);
    }

}


