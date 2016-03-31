package hotel_project.control;

import hotel_project.model.Book;
import hotel_project.model.Room;
import hotel_project.model.Service;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Chayut on 26-Mar-16.
 */
public class SearchRoomAction extends AbstractAction {

    private JTable table;
    private DefaultTableModel tModel;
    private JDatePickerImpl dateInPicker;
    private JDatePickerImpl dateOutPicker;
    private JSpinner personSpin;
    private JComboBox roomTypeBox;
    private Service service;


    public SearchRoomAction(JTable table, DefaultTableModel tModel, JDatePickerImpl dateInPicker, JDatePickerImpl dateOutPicker, JSpinner personSpin, JComboBox roomTypeBox, Service service) {
        this.table = table;
        this.tModel = tModel;
        this.dateInPicker = dateInPicker;
        this.dateOutPicker = dateOutPicker;
        this.personSpin = personSpin;
        this.roomTypeBox = roomTypeBox;
        this.service = service;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int line = 0;
        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
            line = 0;
        }

        String timeStampIn = service.datePickerFormat(dateInPicker) + " 12:00:00";
        String timeStampOut = service.datePickerFormat(dateOutPicker) + " 11:00:00";

        if (checkDate(dateInPicker, dateOutPicker)) {
            String sql = "SELECT * FROM Hotel_room WHERE room_type = \"" + roomTypeBox.getSelectedItem()
                    + "\" AND room_person >= " + personSpin.getValue() + " ORDER BY room_number ASC";
            if (roomTypeBox.getSelectedItem().equals("-")) {
                sql = "SELECT * FROM Hotel_room WHERE room_person >= " + personSpin.getValue() + " ORDER BY room_number ASC";
            }

            ArrayList<Room> matchedRooms = service.searchRooms(sql);

            for (Room room : matchedRooms) {
                String room_status = "Available";
                String byId = service.whoBook(room.getNumber(), timeStampIn, timeStampOut);

                if (!byId.equals("")) {
                    room_status = "On book by " + byId;
                }

                DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
                Date date = new Date();

                if (!room.onService() && timeStampIn.equals(dateFormat.format(date) + " 12:00:00")) {
                    room_status = "Not available";
                }

                tModel.addRow(new Object[0]);
                tModel.setValueAt(false, line, 0);
                tModel.setValueAt(room.getNumber(), line, 1);
                tModel.setValueAt(room.getType(), line, 2);
                tModel.setValueAt(room.getNumberOfPerson(), line, 3);
                tModel.setValueAt(room.getPrice(), line, 4);
                tModel.setValueAt(room_status, line, 5);
                line = line + 1;
            }

        } else {
            table.setModel(tModel);
        }
    }


    private boolean checkDate(JDatePickerImpl dateInPicker, JDatePickerImpl dateOutPicker) {
        if (Integer.valueOf(dateInPicker.getModel().getYear()) <= Integer.valueOf(dateOutPicker.getModel().getYear())) {
            if (Integer.valueOf(dateInPicker.getModel().getMonth()) <= Integer.valueOf(dateInPicker.getModel().getMonth())) {
                return Integer.valueOf(dateInPicker.getModel().getDay()) <= Integer.valueOf(dateInPicker.getModel().getDay());
            }
        }
        return false;
    }



}
