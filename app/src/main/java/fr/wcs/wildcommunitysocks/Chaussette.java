package fr.wcs.wildcommunitysocks;

import android.media.Image;

public class Chaussette {

    private Image mImgChaussette;
    private String mLegende;
    private int mIdChaussette, mIdUser;
    private double mNote;

    private Chaussette () {
    }

    public Chaussette (Image imgChaussette, String legende, int idChaussette, int idUser){
        this.mImgChaussette = imgChaussette;
        this.mLegende = legende;
        this.mIdChaussette = idChaussette;
        this.mIdUser = idUser;
        this.mNote=0;
    }

    public Image getmImgChaussette() {
        return mImgChaussette;
    }

    public String getmLegende() {
        return mLegende;
    }

    public int getmIdChaussette() {
        return mIdChaussette;
    }

    public int getmIdUser() {
        return mIdUser;
    }

    public double getmNote() {
        return mNote;
    }

    public void setmNote(double mNote) {
        this.mNote = mNote;
    }
}
