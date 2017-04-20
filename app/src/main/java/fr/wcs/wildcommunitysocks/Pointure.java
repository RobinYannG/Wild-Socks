package fr.wcs.wildcommunitysocks;

public class Pointure  {

    private String mPointure;



    private Pointure () {
    }

    public Pointure (String pointure){
        mPointure = pointure;
    }

    public String getmPointure() {
        return mPointure;
    }

    public void setmPointure(String mPointure) {
        this.mPointure = mPointure;
    }
}
