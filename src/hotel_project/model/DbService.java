package hotel_project.model;

import edu.sit.cs.db.CSDbDelegate;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            String id = (String) customer.get("customer_id");
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

    @Override
    public String datePickerFormat(JDatePickerImpl datePicker) {
        String dd = "" + datePicker.getModel().getDay();
        String mm = "" + (datePicker.getModel().getMonth() + 1);
        String yyyy = "" + datePicker.getModel().getYear();
        return yyyy + "-" + mm + "-" + dd;
    }

    @Override
    public boolean validateCustomer(Integer id) {
        db.connect();
        ArrayList<HashMap> member = db.queryRows("SELECT customer_id FROM Hotel_customer WHERE customer_id = " + id);
        if (!member.isEmpty()) {
            return true;
        }
        JOptionPane.showMessageDialog(null, "Invalid Member ID");
        db.disconnect();
        return false;
    }

    @Override
    public String whoBook(String room, String timeIn, String timeOut) {
        String sql = "SELECT * FROM Hotel_book WHERE (room_number = " + room + " AND '" + timeIn + "'>= book_dateIn AND book_dateOut >= '" + timeOut + "')"
                + " OR (room_number = " + room + " AND '" + timeOut + "'>= book_dateIn AND book_dateOut >= '" + timeIn + "')";
        ArrayList<Book> matchedBooks = searchBooks(sql);

        for (Book book : matchedBooks) {
            if (!matchedBooks.isEmpty()) {
                return book.getCustomerId();
            }
        }
        return "";
    }

    @Override
    public void insertBook(String sql) {
        db.connect();
        db.executeQuery(sql);
        db.disconnect();
    }

    @Override
    public void cancelBook(String sql) {
        db.connect();
        db.executeQuery(sql);
        db.disconnect();
    }

    @Override
    public int bookIdCreator() {
        db.connect();
        int tmp = 0;
        String sql = "SELECT MAX(book_id) AS MAX FROM Hotel_book";
        ArrayList<HashMap> id = db.queryRows(sql);
        for (HashMap ids : id) {
            try {
                tmp = Integer.parseInt((String) ids.get("MAX"));
            } catch (NumberFormatException e) {
                System.err.print("Book id creator ERROR >>> " + e);
            }
        }
        db.disconnect();
        return tmp + 1;
    }

    @Override
    public boolean onService(Integer roomNumber) {
        db.connect();
        String sql = "SELECT room_onService FROM Hotel_room WHERE room_number = " + roomNumber;
        ArrayList<HashMap> service = db.queryRows(sql);
        for (HashMap s : service) {
            if (Integer.valueOf((String) s.get("room_onService")) == 1) {
                return false;
            }
        }
        db.disconnect();
        return true;
    }

    @Override
    public void checkIn(String id, String roomNumber) {
        db.connect();
        String sql1 = "INSERT INTO Hotel_log(customer_id, log_room) "
                + "VALUES (" + id + ", " + roomNumber + ")";
        String sql2 = "UPDATE Hotel_room SET room_onService = 1 WHERE room_number = " + roomNumber;
        db.executeQuery(sql1);
        db.executeQuery(sql2);
        db.disconnect();
    }

    @Override
    public int getLatestLogId(String roomNumber) {
        db.connect();
        int logId = 0;
        String sql = "SELECT MAX(log_id) AS MAX FROM Hotel_log WHERE log_room = " + roomNumber;
        ArrayList<HashMap> log = db.queryRows(sql);
        for (HashMap l : log) {
            try {
                logId = Integer.parseInt((String) l.get("MAX"));
            } catch (NumberFormatException er) {

            }
        }
        db.disconnect();
        return logId;
    }

    @Override
    public void checkOut(int logId, String roomNumber) {
        db.connect();
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String formattedDate = sdf.format(date);

        String sql;
        sql = "UPDATE Hotel_log SET log_dateOut = '" + formattedDate + "' WHERE log_id = " + logId;
        db.executeQuery(sql);
        sql = "UPDATE Hotel_room SET room_onService = 0 WHERE room_number = " + roomNumber;
        db.executeQuery(sql);
        db.disconnect();
    }

    @Override
    public void printOutInvoice(int logId, int numberOfDay, double price) {
        String dateIn = "";
        String dateOut = "";
        String name = "";
        String customerId = "";
        String roomNumber = "";
        int day = numberOfDay;
        double total = price;

        db.connect();
        String sql = "SELECT * FROM Hotel_log WHERE log_id = " + logId;
        ArrayList<Log> logs = searchLogs(sql);
        for (Log log : logs) {
            dateIn = log.getDateIn();
            dateOut = log.getDateOut();
            customerId = log.getCustomerId();
            roomNumber = log.getRoom();
        }

        sql = "SELECT * FROM Hotel_customer WHERE customer_id = " + customerId;
        ArrayList<Customer> customers = searchCustomers(sql);
        for (Customer customer : customers) {
            name = customer.getName() + "  " + customer.getLastName();
        }

        db.disconnect();

        String printData
                = "\n"
                + "                                                                        Invoice\n\n"
                + "        Member ID : " + customerId + "\n"
                + "        Guest Name :  " + name + "\n\n"
                + "        __________________________________________________________________________\n"
                + "        Date-In                               Date-Out                           Room No.        Day               Total\n"
                + "        __________________________________________________________________________\n"
                + "        " + dateIn + "      " + dateOut + "      " + roomNumber + "                       " + day + "                  " + total + "\n";

        PrinterJob job = PrinterJob.getPrinterJob();
        job.setPrintable(new OutputPrinter(printData));
        boolean doPrint = job.printDialog();
        if (doPrint) {
            try {
                job.print();
            } catch (PrinterException err) {
                // Print job did not complete.
            }
        }
    }

    @Override
    public int getNumberOfDay(int logId) {
        int days = 0;
        db.connect();
        String sql = "SELECT DATEDIFF(log_dateOut,log_dateIn) AS DiffDate FROM Hotel_log WHERE log_id = " + logId;
        ArrayList<HashMap> day = db.queryRows(sql);
        for (HashMap d : day) {
            days = Integer.valueOf((String) d.get("DiffDate"));
            if (days == 0) {
                days = 1;
            }
        }
        db.disconnect();
        return days;
    }

    @Override
    public double calculatePrice(String roomNumber, int numberOfDay) {
        db.connect();
        double price = 0.0;
        String sql = "SELECT * FROM Hotel_room WHERE room_number = " + roomNumber;
        ArrayList<Room> rooms = searchRooms(sql);
        for (Room room : rooms) {
            int cost = Integer.valueOf(room.getPrice());
            price = numberOfDay * cost;
        }
        db.disconnect();
        return price;
    }

}
