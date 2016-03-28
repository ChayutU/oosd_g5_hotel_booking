package hotel_project.model;

/**
 * Created by Chayu on 28-Mar-16.
 */
public class Log {

    private String logId;
    private String customerId;
    private String room;
    private String dateIn;
    private String dateOut;

    public Log(String logId, String customerId, String room, String dateIn, String dateOut) {
        this.logId = logId;
        this.customerId = customerId;
        this.room = room;
        this.dateIn = dateIn;
        this.dateOut = dateOut;
    }

    public String getLogId() {
        return logId;
    }

    public void setLogId(String logId) {
        this.logId = logId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getDateIn() {
        return dateIn;
    }

    public void setDateIn(String dateIn) {
        this.dateIn = dateIn;
    }

    public String getDateOut() {
        return dateOut;
    }

    public void setDateOut(String dateOut) {
        this.dateOut = dateOut;
    }
}
