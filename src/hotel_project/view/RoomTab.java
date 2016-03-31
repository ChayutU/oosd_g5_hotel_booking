package hotel_project.view;

import hotel_project.DateLabelFormatter;
import hotel_project.OutputPrinter;
import hotel_project.control.BookingRoomAction;
import hotel_project.control.CancelBookingRoomAction;
import hotel_project.control.SearchRoomAction;
import hotel_project.model.DbService;
import hotel_project.model.Room;
import hotel_project.model.Service;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.Vector;

import static javax.swing.JOptionPane.QUESTION_MESSAGE;
import static javax.swing.JOptionPane.YES_NO_OPTION;

/**
 * Created by Chayut on 25-Mar-16.
 */
public class RoomTab extends JPanel {

    private Service service = new DbService();

    public RoomTab() {
        setLayout(new BorderLayout(5, 10));
        setBackground(Color.decode("#976241"));

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


        searchPanel.setLayout(new GridLayout(1, 9, 8, 0));

        searchPanel.add(lblPerson);
        searchPanel.add(personSpin);

        searchPanel.add(lblRoomType);
        searchPanel.add(roomTypeBox);

        searchPanel.add(lblCheckInDate);
        searchPanel.add(dateInPicker);

        searchPanel.add(lblCheckOutDate);
        searchPanel.add(dateOutPicker);

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

        Action searchRoomAction = new SearchRoomAction(table, tModel, dateInPicker, dateOutPicker, personSpin, roomTypeBox, service);
        searchRoomAction.putValue(Action.NAME, "Search");
        JButton btnSearch = new JButton(searchRoomAction);

        searchPanel.add(btnSearch);

        Action bookingRoomAction = new BookingRoomAction(table, dateInPicker, dateOutPicker, btnSearch, service);
        bookingRoomAction.putValue(Action.NAME, "Booking");
        JButton btnBook = new JButton(bookingRoomAction);

        Action cancelBookingRoomAction = new CancelBookingRoomAction(table, dateInPicker, dateOutPicker, btnSearch, service);
        cancelBookingRoomAction.putValue(Action.NAME, "Cancel Booking");
        JButton btnCBook = new JButton(cancelBookingRoomAction);

        JButton btnCIn = new JButton("Check-In");

        JButton btnCOut = new JButton("Check-Out");

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

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public String[] getRoomType() {

        String sql = "SELECT room_type FROM Hotel_room WHERE 1 GROUP BY room_type ORDER BY room_cost ASC";
        ArrayList<Room> roomTypeList = service.searchRooms(sql);
        String[] roomType = new String[roomTypeList.size() + 1];
        roomType[0] = "-";
        int i = 1;
        for (Room room : roomTypeList) {
            roomType[i++] = room.getType();
        }

        return roomType;
    }
}
