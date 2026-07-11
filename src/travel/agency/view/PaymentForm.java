package travel.agency.view;

import travel.agency.dao.BookingDAO;
import travel.agency.dao.PaymentDAO;
import travel.agency.model.Booking;
import travel.agency.model.Payment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class PaymentForm extends JFrame {

    private JComboBox<Booking> cmbBooking;
    private JTextField txtAmount;
    private JComboBox<String> cmbMethod;
    private JLabel lblTotalAmount, lblAlreadyPaid, lblBalanceDue;
    private JTable table;
    private DefaultTableModel tableModel;

    private BookingDAO bookingDAO;
    private PaymentDAO paymentDAO;

    public PaymentForm() {
        bookingDAO = new BookingDAO();
        paymentDAO = new PaymentDAO();

        setTitle("Payment Management");
        setSize(700, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Record Payment"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panel.add(new JLabel("Booking:"), gbc);
        gbc.gridx = 1;
        cmbBooking = new JComboBox<>();
        loadBookings();
        cmbBooking.addActionListener(e -> onBookingSelected());
        panel.add(cmbBooking, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panel.add(new JLabel("Total Amount:"), gbc);
        gbc.gridx = 1;
        lblTotalAmount = new JLabel("-");
        panel.add(lblTotalAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(new JLabel("Already Paid:"), gbc);
        gbc.gridx = 1;
        lblAlreadyPaid = new JLabel("-");
        panel.add(lblAlreadyPaid, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panel.add(new JLabel("Balance Due:"), gbc);
        gbc.gridx = 1;
        lblBalanceDue = new JLabel("-");
        panel.add(lblBalanceDue, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panel.add(new JLabel("Amount to Pay:"), gbc);
        gbc.gridx = 1;
        txtAmount = new JTextField(10);
        panel.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panel.add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cmbMethod = new JComboBox<>(new String[]{"Cash", "Card", "Bank Transfer"});
        panel.add(cmbMethod, gbc);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(new Object[]{"Payment ID", "Booking ID", "Amount Paid", "Date", "Method", "Balance Due"}, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };
        table = new JTable(tableModel);
        return new JScrollPane(table);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        JButton btnPay = new JButton("Record Payment");
        btnPay.addActionListener(e -> recordPayment());
        panel.add(btnPay);
        return panel;
    }

    private void loadBookings() {
        List<Booking> bookings = bookingDAO.getAllBookings();
        for (Booking b : bookings) {
            cmbBooking.addItem(b);
        }
        cmbBooking.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value == null ? "" : "Booking #" + value.getBookingId() + " - Rs." + value.getTotalAmount()));
        if (cmbBooking.getItemCount() > 0) onBookingSelected();
    }

    private void onBookingSelected() {
        Booking selected = (Booking) cmbBooking.getSelectedItem();
        if (selected == null) return;

        List<Payment> payments = paymentDAO.getPaymentsByBookingId(selected.getBookingId());
        BigDecimal totalPaid = BigDecimal.ZERO;
        for (Payment p : payments) totalPaid = totalPaid.add(p.getAmountPaid());

        BigDecimal balance = selected.getTotalAmount().subtract(totalPaid);

        lblTotalAmount.setText("Rs. " + selected.getTotalAmount().toPlainString());
        lblAlreadyPaid.setText("Rs. " + totalPaid.toPlainString());
        lblBalanceDue.setText("Rs. " + balance.toPlainString());

        tableModel.setRowCount(0);
        for (Payment p : payments) {
            tableModel.addRow(new Object[]{p.getPaymentId(), p.getBookingId(), p.getAmountPaid(), p.getPaymentDate(), p.getPaymentMethod(), p.getBalanceDue()});
        }
    }

    private void recordPayment() {
        Booking selected = (Booking) cmbBooking.getSelectedItem();
        if (selected == null) {
            JOptionPane.showMessageDialog(this, "Select a booking first.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        BigDecimal amount;
        try {
            amount = new BigDecimal(txtAmount.getText().trim());
            if (amount.compareTo(BigDecimal.ZERO) <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Enter a valid payment amount.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<Payment> payments = paymentDAO.getPaymentsByBookingId(selected.getBookingId());
        BigDecimal totalPaid = BigDecimal.ZERO;
        for (Payment p : payments) totalPaid = totalPaid.add(p.getAmountPaid());
        BigDecimal newBalance = selected.getTotalAmount().subtract(totalPaid).subtract(amount);

        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            JOptionPane.showMessageDialog(this, "Payment exceeds the remaining balance.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Payment payment = new Payment(0, selected.getBookingId(), amount, LocalDate.now(), (String) cmbMethod.getSelectedItem(), newBalance);
        boolean success = paymentDAO.insertPayment(payment);

        if (success) {
            JOptionPane.showMessageDialog(this, "Payment recorded successfully.");
            txtAmount.setText("");
            onBookingSelected();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to record payment.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}