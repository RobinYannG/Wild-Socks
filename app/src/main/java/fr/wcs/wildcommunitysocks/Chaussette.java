package fr.wcs.wildcommunitysocks;

import android.media.Image;

public class Chaussette {

    private Image mImgChaussette;
    private String mLegende;
    private int mIdChaussette, mIdUser;

    private Chaussette () {
    }

    public Chaussette (Image imgChaussette, String legende, int idChaussette, int idUser){
        this.mImgChaussette = imgChaussette;
        this.mLegende = legende;
        this.mIdChaussette = idChaussette;
        this.mIdUser = idUser;
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
}
