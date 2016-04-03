package hotel_project.control;

import hotel_project.model.Room;
import hotel_project.model.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import static javax.swing.JOptionPane.YES_NO_OPTION;
import static javax.swing.JOptionPane.YES_OPTION;

/**
 * Created by Chayut on 03-Apr-16.
 */
public class CheckOutAction extends AbstractAction {

    private JTable table;
    private JButton btnSearch;
    private Service service;

    public CheckOutAction(JTable table, JButton btnSearch, Service service) {
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
                int answer = JOptionPane.showConfirmDialog(null, "Confirm Check-Out.", "Check-Out", YES_NO_OPTION);
                if (answer == YES_OPTION) {
                    if (!service.onService(Integer.valueOf(roomNumber))) {

                        int logId = service.getLatestLogId(roomNumber);

                        service.checkOut(logId, roomNumber);



                        int numberOfDay = service.getNumberOfDay(logId);

                        double price = service.calculatePrice(roomNumber, numberOfDay);

                        //Print invoice here
                        service.printOutInvoice(logId, numberOfDay, price);
                    }
                }
            }
        }
        btnSearch.doClick();
    }
}
