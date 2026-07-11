package travel.agency.view;

import travel.agency.model.Staff;

import javax.swing.*;
import java.awt.*;

public class Dashboard extends JFrame {

    private Staff loggedInStaff;

    public Dashboard(Staff staff) {
        this.loggedInStaff = staff;
        setTitle("Travel Agency - Dashboard");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JLabel welcome = new JLabel("Welcome, " + staff.getFullName() + " (" + staff.getRole() + ")", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.PLAIN, 13));
        welcome.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(welcome, BorderLayout.NORTH);

        add(buildButtonPanel(), BorderLayout.CENTER);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        JButton btnCustomers = new JButton("Manage Customers");
        btnCustomers.addActionListener(e -> new CustomerForm().setVisible(true));
        panel.add(btnCustomers);

        JButton btnPackages = new JButton("Manage Trip Packages");
        btnPackages.addActionListener(e -> new TripPackageForm().setVisible(true));
        panel.add(btnPackages);

        JButton btnBooking = new JButton("New Booking");
        btnBooking.addActionListener(e -> new BookingForm(loggedInStaff).setVisible(true));
        panel.add(btnBooking); 
        JButton btnReports = new JButton("View Reports");
        
        JButton btnPayments = new JButton("Manage Payments");
        btnPayments.addActionListener(e -> new PaymentForm().setVisible(true));
        panel.add(btnPayments);
        
        panel.add(btnReports);

        return panel;
    }
}