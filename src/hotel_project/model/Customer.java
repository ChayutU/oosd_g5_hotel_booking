package hotel_project.model;

/**
 * Created by Chayut on 28-Mar-16.
 */
public class Customer {

    private String name;
    private String lastName;
    private String sex;
    private String tel;
    private String address;
    private String id;

    public Customer(String name, String lastName, String sex, String tel, String address, String id) {
        this.name = name;
        this.lastName = lastName;
        this.sex = sex;
        this.tel = tel;
        this.address = address;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
