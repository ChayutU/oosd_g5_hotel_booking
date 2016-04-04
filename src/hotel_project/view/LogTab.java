package hotel_project.view;

import hotel_project.model.DbService;
import hotel_project.model.Service;
import hotel_project.control.SearchLogAction;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.BoxLayout;
import java.awt.*;

import javax.swing.*;

/**
 * Created by Chayut on 25-Mar-16.
 */
public class LogTab extends JPanel {
    private Service service = new DbService();

    public LogTab() {

        setLayout(new BorderLayout(5, 10));
        setBackground(Color.decode("#976241"));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#c0c0c0"));

        JLabel lblSearch = new JLabel("Search By");
        String search[] = {"Member ID", "Room No."};
        JComboBox boxSearch = new JComboBox(search);

        JTextField searchf = new JTextField(15);

        searchPanel.add(lblSearch);
        searchPanel.add(boxSearch);
        searchPanel.add(searchf);


        JPanel totalPanel = new JPanel();
        totalPanel.setBackground(Color.decode("#f1f0ea"));

        JLabel lblTotal = new JLabel("TOTAL : ");
        lblTotal.setForeground(Color.decode("#6e4627"));
        JLabel lblSum = new JLabel("......");
        lblSum.setForeground(Color.decode("#6e4627"));

        totalPanel.add(lblTotal);
        totalPanel.add(lblSum);

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(700, 350));

        JTable table = new JTable();
        scrollPane.setViewportView(table);

        DefaultTableModel tModel;
        tModel = new DefaultTableModel() {

            boolean[] canEdit = new boolean[]{
                    false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        };

        table.setModel(tModel);

        tModel.addColumn("Log No.");
        tModel.addColumn("Check In");
        tModel.addColumn("Check Out");
        tModel.addColumn("Room No.");
        tModel.addColumn("Member ID");

        Action searchLogAction = new SearchLogAction(table, tModel, boxSearch, searchf, lblSum, service);
        searchLogAction.putValue(Action.NAME, "Search");
        JButton btnSearch = new JButton(searchLogAction);

        searchPanel.add(btnSearch);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(totalPanel, BorderLayout.SOUTH);
    }
}
