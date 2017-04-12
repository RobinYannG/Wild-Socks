package fr.wcs.wildcommunitysocks;

/**
 * Created by wilder on 10/04/17.
 */

public class Comment {

    private String mIdComment;
    private String mAuthorName;
    private String mAuthorId;
    private String mSockId;
    private String mComment;


    private Comment(){
    }
    public Comment(String idComment, String authorId, String authorName, String sockId, String comment){
        mIdComment = idComment;
        mAuthorId =authorId;
        mAuthorName = authorName;
        mSockId = sockId;
        mComment =comment;
    }

    public String getmIdComment() {
        return mIdComment;
    }

    public String getmAuthorId() {
        return mAuthorId;
    }

    public void setmUAuthorId(String authorId) {
        this.mAuthorId = authorId;
    }

    public void setmIdComment(String mIdComment) {
        this.mIdComment = mIdComment;
    }

    public String getmAuthorName() {
        return mAuthorName;
    }

    public void setmAuthorName(String authorName) {
        this.mAuthorName = authorName;
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
