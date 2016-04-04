package hotel_project.view;

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

        JPanel nmemberPanel = new JPanel();
        nmemberPanel.setBackground(Color.decode("#c0c0c0"));
        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#c0c0c0"));

        nmemberPanel.setBorder(BorderFactory.createTitledBorder("New Member"));
        nmemberPanel.setPreferredSize(new Dimension(750, 100));
        nmemberPanel.setMaximumSize(new Dimension(750, 100));
        nmemberPanel.setBackground(Color.decode("#f1f0ea"));
        nmemberPanel.setForeground(Color.decode("#6e4627"));

        JLabel lblName = new JLabel("Name");
        JTextField nameText = new JTextField(15);

        JLabel lblLastname = new JLabel("Lastname");
        JTextField lnameText = new JTextField(15);

        JLabel lblsex = new JLabel("sex");
        String[] gSex = {"Male", "Female"};
        JComboBox sexBox = new JComboBox(gSex);

        JLabel Tel = new JLabel("Tel.");
        JTextField telText = new JTextField(11);

        JLabel lbladdress = new JLabel("Address");
        JTextField addressText = new JTextField(50);

        JButton submit = new JButton("submit");

        nmemberPanel.add(lblName);
        nmemberPanel.add(nameText);

        nmemberPanel.add(lblLastname);
        nmemberPanel.add(lnameText);

        nmemberPanel.add(lblsex);
        nmemberPanel.add(sexBox);

        nmemberPanel.add(Tel);
        nmemberPanel.add(telText);

        nmemberPanel.add(lbladdress);
        nmemberPanel.add(addressText);

        nmemberPanel.add(submit);

        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Member"));
        searchPanel.setForeground(new Color(240, 250, 240));
        searchPanel.setMaximumSize(new Dimension(750, 65));
        searchPanel.setBackground(Color.decode("#f1f0ea"));

        JLabel lblSearch = new JLabel("Search Member");

        String search[] = {"ID", "Name or Last Name", "Tel."};
        JComboBox boxSearch = new JComboBox(search);

        JTextField searchf = new JTextField(15);

        searchPanel.add(lblSearch);
        searchPanel.add(boxSearch);
        searchPanel.add(searchf);

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        JTable table = new JTable();
        scrollPane.setViewportView(table);

        DefaultTableModel tModel;
        tModel = new DefaultTableModel() {

            boolean[] canEdit = new boolean[]{
                    true, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    default:
                        return String.class;
                }
            }
        };

        table.setModel(tModel);

        tModel.addColumn("ID");
        tModel.addColumn("Name");
        tModel.addColumn("Lastname");
        tModel.addColumn("Sex");
        tModel.addColumn("Tel");
        tModel.addColumn("Address");

        JButton btnSearch = new JButton(" Search ");

        searchPanel.add(btnSearch);

        add(Box.createRigidArea(new Dimension(0,10)));
        add(nmemberPanel, BorderLayout.NORTH);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(searchPanel, BorderLayout.CENTER);
        add(Box.createRigidArea(new Dimension(0,10)));
        add(scrollPane, BorderLayout.SOUTH);
    }
}
