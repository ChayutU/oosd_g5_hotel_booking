package hotel_project;

import edu.sit.cs.db.CSDbDelegate;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;

import hotel_project.model.DateLabelFormatter;
import hotel_project.model.OutputPrinter;
import org.jdatepicker.impl.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 *
 * @author Chayut
 */
public class MainMenu extends JFrame {

    private JPanel room_search;
    private JPanel customer;
    private JPanel log;

    CSDbDelegate db = new CSDbDelegate("csprog-in.sit.kmutt.ac.th", "3306", "CSC105_G4", "csc105_2014", "csc105");

    public MainMenu() {
        db.connect();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hotel");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#708090"));
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        roomSearchTab();
        customerTab();
        logTab();

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Room Search", room_search);
        tabPane.addTab("Customer", customer);
        tabPane.addTab("    Log    ", log);
        mainPanel.add(tabPane);
    }

    public int idCreator() {
        int tmp = 0;
        String sql = "SELECT MAX(customer_id) AS MAX FROM Hotel_customer";
        ArrayList<HashMap> id = db.queryRows(sql);
        for (HashMap ids : id) {
            try {
                tmp = Integer.parseInt((String) ids.get("MAX"));
            } catch (NumberFormatException e) {

            }
        }
        return tmp + 1;
    }

    public int bookIdCreator() {
        int tmp = 0;
        String sql = "SELECT MAX(book_id) AS MAX FROM Hotel_book";
        ArrayList<HashMap> id = db.queryRows(sql);
        for (HashMap ids : id) {
            try {
                tmp = Integer.parseInt((String) ids.get("MAX"));
            } catch (NumberFormatException e) {

            }
        }
        return tmp + 1;
    }

    public boolean onService(int room) {
        String sql = "SELECT room_onService FROM Hotel_room WHERE room_number = " + room;
        ArrayList<HashMap> service = db.queryRows(sql);
        for (HashMap s : service) {
            if (Integer.valueOf((String) s.get("room_onService")) == 1) {
                return false;
            }
        }
        return true;
    }

    public String bookBy(String room, String timeIn, String timeOut) {
        String sql = "SELECT * FROM Hotel_book WHERE (room_number = " + room + " AND '" + timeIn + "'>= book_dateIn AND book_dateOut >= '" + timeOut + "')"
                + " OR (room_number = " + room + " AND '" + timeOut + "'>= book_dateIn AND book_dateOut >= '" + timeIn + "')";
        ArrayList<HashMap> date = db.queryRows(sql);
        for (HashMap d : date) {
            if (!date.isEmpty()) {
                return (String) d.get("customer_id");
            }
        }
        return "";
    }

    public boolean memberIdChecker(int id) {
        ArrayList<HashMap> member = db.queryRows("SELECT customer_id FROM Hotel_customer WHERE customer_id = " + id);
        if (!member.isEmpty()) {
            return true;
        }
        JOptionPane.showMessageDialog(null, "Invalid Member ID");
        return false;
    }

    public String[] getRoomType() {
        String sql = "SELECT room_type FROM Hotel_room WHERE 1 GROUP BY room_type ORDER BY room_cost ASC";
        ArrayList<HashMap> rType = db.queryRows(sql);
        String[] type = new String[rType.size()+1];
        type[0] = "-";
        int i = 1;
        for (HashMap r : rType) {
            type[i++] = (String) r.get("room_type");
        }
        return type;
    }

    private boolean checkDate(String yyyyIn, String mmIn, String ddIn, String yyyyOut, String mmOut, String ddOut) {
        if (Integer.valueOf(yyyyIn) <= Integer.valueOf(yyyyOut)) {
            if (Integer.valueOf(mmIn) <= Integer.valueOf(mmOut)) {
                return Integer.valueOf(ddIn) <= Integer.valueOf(ddOut);
            }
        }
        return false;
    }

    private void roomSearchTab() {
        room_search = new JPanel();
        room_search.setLayout(new BorderLayout(5, 10));
        room_search.setBackground(Color.decode("#976241"));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#c0c0c0"));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.decode("#c0c0c0"));

        JLabel lblPerson = new JLabel("# of Person", JLabel.RIGHT);
        JSpinner personSpin = new JSpinner(new SpinnerNumberModel(0, 0, 16, 1));
        JLabel lblRoomType = new JLabel("Room type", JLabel.RIGHT);
        JComboBox roomTypeBox = new JComboBox(getRoomType());
        JLabel lblCheckInDate = new JLabel("Check in date", JLabel.RIGHT);

        /**
         * JDatePicker :: http://sourceforge.net/projects/jdatepicker/
         * How to :: http://stackoverflow.com/questions/26794698/how-do-i-implement-jdatepicker
         */
        Properties p = new Properties();
        p.put("text.today", "Today");
        p.put("text.month", "Month");
        p.put("text.year", "Year");

        UtilDateModel dateInModel = new UtilDateModel();
        JDatePanelImpl dateInPanel = new JDatePanelImpl(dateInModel, p);
        JDatePickerImpl dateInPicker = new JDatePickerImpl(dateInPanel, new DateLabelFormatter());

        JLabel lblCheckOutDate = new JLabel("Check Out date", JLabel.RIGHT);

        UtilDateModel dateOutModel = new UtilDateModel();
        JDatePanelImpl dateOutPanel = new JDatePanelImpl(dateOutModel, p);
        JDatePickerImpl dateOutPicker = new JDatePickerImpl(dateOutPanel, new DateLabelFormatter());

        JButton btnSearch = new JButton("Search");

        searchPanel.setLayout(new GridLayout(1, 9, 8, 0));

        searchPanel.add(lblPerson);
        searchPanel.add(personSpin);

        searchPanel.add(lblRoomType);
        searchPanel.add(roomTypeBox);

        searchPanel.add(lblCheckInDate);
        searchPanel.add(dateInPicker);

        searchPanel.add(lblCheckOutDate);
        searchPanel.add(dateOutPicker);

        searchPanel.add(btnSearch);

        JScrollPane scrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setPreferredSize(new Dimension(700, 400));

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

        tModel.addColumn("Select");
        tModel.addColumn("Room No.");
        tModel.addColumn("Type");
        tModel.addColumn("Person");
        tModel.addColumn("Price");
        tModel.addColumn("Status");

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                int line = 0;
                while (tModel.getRowCount() > 0) {
                    tModel.removeRow(0);
                    line = 0;
                }

                String ddIn = "" + dateInPicker.getModel().getDay();
                String mmIn = "" + (dateInPicker.getModel().getMonth() + 1);
                String yyyyIn = "" + dateInPicker.getModel().getYear();
                String timeStampIn = yyyyIn + "-" + mmIn + "-" + ddIn + " 12:00:00";

                String ddOut = "" + dateOutPicker.getModel().getDay();
                String mmOut = "" + (dateOutPicker.getModel().getMonth() + 1);
                String yyyyOut = "" + dateOutPicker.getModel().getYear();
                String timeStampOut = yyyyOut + "-" + mmOut + "-" + ddOut + " 11:00:00";

                if (checkDate(yyyyIn, mmIn, ddIn, yyyyOut, mmOut, ddOut)) {
                    String sql = "SELECT * FROM Hotel_room WHERE room_type = \"" + roomTypeBox.getSelectedItem()
                            + "\" AND room_person >= " + personSpin.getValue() + " ORDER BY room_number ASC";
                    if (roomTypeBox.getSelectedItem().equals("-")) {
                        sql = "SELECT * FROM Hotel_room WHERE room_person >= " + personSpin.getValue() + " ORDER BY room_number ASC";
                    }
                    ArrayList<HashMap> room = db.queryRows(sql);
                    for (HashMap r : room) {

                        String room_status = "Available";
                        String byId = MainMenu.this.bookBy((String) r.get("room_number"), timeStampIn, timeStampOut);

                        if (!byId.equals("")) {
                            room_status = "On book by " + byId;
                        }
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-M-d");
                        Date date = new Date();
                        if (!MainMenu.this.onService(Integer.valueOf("" + r.get("room_number"))) && timeStampIn.equals(dateFormat.format(date) + " 12:00:00")) {
                            room_status = "Not available";
                        }

                        tModel.addRow(new Object[0]);
                        tModel.setValueAt(false, line, 0);
                        tModel.setValueAt(r.get("room_number"), line, 1);
                        tModel.setValueAt(r.get("room_type"), line, 2);
                        tModel.setValueAt(r.get("room_person"), line, 3);
                        tModel.setValueAt(r.get("room_cost"), line, 4);
                        tModel.setValueAt(room_status, line, 5);
                        line = line + 1;
                    }
                } else {
                    table.setModel(tModel);
                }
                db.disconnect();
            }
        });

        JButton btnBook = new JButton("Booking");
        btnBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                String ddIn = "" + dateInPicker.getModel().getDay();
                String mmIn = "" + (dateInPicker.getModel().getMonth() + 1);
                String yyyyIn = "" + dateInPicker.getModel().getYear();
                String timeStampIn = yyyyIn + "-" + mmIn + "-" + ddIn;

                String ddOut = "" + dateOutPicker.getModel().getDay();
                String mmOut = "" + (dateOutPicker.getModel().getMonth() + 1);
                String yyyyOut = "" + dateOutPicker.getModel().getYear();
                String timeStampOut = yyyyOut + "-" + mmOut + "-" + ddOut;

                if (!timeStampIn.equals(timeStampOut)) {
                    for (int i = 0; i < table.getRowCount(); i++) {
                        Boolean chked = Boolean.valueOf(table.getValueAt(i, 0).toString());
                        String roomNo = table.getValueAt(i, 1).toString();
                        if (chked) {
                            String id = JOptionPane.showInputDialog(null, "Confirm booking room number " + roomNo + " on " + timeStampIn + " to " + timeStampOut + "\nPlease input Member ID",
                                    "Confirm booking", QUESTION_MESSAGE);
                            if (MainMenu.this.memberIdChecker(Integer.valueOf(id))) {
                                String sql = "INSERT INTO Hotel_book(book_dateIn, book_dateOut, book_id, customer_id, room_number) "
                                        + "VALUES ('" + timeStampIn + " 12:00:00" + "','" + timeStampOut + " 11:00:00" + "','" + MainMenu.this.bookIdCreator() + "','" + id + "','" + roomNo + "')";
                                if (MainMenu.this.bookBy(roomNo, timeStampIn, timeStampOut).equals("")) {
                                    db.executeQuery(sql);
                                }
                            }
                        }
                    }
                    btnSearch.doClick();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid Date");
                }
                db.disconnect();
            }
        });

        JButton btnCBook = new JButton("Cancel Booking");
        btnCBook.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                String ddIn = "" + dateInPicker.getModel().getDay();
                String mmIn = "" + (dateInPicker.getModel().getMonth() + 1);
                String yyyyIn = "" + dateInPicker.getModel().getYear();
                String timeStampIn = yyyyIn + "-" + mmIn + "-" + ddIn;

                String ddOut = "" + dateOutPicker.getModel().getDay();
                String mmOut = "" + (dateOutPicker.getModel().getMonth() + 1);
                String yyyyOut = "" + dateOutPicker.getModel().getYear();
                String timeStampOut = yyyyOut + "-" + mmOut + "-" + ddOut;

                for (int i = 0; i < table.getRowCount(); i++) {
                    Boolean chked = Boolean.valueOf(table.getValueAt(i, 0).toString());
                    String roomNo = table.getValueAt(i, 1).toString();
                    if (chked) {
                        int answer = JOptionPane.showConfirmDialog(null, "Confirm booking cancle.", "Cancle Booking", YES_NO_OPTION);
                        if (answer == JOptionPane.YES_OPTION) {
                            String sql = "DELETE FROM Hotel_book WHERE (room_number = " + roomNo + " AND '" + timeStampIn + "'>= book_dateIn AND book_dateOut >= '" + timeStampOut + "')"
                                    + " OR (room_number = " + roomNo + " AND '" + timeStampOut + "'>= book_dateIn AND book_dateOut >= '" + timeStampIn + "')";
                            db.executeQuery(sql);
                        }
                    }
                }
                btnSearch.doClick();
                db.disconnect();
            }
        });

        JButton btnCIn = new JButton("Check-In");
        btnCIn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                for (int i = 0; i < table.getRowCount(); i++) {
                    Boolean chked = Boolean.valueOf(table.getValueAt(i, 0).toString());
                    String roomNo = table.getValueAt(i, 1).toString();
                    if (chked) {
                        String id = JOptionPane.showInputDialog(null, "Confirm check in room number " + roomNo + "\nPlease input Member ID",
                                "Check-In", QUESTION_MESSAGE);
                        if (MainMenu.this.memberIdChecker(Integer.valueOf(id))) {
                            if (MainMenu.this.onService(Integer.valueOf(roomNo))) {
                                String sql = "INSERT INTO Hotel_log(customer_id, log_room) "
                                        + "VALUES (" + id + ", " + roomNo + ")";
                                String sql2 = "UPDATE Hotel_room SET room_onService = 1 WHERE room_number = " + roomNo;
                                db.executeQuery(sql);
                                db.executeQuery(sql2);
                            }
                        }
                    }
                }
                btnSearch.doClick();
                db.disconnect();
            }
        });

        JButton btnCOut = new JButton("Check-Out");
        btnCOut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                for (int i = 0; i < table.getRowCount(); i++) {
                    Boolean chked = Boolean.valueOf(table.getValueAt(i, 0).toString());
                    String roomNo = table.getValueAt(i, 1).toString();
                    if (chked) {
                        int logId = 0;
                        int answer = JOptionPane.showConfirmDialog(null, "Confirm Check-Out.", "Check-Out", YES_NO_OPTION);
                        if (answer == JOptionPane.YES_OPTION) {
                            if (!MainMenu.this.onService(Integer.valueOf(roomNo))) {
                                String sql = "SELECT MAX(log_id) AS MAX FROM Hotel_log WHERE log_room = " + roomNo;
                                ArrayList<HashMap> log = db.queryRows(sql);
                                for (HashMap l : log) {
                                    try {
                                        logId = Integer.parseInt((String) l.get("MAX"));
                                    } catch (NumberFormatException er) {

                                    }
                                }
                                Timestamp time = new Timestamp(System.currentTimeMillis());
                                String t = time + "";
                                t = t.substring(0, t.length() - 4);
                                sql = "UPDATE Hotel_log SET log_dateOut = '" + t + "' WHERE log_id = " + logId;
                                db.executeQuery(sql);
                                sql = "UPDATE Hotel_room SET room_onService = 0 WHERE room_number = " + roomNo;
                                db.executeQuery(sql);

                                //Print Invoice
                                String dateIn = "", dateOut = "", name = "";
                                int customerId = 0;
                                int day = 0;
                                double total = 0.0;

                                sql = "SELECT * FROM Hotel_log WHERE log_id = " + logId;
                                ArrayList<HashMap> bill = db.queryRows(sql);
                                for (HashMap b : bill) {
                                    dateIn = (String) b.get("log_dateIn");
                                    dateOut = (String) b.get("log_dateOut");
                                    customerId = Integer.valueOf("" + b.get("customer_id"));
                                }

                                sql = "SELECT * FROM Hotel_customer WHERE customer_id = " + customerId;
                                ArrayList<HashMap> member = db.queryRows(sql);
                                for (HashMap m : member) {
                                    name = m.get("customer_name") + "  " + m.get("customer_lastName");
                                }
                                sql = "SELECT DATEDIFF(log_dateOut,log_dateIn) AS DiffDate FROM Hotel_log WHERE log_id = " + logId;
                                ArrayList<HashMap> days = db.queryRows(sql);
                                for (HashMap d : days) {
                                    day = Integer.valueOf("" + d.get("DiffDate"));
                                    if (day == 0) {
                                        day = 1;
                                    }
                                }

                                sql = "SELECT * FROM Hotel_room WHERE room_number = " + roomNo;
                                ArrayList<HashMap> price = db.queryRows(sql);
                                for (HashMap pp : price) {
                                    int cost = Integer.valueOf("" + pp.get("room_cost"));
                                    total = day * cost;
                                }

                                String printData
                                        = "\n"
                                        + "                                                                        Invoice\n\n"
                                        + "        Member ID : " + customerId + "\n"
                                        + "        Guest Name :  " + name + "\n\n"
                                        + "        __________________________________________________________________________\n"
                                        + "        Date-In                               Date-Out                           Room No.        Day               Total\n"
                                        + "        __________________________________________________________________________\n"
                                        + "        " + dateIn + "      " + dateOut + "      " + roomNo + "                       " + day + "                  " + total + "\n";

                                PrinterJob job = PrinterJob.getPrinterJob();
                                job.setPrintable(new OutputPrinter(printData));
                                boolean doPrint = job.printDialog();
                                if (doPrint) {
                                    try {
                                        job.print();
                                    } catch (PrinterException err) {
                                        // Print job did not complete.
                                    }
                                }
                            }
                        }
                    }
                }
                btnSearch.doClick();
                db.disconnect();
            }
        });

        JPanel Book = new JPanel();
        Book.setBackground(Color.decode("#f1f0ea"));
        Book.setBorder(BorderFactory.createTitledBorder("Book"));

        Book.add(btnBook);
        Book.add(btnCBook);

        JPanel Check = new JPanel();
        Check.setBackground(Color.decode("#f1f0ea"));
        Check.setBorder(BorderFactory.createTitledBorder("Check In & Out"));

        Check.add(btnCIn);
        Check.add(btnCOut);

        bottomPanel.add(Book);
        bottomPanel.add(Check);

        room_search.add(searchPanel, BorderLayout.NORTH);
        room_search.add(scrollPane, BorderLayout.CENTER);
        room_search.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void customerTab() {
        customer = new JPanel();

        customer.setLayout(new BoxLayout(customer, BoxLayout.Y_AXIS));
        customer.setBackground(Color.decode("#c0c0c0"));
        

        JPanel searchPanel = new JPanel();
        
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search Member"));
        searchPanel.setForeground(new Color(240, 250, 240));

        searchPanel.setMaximumSize(new Dimension(500, 120));
        searchPanel.setBackground(Color.decode("#f1f0ea"));

        JLabel lblSearch = new JLabel("Search Member");
        searchPanel.add(lblSearch);

        String search[] = {"ID", "Name or Last Name", "Tel."};
        JComboBox boxSearchBy = new JComboBox(search);
        searchPanel.add(boxSearchBy);

        JTextField searchField = new JTextField(null, 8);
        searchPanel.add(searchField);

        JButton btnSearch = new JButton(" Search ");

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
        tModel.addColumn("Last Name");
        tModel.addColumn("Sex");
        tModel.addColumn("Telephone Number");
        tModel.addColumn("Address");

        // Search Member
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
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

                ArrayList<HashMap> cus = db.queryRows(sql);

                if (cus.isEmpty() && !(searchField.getText().equals(""))) {
                    JOptionPane.showMessageDialog(null, "No Results");
                }

                while (tModel.getRowCount() > 0) {
                    tModel.removeRow(0);
                    line = 0;
                }

                for (HashMap c : cus) {
                    tModel.addRow(new Object[0]);
                    tModel.setValueAt(c.get("customer_id"), line, 0);
                    tModel.setValueAt(c.get("customer_name"), line, 1);
                    tModel.setValueAt(c.get("customer_lastName"), line, 2);
                    tModel.setValueAt(c.get("customer_sex"), line, 3);
                    tModel.setValueAt(c.get("customer_tel"), line, 4);
                    tModel.setValueAt(c.get("customer_add"), line, 5);
                    line = line + 1;
                }
                db.disconnect();
            }
        });
        searchPanel.add(btnSearch);
        customer.add(searchPanel);

        JPanel newMemberPanel = new JPanel();
        newMemberPanel.setBorder(BorderFactory.createTitledBorder("New Member"));
        newMemberPanel.setPreferredSize(new Dimension(750, 100));
        newMemberPanel.setMaximumSize(new Dimension(750, 100));
        newMemberPanel.setBackground(Color.decode("#f1f0ea"));
        newMemberPanel.setForeground(Color.decode("#6e4627"));

        JLabel lblName = new JLabel("Name");
        newMemberPanel.add(lblName);

        JTextField nameTextField = new JTextField(null, 11);
        newMemberPanel.add(nameTextField);

        JLabel lblLastName = new JLabel("Last name");
        newMemberPanel.add(lblLastName);

        JTextField lastNameTextField = new JTextField(null, 11);
        newMemberPanel.add(lastNameTextField);

        JLabel lblSex = new JLabel("Sex");
        newMemberPanel.add(lblSex);

        String[] sex = {"Male", "Female"};
        JComboBox sexBox = new JComboBox(sex);
        newMemberPanel.add(sexBox);

        JLabel lblTel = new JLabel("Telephone number");
        newMemberPanel.add(lblTel);

        JTextField telField = new JTextField(null, 10);
        newMemberPanel.add(telField);

        JLabel lblAdd = new JLabel("Address");
        newMemberPanel.add(lblAdd);

        JTextField addField = new JTextField(null, 30);
        newMemberPanel.add(addField);

        JButton btnSubmit = new JButton(" Submit ");

        // Add new member  & Gen Member ID
        btnSubmit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                if (nameTextField.getText().trim().equals("") || lastNameTextField.getText().trim().equals("") || telField.getText().trim().equals("") || addField.getText().trim().equals("")) {
                    JOptionPane.showMessageDialog(null, "Please fill all data.");
                } else {

                    String addMember = "INSERT INTO Hotel_customer(customer_name, customer_lastName, customer_sex, customer_tel, customer_add, customer_id)"
                            + "VALUES(" + "'" + nameTextField.getText() + "'" + "," + "'" + lastNameTextField.getText() + "'" + "," + "'" + sexBox.getSelectedItem().toString() + "'"
                            + "," + "'" + telField.getText() + "'" + "," + "'" + addField.getText() + "'" + "," + "'" + MainMenu.this.idCreator() + "'" + ")";
                    ArrayList<HashMap> tel = db.queryRows("SELECT * FROM Hotel_customer WHERE customer_tel = " + telField.getText());
                    if (tel.isEmpty()) {
                        if (db.executeQuery(addMember)) {
                            String sql = "SELECT * FROM Hotel_customer WHERE customer_tel = " + telField.getText();
                            ArrayList<HashMap> cus = db.queryRows(sql);
                            String id = "";
                            for (HashMap c : cus) {
                                id = "" + c.get("customer_id");
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
                        for (HashMap t : tel) {
                            id = "" + t.get("customer_id");
                        }
                        JOptionPane.showMessageDialog(null, "Already a Member \nMember ID : " + id);
                    }
                }
                db.disconnect();
            }
        });
        newMemberPanel.add(btnSubmit);
        customer.add(Box.createRigidArea(new Dimension(0,10)));
        customer.add(newMemberPanel);
        customer.add(Box.createRigidArea(new Dimension(0, 10)));
        customer.add(scrollPane);
    }

    private void logTab() {
        log = new JPanel();
        log.setLayout(new BorderLayout(5, 10));
        log.setBackground(Color.decode("#976241"));

        JPanel searchPanel = new JPanel();
        searchPanel.setBackground(Color.decode("#c0c0c0"));

        JLabel lblSearch = new JLabel("Search Log by");
        String search[] = {"Member ID", "Room No."};
        JComboBox boxSearchBy = new JComboBox(search);
        JTextField searchField = new JTextField(null, 8);
        JButton btnSearch = new JButton(" Search ");

        searchPanel.add(lblSearch);
        searchPanel.add(boxSearchBy);
        searchPanel.add(searchField);
        searchPanel.add(btnSearch);

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

        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                db.connect();
                int line = 0;
                String sql = "SELECT * FROM Hotel_log WHERE 1 ORDER BY log_id ASC";

                if (!searchField.getText().equals("")) {
                    if (boxSearchBy.getSelectedItem().toString().equals("Member ID")) {
                        sql = "SELECT * FROM Hotel_log WHERE customer_id = " + searchField.getText() + " ORDER BY log_id ASC";
                    }

                    if (boxSearchBy.getSelectedItem().toString().equals("Room No.")) {
                        sql = "SELECT * FROM Hotel_log WHERE log_room = " + searchField.getText() + " ORDER BY log_id ASC";
                    }
                }
                ArrayList<HashMap> log = db.queryRows(sql);
                if (log.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "No Results");
                }

                while (tModel.getRowCount() > 0) {
                    tModel.removeRow(0);
                    line = 0;
                }

                for (HashMap l : log) {
                    tModel.addRow(new Object[0]);
                    tModel.setValueAt(l.get("log_id"), line, 0);
                    tModel.setValueAt(l.get("log_dateIn"), line, 1);
                    tModel.setValueAt(l.get("log_dateOut"), line, 2);
                    tModel.setValueAt(l.get("log_room"), line, 3);
                    tModel.setValueAt(l.get("customer_id"), line, 4);
                    line = line + 1;
                    lblSum.setText(String.valueOf(line));
                }
                db.disconnect();
            }
        });

        log.add(searchPanel, BorderLayout.NORTH);
        log.add(scrollPane, BorderLayout.CENTER);
        log.add(totalPanel, BorderLayout.SOUTH);
    }

}
