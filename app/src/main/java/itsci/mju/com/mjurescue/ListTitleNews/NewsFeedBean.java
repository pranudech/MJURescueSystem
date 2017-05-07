package itsci.mju.com.mjurescue.ListTitleNews;

/**
 * Created by Tom on 24/10/2559.
 */

public class NewsFeedBean {

    private int newsFeedID;
    private String title;
    private String content;
    private String date;
    private String image;

    public NewsFeedBean(int newsFeedID, String title, String content, String date, String image) {
        this.newsFeedID = newsFeedID;
        this.title = title;
        this.content = content;
        this.date = date;
        this.image = image;
    }

    public NewsFeedBean(){}

    public int getNewsFeedID() {
        return newsFeedID;
    }

    public void setNewsFeedID(int newsFeedID) {
        this.newsFeedID = newsFeedID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
