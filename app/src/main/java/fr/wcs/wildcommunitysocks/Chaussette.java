package fr.wcs.wildcommunitysocks;

import android.media.Image;
import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

public class Chaussette {

    private String urlChaussette;
    private String legende;
    private String idUser;
    private int idChaussette;
    private double note;
    public static int nId;

    private Chaussette () {
    }

    public Chaussette (String urlChaussette, String legende, String idUser){
        this.urlChaussette = urlChaussette;
        this.legende = legende;
        this.idChaussette = ++ nId;
        this.idUser = idUser;
        this.note=0;
    }

    public String getmImgChaussette() {
        return urlChaussette;
    }

    public String getmLegende() {

        return legende;
    }

    public int getmIdChaussette() {

        return idChaussette;
    }

    public String getmIdUser() {

        return idUser;
    }

    public double getmNote() {

        return note;
    }

    public void setNote(double note) {
        this.note = note;
    }
}

