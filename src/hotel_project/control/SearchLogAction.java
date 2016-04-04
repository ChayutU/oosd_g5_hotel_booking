package hotel_project.control;

import hotel_project.model.Log;
import hotel_project.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;


/**
 * Created by Nanny on 04/04/2016.
 */
public class SearchLogAction extends  AbstractAction {

    private JTable table;
    private DefaultTableModel tModel;
    private JComboBox boxSearch;
    private JTextField searchf;
    private JLabel lblSum;
    private Service service;


    public SearchLogAction(JTable table, DefaultTableModel tModel, JComboBox boxSearch, JTextField searchf, JLabel lblSum, Service service) {
        this.table = table;
        this.tModel = tModel;
        this.boxSearch = boxSearch;
        this.searchf = searchf;
        this.lblSum = lblSum;
        this.service = service;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int line = 0;
        String sql = "SELECT * FROM Hotel_log WHERE 1 ORDER BY log_id ASC";

        if (!searchf.getText().equals("")) {
            if (boxSearch.getSelectedItem().toString().equals("Member ID")) {
                sql = "SELECT * FROM Hotel_log WHERE customer_id = " + searchf.getText() + " ORDER BY log_id ASC";
            }

            if (boxSearch.getSelectedItem().toString().equals("Room No.")) {
                sql = "SELECT * FROM Hotel_log WHERE log_room = " + searchf.getText() + " ORDER BY log_id ASC";
            }
        }
        ArrayList<Log> log = service.searchLogs(sql);
        if (log.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No Results");
        }

        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
            line = 0;
        }


        for (Log l : log) {
            tModel.addRow(new Object[0]);
            tModel.setValueAt(l.getLogId(), line, 0);
            tModel.setValueAt(l.getDateIn(), line, 1);
            tModel.setValueAt(l.getDateOut(), line, 2);
            tModel.setValueAt(l.getRoom(), line, 3);
            tModel.setValueAt(l.getCustomerId(), line, 4);
            line = line + 1;
            lblSum.setText(String.valueOf(line));
        }


    }

}
