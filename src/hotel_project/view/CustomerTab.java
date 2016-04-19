package hotel_project.view;

import hotel_project.control.AddCustomerAction;
import hotel_project.control.SearchCustomerAction;
import hotel_project.model.DbService;
import hotel_project.model.Service;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.*;

/**
 * Created by Chayut on 25-Mar-16.
 */
public class CustomerTab extends JPanel {
    private Service service = new DbService();

    public CustomerTab() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.decode("#976241"));

        JPanel memberPanel = new JPanel();
        memberPanel.setBackground(Color.decode("#c0c0c0"));
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#c0c0c0"));

        memberPanel.setBorder(BorderFactory.createTitledBorder("New Member"));
        memberPanel.setPreferredSize(new Dimension(750, 100));
        memberPanel.setMaximumSize(new Dimension(750, 100));
        memberPanel.setBackground(Color.decode("#f1f0ea"));
        memberPanel.setForeground(Color.decode("#6e4627"));

        JLabel lblName = new JLabel("Name");
        JTextField nameField = new JTextField(15);

        JLabel lblLastName = new JLabel("Lastname");
        JTextField lastNameField = new JTextField(15);

        JLabel lblSex = new JLabel("sex");
        String[] sex = {"Male", "Female"};
        JComboBox sexBox = new JComboBox(sex);

        JLabel Tel = new JLabel("Tel.");
        JTextField telField = new JTextField(11);

        JLabel lblAddress = new JLabel("Address");
        JTextField addressField = new JTextField(50);

        Action addCustomerAction = new AddCustomerAction(nameField, lastNameField, telField, addressField, sexBox, sex, service);
        addCustomerAction.putValue(Action.NAME, "Submit");
        JButton addCustomer = new JButton(addCustomerAction);

        memberPanel.add(lblName);
        memberPanel.add(nameField);

        memberPanel.add(lblLastName);
        memberPanel.add(lastNameField);

        memberPanel.add(lblSex);
        memberPanel.add(sexBox);

        memberPanel.add(Tel);
        memberPanel.add(telField);

        memberPanel.add(lblAddress);
        memberPanel.add(addressField);

        memberPanel.add(addCustomer);

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Member"));
        searchPanel.setForeground(new Color(240, 250, 240));
        searchPanel.setMaximumSize(new Dimension(750, 65));
        searchPanel.setBackground(Color.decode("#f1f0ea"));

        JLabel lblSearch = new JLabel("Search Member");

        String search[] = {"ID", "Name or Last Name", "Tel."};
        JComboBox boxSearch = new JComboBox(search);

        JTextField searchField = new JTextField(15);

        searchPanel.add(lblSearch);
        searchPanel.add(boxSearch);
        searchPanel.add(searchField);

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JTable table = new JTable();
        scrollPane.setViewportView(table);

        DefaultTableModel tModel;
        tModel = new DefaultTableModel() {

            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        table.setModel(tModel);

        tModel.addColumn("ID");
        tModel.addColumn("Name");
        tModel.addColumn("Lastname");
        tModel.addColumn("Sex");
        tModel.addColumn("Tel");
        tModel.addColumn("Address");

        Action searchCustomerAction = new SearchCustomerAction(tModel, searchField, boxSearch, service);
        searchCustomerAction.putValue(Action.NAME, " Search ");
        JButton btnSearch = new JButton(searchCustomerAction);

        searchPanel.add(btnSearch);

        add(Box.createRigidArea(new Dimension(0,10)));
        add(memberPanel, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(searchPanel, BorderLayout.CENTER);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(scrollPane, BorderLayout.SOUTH);
    }
}
