package hotel_project.model;

/**
 * Created by Chayut on 25-Mar-16.
 */
public class Room {

    private String number;
    private String type;
    private String numberOfPerson;
    private String price;
    private String onService;

    public Room(String number, String type, String numberOfPerson, String price, String onService) {
        this.number = number;
        this.type = type;
        this.numberOfPerson = numberOfPerson;
        this.price = price;
        this.onService = onService;
    }

    public String getOnService() {
        return onService;
    }

    public void setOnService(String onService) {
        this.onService = onService;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberOfPerson() {
        return numberOfPerson;
    }

    public void setNumberOfPerson(String numberOfPerson) {
        this.numberOfPerson = numberOfPerson;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
