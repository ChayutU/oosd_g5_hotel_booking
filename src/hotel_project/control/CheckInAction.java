package hotel_project.control;

import hotel_project.model.Service;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import java.awt.event.ActionEvent;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;

/**
 * Created by Chayut on 31-Mar-16.
 */
public class CheckInAction extends AbstractAction {

    private JTable table;
    private JButton btnSearch;
    private Service service;

    public CheckInAction(JTable table, JButton btnSearch, Service service) {
        this.table = table;
        this.btnSearch = btnSearch;
        this.service = service;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        for (int i = 0; i < table.getRowCount(); i++) {
            Boolean isSelect = Boolean.valueOf(table.getValueAt(i, 0).toString());
            String roomNumber = table.getValueAt(i, 1).toString();
            if (isSelect) {
                String id = JOptionPane.showInputDialog(null, "Confirm check in room number " + roomNumber + "\nPlease input Member ID",
                        "Check-In", QUESTION_MESSAGE);
                if (service.validateCustomer(Integer.valueOf(id))) {
                    if (service.onService(Integer.valueOf(roomNumber))) {

                        service.checkIn(id, roomNumber);
                    }
                }
            }
        }
        btnSearch.doClick();
    }
}
