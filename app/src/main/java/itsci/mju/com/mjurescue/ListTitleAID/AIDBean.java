package itsci.mju.com.mjurescue.ListTitleAID;

/**
 * Created by Tom on 7/9/2559.
 */
public class AIDBean {
    private String aidId;
    private String title;
    private String detail;
    private String image;

    public AIDBean() {
    }

    public String getAidId() {
        return aidId;
    }

    public void setAidId(String aidId) {
        this.aidId = aidId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
