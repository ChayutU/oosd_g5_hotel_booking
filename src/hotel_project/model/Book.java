package hotel_project.model;

/**
 * Created by Chayut on 28-Mar-16.
 */
public class Book {

    private String dateIn;
    private String dateOut;
    private String bookId;
    private String customerId;
    private String roomNumber;

    public Book(String dateIn, String dateOut, String bookId, String customerId, String roomNumber) {
        this.dateIn = dateIn;
        this.dateOut = dateOut;
        this.bookId = bookId;
        this.customerId = customerId;
        this.roomNumber = roomNumber;
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

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
