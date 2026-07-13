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
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(245, 247, 250));

        add(buildHeaderPanel(staff), BorderLayout.NORTH);
        add(buildButtonPanel(), BorderLayout.CENTER);
    }

    private JPanel buildHeaderPanel(Staff staff) {
        JPanel header = new JPanel();
        header.setBackground(new Color(33, 87, 143));
        header.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel welcome = new JLabel("Welcome! " + staff.getFullName() + " (" + staff.getRole() + ")");
        welcome.setFont(new Font("Arial", Font.PLAIN, 16));
        welcome.setForeground(Color.WHITE);
        header.add(welcome);

        return header;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));
        panel.setBackground(new Color(245, 247, 250));

        JButton btnCustomers = makeButton("Manage Customers", new Color(52, 152, 219));
        btnCustomers.addActionListener(e -> new CustomerForm().setVisible(true));
        panel.add(btnCustomers);

        JButton btnPackages = makeButton("Manage Trip Packages", new Color(46, 204, 113));
        btnPackages.addActionListener(e -> new TripPackageForm().setVisible(true));
        panel.add(btnPackages);

        JButton btnBooking = makeButton("New Booking", new Color(230, 126, 34));
        btnBooking.addActionListener(e -> new BookingForm(loggedInStaff).setVisible(true));
        panel.add(btnBooking);

        JButton btnPayments = makeButton("Manage Payments", new Color(155, 89, 182));
        btnPayments.addActionListener(e -> new PaymentForm().setVisible(true));
        panel.add(btnPayments);

        JButton btnReports = makeButton("View Reports", new Color(231, 76, 60));
        btnReports.addActionListener(e -> ReportForm.showBookingReport());
        panel.add(btnReports);

        return panel;
    }

    private JButton makeButton(String text, Color bgColor) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bgColor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}