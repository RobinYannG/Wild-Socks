package fr.wcs.wildcommunitysocks;

/**
 * Created by wilder on 10/04/17.
 */

public class Comment {

    private String mUserName;
    private String mSockId;
    private String mComment;

    private Comment(){

    }
    public Comment(String userName, String sockId, String comment){
        mUserName = userName;
        mSockId = sockId;
        mComment =comment;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserId) {
        this.mUserName = mUserId;
    }

    public String getmSockId() {
        return mSockId;
    }

    public void setmSockId(String mSockId) {
        this.mSockId = mSockId;
    }

    public String getmComment() {
        return mComment;
    }

    public void setmComment(String mComment) {
        this.mComment = mComment;
    }
}
