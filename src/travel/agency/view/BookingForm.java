package travel.agency.view;

import travel.agency.dao.BookingDAO;
import travel.agency.dao.CustomerDAO;
import travel.agency.dao.TripPackageDAO;
import travel.agency.exception.SeatNotAvailableException;
import travel.agency.model.Booking;
import travel.agency.model.Customer;
import travel.agency.model.Staff;
import travel.agency.model.TripPackage;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class BookingForm extends JFrame {

    private JComboBox<Customer> cmbCustomer;
    private JComboBox<TripPackage> cmbPackage;
    private JTextField txtTravelDate;
    private JTextField txtTravelers;
    private JLabel lblTotalPrice;
    private JLabel lblSeatsAvailable;

    private CustomerDAO customerDAO;
    private TripPackageDAO packageDAO;
    private BookingDAO bookingDAO;
    private Staff loggedInStaff;

    public BookingForm(Staff staff) {
        this.loggedInStaff = staff;
        customerDAO = new CustomerDAO();
        packageDAO = new TripPackageDAO();
        bookingDAO = new BookingDAO();

        setTitle("New Booking");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Booking Details"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Customer dropdown
        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Customer:"), gbc);
        gbc.gridx = 1;
        cmbCustomer = new JComboBox<>();
        loadCustomers();
        panel.add(cmbCustomer, gbc);

        // Package dropdown
        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Trip Package:"), gbc);
        gbc.gridx = 1;
        cmbPackage = new JComboBox<>();
        loadPackages();
        cmbPackage.addActionListener(e -> updateSeatsAndPrice());
        panel.add(cmbPackage, gbc);

        // Seats available (read-only info)
        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Seats Available:"), gbc);
        gbc.gridx = 1;
        lblSeatsAvailable = new JLabel("-");
        panel.add(lblSeatsAvailable, gbc);

        // Travel date
        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Travel Date (yyyy-mm-dd):"), gbc);
        gbc.gridx = 1;
        txtTravelDate = new JTextField(LocalDate.now().plusMonths(1).toString());
        panel.add(txtTravelDate, gbc);

        // Number of travelers
        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("No. of Travelers:"), gbc);
        gbc.gridx = 1;
        txtTravelers = new JTextField("1");
        panel.add(txtTravelers, gbc);
        txtTravelers.addCaretListener(e -> updateSeatsAndPrice());

        // Total price (calculated)
        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Total Price (Rs.):"), gbc);
        gbc.gridx = 1;
        lblTotalPrice = new JLabel("-");
        lblTotalPrice.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblTotalPrice, gbc);

        updateSeatsAndPrice();

        return panel;
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        JButton btnBook = new JButton("Confirm Booking");
        btnBook.addActionListener(e -> confirmBooking());
        panel.add(btnBook);
        return panel;
    }

    private void loadCustomers() {
        List<Customer> customers = customerDAO.getAllCustomers();
        for (Customer c : customers) {
            cmbCustomer.addItem(c);
        }
        cmbCustomer.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : value.getFullName() + " (" + value.getPhone() + ")"));
    }

    private void loadPackages() {
        List<TripPackage> packages = packageDAO.getAllPackages();
        for (TripPackage p : packages) {
            cmbPackage.addItem(p);
        }
    }

    private void updateSeatsAndPrice() {
        TripPackage selected = (TripPackage) cmbPackage.getSelectedItem();
        if (selected == null) {
            lblSeatsAvailable.setText("-");
            lblTotalPrice.setText("-");
            return;
        }

        lblSeatsAvailable.setText(String.valueOf(selected.getSeatsAvailable()));

        try {
            int travelers = Integer.parseInt(txtTravelers.getText().trim());
            BigDecimal total = selected.getPrice().multiply(BigDecimal.valueOf(travelers));
            lblTotalPrice.setText("Rs. " + total.toPlainString());
        } catch (NumberFormatException e) {
            lblTotalPrice.setText("-");
        }
    }

    private void confirmBooking() {
        Customer selectedCustomer = (Customer) cmbCustomer.getSelectedItem();
        TripPackage selectedPackage = (TripPackage) cmbPackage.getSelectedItem();

        if (selectedCustomer == null || selectedPackage == null) {
            JOptionPane.showMessageDialog(this, "Please select a customer and a trip package.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int travelers;
        try {
            travelers = Integer.parseInt(txtTravelers.getText().trim());
            if (travelers <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Number of travelers must be a positive number.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LocalDate travelDate;
        try {
            travelDate = LocalDate.parse(txtTravelDate.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Travel date must be in format yyyy-mm-dd.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (travelDate.isBefore(LocalDate.now())) {
            JOptionPane.showMessageDialog(this, "Travel date cannot be in the past.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal totalAmount = selectedPackage.getPrice().multiply(BigDecimal.valueOf(travelers));

        Booking booking = new Booking(
                0,
                selectedCustomer.getCustomerId(),
                selectedPackage.getPackageId(),
                loggedInStaff.getStaffId(),
                LocalDate.now(),
                travelDate,
                travelers,
                totalAmount,
                "Confirmed"
        );

        try {
            boolean success = bookingDAO.createBooking(booking);
            if (success) {
                JOptionPane.showMessageDialog(this, "Booking confirmed successfully!\nTotal: Rs. " + totalAmount.toPlainString());
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to create booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SeatNotAvailableException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Seats Not Available", JOptionPane.ERROR_MESSAGE);
        }
    }
}