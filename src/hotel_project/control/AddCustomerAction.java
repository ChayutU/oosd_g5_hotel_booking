package hotel_project.control;

import hotel_project.model.Customer;
import hotel_project.model.Service;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Chayut on 19-Apr-16.
 */
public class AddCustomerAction extends AbstractAction {

    private JTextField nameTextField;
    private JTextField lastNameTextField;
    private JTextField telField;
    private JTextField addField;
    private JComboBox sexBox;
    String[] sex;
    private Service service;

    public AddCustomerAction(JTextField nameTextField, JTextField lastNameTextField, JTextField telField, JTextField addField, JComboBox sexBox, String[] sex, Service service) {
        this.nameTextField = nameTextField;
        this.lastNameTextField = lastNameTextField;
        this.telField = telField;
        this.addField = addField;
        this.sexBox = sexBox;
        this.sex = sex;
        this.service = service;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (nameTextField.getText().trim().equals("") || lastNameTextField.getText().trim().equals("") || telField.getText().trim().equals("") || addField.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(null, "Please fill all data.");
        } else {

            String validateTelNoSql = "SELECT * FROM Hotel_customer WHERE customer_tel = " + telField.getText();
            ArrayList<Customer> customerTelNo = service.searchCustomers(validateTelNoSql);

            if (customerTelNo.isEmpty()) {
                if (service.addCustomer(nameTextField, lastNameTextField, telField, addField, sexBox)) {
                    String sql = "SELECT * FROM Hotel_customer WHERE customer_tel = " + telField.getText();
                    ArrayList<Customer> customers = service.searchCustomers(sql);
                    String id = "";
                    for (Customer customer : customers) {
                        id = customer.getId();
                    }
                    JOptionPane.showMessageDialog(null, nameTextField.getText() + "  " + lastNameTextField.getText() + "\nNow member\nMember ID : " + id);
                    nameTextField.setText("");
                    lastNameTextField.setText("");
                    telField.setText("");
                    addField.setText("");
                    sexBox.setSelectedItem(sex[0]);
                }
            } else {
                String id = "";
                for (Customer tel : customerTelNo) {
                    id = tel.getId();
                }
                JOptionPane.showMessageDialog(null, "Already a Member \nMember ID : " + id);
            }
        }
    }
}
