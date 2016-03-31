package hotel_project.control;

import hotel_project.model.Service;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.YES_NO_OPTION;

/**
 * Created by Chayu on 31-Mar-16.
 */
public class CancelBookingRoomAction extends AbstractAction {

    private JTable table;
    private JDatePickerImpl dateInPicker;
    private JDatePickerImpl dateOutPicker;
    private JButton btnSearch;
    private Service service;

    public CancelBookingRoomAction(JTable table, JDatePickerImpl dateInPicker, JDatePickerImpl dateOutPicker, JButton btnSearch, Service service) {
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

        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean isSelect = Boolean.valueOf(table.getValueAt(i, 0).toString());
            String roomNo = table.getValueAt(i, 1).toString();
            if (isSelect) {
                int answer = JOptionPane.showConfirmDialog(null, "Confirm booking cancle.", "Cancle Booking", YES_NO_OPTION);
                if (answer == JOptionPane.YES_OPTION) {
                    String sql = "DELETE FROM Hotel_book WHERE (room_number = " + roomNo + " AND '" + timeStampIn + "'>= book_dateIn AND book_dateOut >= '" + timeStampOut + "')"
                            + " OR (room_number = " + roomNo + " AND '" + timeStampOut + "'>= book_dateIn AND book_dateOut >= '" + timeStampIn + "')";
                    service.cancelBook(sql);
                }
            }
        }
        btnSearch.doClick();
    }
}
