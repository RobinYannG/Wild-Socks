package fr.wcs.wildcommunitysocks;

public class Chaussette {

    private String mImgChaussette;
    private String mLegende;
    private String mIdUser;
    //private int mIdChaussette;
    private double mNote;
    //public static int mNId;

    private Chaussette () {
    }

    public Chaussette (String urlChaussette, String legende, String idUser){
        mImgChaussette = urlChaussette;
        mLegende = legende;
        //mIdChaussette = ++ mNId;
        mIdUser = idUser;
        mNote=0;
    }

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

  /**  public int getmIdChaussette() {
        return mIdChaussette;
    }


    public void setmIdChaussette(int mIdChaussette) {
        this.mIdChaussette = mIdChaussette;
    }
   **/

    public double getmNote() {
        return mNote;
    }

    public void setmNote(double mNote) {
        this.mNote = mNote;
    }
  /**
    public static int getmNId() {
        return mNId;
    }

    public static void setmNId(int mNId) {
        Chaussette.mNId = mNId;
    }
      **/
}


