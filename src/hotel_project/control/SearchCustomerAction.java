package hotel_project.control;

import hotel_project.model.Customer;
import hotel_project.model.Service;
import org.jdatepicker.impl.JDatePickerImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chayut on 19-Apr-16.
 */
public class SearchCustomerAction extends AbstractAction {

    private DefaultTableModel tModel;
    private JTextField searchField;
    private JComboBox boxSearchBy;
    private Service service;

    public SearchCustomerAction(DefaultTableModel tModel, JTextField searchField, JComboBox boxSearchBy, Service service) {
        this.tModel = tModel;
        this.searchField = searchField;
        this.boxSearchBy = boxSearchBy;
        this.service = service;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        int line = 0;
        String sql = "";

        if (boxSearchBy.getSelectedItem().toString().equals("ID")) {
            sql = "SELECT * FROM Hotel_customer WHERE customer_id = " + searchField.getText();
        }

        if (boxSearchBy.getSelectedItem().toString().equals("Name or Last Name")) {
            sql = "SELECT * FROM Hotel_customer WHERE customer_name LIKE '%" + searchField.getText() + "%' OR customer_lastName LIKE '%" + searchField.getText() + "%'";
        }

        if (boxSearchBy.getSelectedItem().toString().equals("Tel.")) {
            sql = "SELECT * FROM Hotel_customer WHERE customer_tel = " + searchField.getText();
        }

        if (searchField.getText().equals("")) {
            sql = "SELECT * FROM Hotel_customer WHERE 1";
        }

        ArrayList<Customer> customers = service.searchCustomers(sql);

        if (customers.isEmpty() && !(searchField.getText().equals(""))) {
            JOptionPane.showMessageDialog(null, "No Results");
        }

        while (tModel.getRowCount() > 0) {
            tModel.removeRow(0);
            line = 0;
        }

        for (Customer customer : customers) {
            tModel.addRow(new Object[0]);
            tModel.setValueAt(customer.getId(), line, 0);
            tModel.setValueAt(customer.getName(), line, 1);
            tModel.setValueAt(customer.getLastName(), line, 2);
            tModel.setValueAt(customer.getSex(), line, 3);
            tModel.setValueAt(customer.getSex(), line, 4);
            tModel.setValueAt(customer.getAddress(), line, 5);
            line = line + 1;
        }

    }
}
