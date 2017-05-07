package itsci.mju.com.mjurescue.RequestAssistance;

public class NotificationBean {

    private String NotificationID;
    private String statusWorking;
    private String date;
    private String latitude;
    private String longitude;
    private String represent;

    public NotificationBean(String notificationID, String statusWorking, String date, String latitude, String longitude,
                            String represent) {
        super();
        NotificationID = notificationID;
        this.statusWorking = statusWorking;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.represent = represent;
    }

    public String getNotificationID() {
        return NotificationID;
    }

    public void setNotificationID(String notificationID) {
        NotificationID = notificationID;
    }

    public String getStatusWorking() {
        return statusWorking;
    }

    public void setStatusWorking(String statusWorking) {
        this.statusWorking = statusWorking;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getRepresent() {
        return represent;
    }

    public void setRepresent(String represent) {
        this.represent = represent;
    }


}
