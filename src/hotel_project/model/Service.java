package hotel_project.model;

import org.jdatepicker.impl.JDatePickerImpl;

import java.util.ArrayList;

/**
 * Created by Chayut on 25-Mar-16.
 */
public interface Service {

    ArrayList<Room> searchRooms(String sql);
    ArrayList<Book> searchBooks(String sql);
    ArrayList<Customer> searchCustomers(String sql);
    ArrayList<Log> searchLogs(String sql);

    String datePickerFormat(JDatePickerImpl datePicker);
    boolean validateCustomer(Integer id);
    String whoBook(String room, String timeStampIn, String timeStampOut);

    void insertBook(String sql);
    void cancelBook(String sql);

    int bookIdCreator();


    boolean onService(Integer roomNumber);

    void checkIn(String id, String roomNumber);
}
