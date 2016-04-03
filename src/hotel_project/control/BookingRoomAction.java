package hotel_project.control;

import hotel_project.MainMenu;
import hotel_project.model.Customer;
import hotel_project.model.Service;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * Created by Chayut on 31-Mar-16.
 */
public class BookingRoomAction extends AbstractAction {

    private JTable table;
    private JDatePickerImpl dateInPicker;
    private JDatePickerImpl dateOutPicker;
    private JButton btnSearch;
    private Service service;

    public BookingRoomAction(JTable table, JDatePickerImpl dateInPicker, JDatePickerImpl dateOutPicker, JButton btnSearch, Service service) {
        this.table = table;
        this.dateInPicker = dateInPicker;
        this.dateOutPicker = dateOutPicker;
        this.btnSearch = btnSearch;
        this.service = service;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String timeStampIn = service.datePickerFormat(dateInPicker);

        String timeStampOut = service.datePickerFormat(dateOutPicker);

        if (!timeStampIn.equals(timeStampOut)) {
            for (int i = 0; i < table.getRowCount(); i++) {
                Boolean isSelect = Boolean.valueOf(table.getValueAt(i, 0).toString());
                String roomNo = table.getValueAt(i, 1).toString();
                if (isSelect) {
                    String id = JOptionPane.showInputDialog(null, "Confirm booking room number " + roomNo + " on " + timeStampIn + " to " + timeStampOut + "\nPlease input Member ID",
                            "Confirm booking", QUESTION_MESSAGE);
                    if (service.validateCustomer(Integer.valueOf(id))) {
                        String sql = "INSERT INTO Hotel_book(book_dateIn, book_dateOut, book_id, customer_id, room_number) "
                                + "VALUES ('" + timeStampIn + " 12:00:00" + "','" + timeStampOut + " 11:00:00" + "','" + service.bookIdCreator() + "','" + id + "','" + roomNo + "')";
                        if (service.whoBook(roomNo, timeStampIn, timeStampOut).equals("")) {
                            service.insertBook(sql);
                        }
                    }
                }
            }
            btnSearch.doClick();
        } else {
            JOptionPane.showMessageDialog(null, "Invalid Date");
        }

    }

}
