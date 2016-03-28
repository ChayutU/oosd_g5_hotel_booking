package hotel_project.model;

import edu.sit.cs.db.CSDbDelegate;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chayut on 25-Mar-16.
 */
public class DbService implements Service {

    CSDbDelegate db = new CSDbDelegate("csprog-in.sit.kmutt.ac.th", "3306", "CSC105_G4", "csc105_2014", "csc105");

    @Override
    public ArrayList<Room> searchRooms(String sql) {
        db.connect();

        ArrayList<Room> roomList = new ArrayList<Room>();

        ArrayList<HashMap> roomTmp = db.queryRows(sql);

        for (HashMap room : roomTmp) {
            String number = (String) room.get("room_number");
            String type = (String) room.get("room_type");
            String numberOfPerson = (String) room.get("room_person");
            String price = (String) room.get("room_cost");
            String onService = (String) room.get("room_onService");
            roomList.add(new Room(number, type, numberOfPerson, price, onService));
        }

        db.disconnect();

        return roomList;
    }

    @Override
    public ArrayList<Book> searchBooks(String sql) {
        db.connect();

        ArrayList<Book> bookList = new ArrayList<Book>();

        ArrayList<HashMap> bookTmp = db.queryRows(sql);

        for (HashMap book : bookTmp) {
            String dateIn = (String) book.get("book_dateIn");
            String dateOut = (String) book.get("book_dateOut");
            String bookId = (String) book.get("book_id");
            String customerId = (String) book.get("customer_id");
            String roomNumber = (String) book.get("room_number");
            bookList.add(new Book(dateIn, dateOut, bookId, customerId, roomNumber));
        }

        db.disconnect();

        return bookList;
    }

    @Override
    public ArrayList<Customer> searchCustomers(String sql) {
        db.connect();

        ArrayList<Customer> customerList = new ArrayList<Customer>();

        ArrayList<HashMap> customerTmp = db.queryRows(sql);

        for (HashMap customer : customerTmp) {
            String name = (String) customer.get("customer_name");
            String lastName = (String) customer.get("customer_lastName");
            String sex = (String) customer.get("customer_sex");
            String tel = (String) customer.get("customer_tel");
            String address = (String) customer.get("customer_add");
            String id = (String) customer.get("customer_id")  ;
            customerList.add(new Customer(name, lastName, sex, tel, address, id));
        }

        db.disconnect();

        return customerList;
    }

    @Override
    public ArrayList<Log> searchLogs(String sql) {
        db.connect();

        ArrayList<Log> logList = new ArrayList<Log>();

        ArrayList<HashMap> logTmp = db.queryRows(sql);

        for (HashMap log : logTmp) {
            String logId = (String) log.get("log_id");
            String customerId = (String) log.get("customer_id");
            String room = (String) log.get("log_room");
            String dateIn = (String) log.get("log_dateIn");
            String dateOut = (String) log.get("log_dateOut");
            logList.add(new Log(logId, customerId, room, dateIn, dateOut));
        }

        db.disconnect();

        return logList;
    }
}
