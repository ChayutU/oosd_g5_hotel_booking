package hotel_project.model;

import java.util.ArrayList;

/**
 * Created by Chayut on 25-Mar-16.
 */
public interface Service {

    ArrayList<Room> searchRooms(String sql);
    ArrayList<Book> searchBooks(String sql);
    ArrayList<Customer> searchCustomers(String sql);
    ArrayList<Log> searchLogs(String sql);
}
