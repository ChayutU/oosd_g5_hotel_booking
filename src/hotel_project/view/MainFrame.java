package hotel_project.view;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Chayut on 25-Mar-16.
 */
public class MainFrame extends JFrame {

    public MainFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Hotel");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.decode("#708090"));
        mainPanel.setLayout(new BorderLayout());
        getContentPane().add(mainPanel);

        RoomTab roomTab = new RoomTab();
        CustomerTab customerTab = new CustomerTab();
        LogTab logTab = new LogTab();

        JTabbedPane tabPane = new JTabbedPane();
        tabPane.addTab("Room Search", roomTab);
        tabPane.addTab("Customer", customerTab);
        tabPane.addTab("    Log    ", logTab);
        mainPanel.add(tabPane);
    }
}
