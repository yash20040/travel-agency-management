package travel.agency.view;

import travel.agency.dao.TripPackageDAO;
import travel.agency.model.TripPackage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class TripPackageForm extends JFrame {

    private JTextField txtName, txtDestination, txtDescription, txtDuration, txtPrice, txtTotalSeats, txtSeatsAvailable;
    private JTable table;
    private DefaultTableModel tableModel;
    private TripPackageDAO dao;
    private int selectedPackageId = -1;

    public TripPackageForm() {
        dao = new TripPackageDAO();
        setTitle("Trip Package Management");
        setSize(900, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadPackages();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Trip Package Details"));

        panel.add(new JLabel("Package Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("Destination:"));
        txtDestination = new JTextField();
        panel.add(txtDestination);

        panel.add(new JLabel("Description:"));
        txtDescription = new JTextField();
        panel.add(txtDescription);

        panel.add(new JLabel("Duration (Days):"));
        txtDuration = new JTextField();
        panel.add(txtDuration);

        panel.add(new JLabel("Price (Rs.):"));
        txtPrice = new JTextField();
        panel.add(txtPrice);

        panel.add(new JLabel("Total Seats:"));
        txtTotalSeats = new JTextField();
        panel.add(txtTotalSeats);

        panel.add(new JLabel("Seats Available:"));
        txtSeatsAvailable = new JTextField();
        panel.add(txtSeatsAvailable);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Package Name", "Destination", "Description", "Duration", "Price", "Total Seats", "Seats Available"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getSelectionModel().addListSelectionListener(e -> onRowSelected());
        return new JScrollPane(table);
    }

    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();

        JButton btnAdd = new JButton("Add");
        JButton btnUpdate = new JButton("Update");
        JButton btnDelete = new JButton("Delete");
        JButton btnClear = new JButton("Clear");

        btnAdd.addActionListener(e -> addPackage());
        btnUpdate.addActionListener(e -> updatePackage());
        btnDelete.addActionListener(e -> deletePackage());
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

    private void loadPackages() {
        tableModel.setRowCount(0);
        List<TripPackage> packages = dao.getAllPackages();
        for (TripPackage p : packages) {
            tableModel.addRow(new Object[]{
                    p.getPackageId(), p.getPackageName(), p.getDestination(), p.getDescription(),
                    p.getDurationDays(), p.getPrice(), p.getTotalSeats(), p.getSeatsAvailable()
            });
        }
    }

    private void onRowSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        selectedPackageId = (int) tableModel.getValueAt(row, 0);
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtDestination.setText(tableModel.getValueAt(row, 2).toString());
        txtDescription.setText(tableModel.getValueAt(row, 3).toString());
        txtDuration.setText(tableModel.getValueAt(row, 4).toString());
        txtPrice.setText(tableModel.getValueAt(row, 5).toString());
        txtTotalSeats.setText(tableModel.getValueAt(row, 6).toString());
        txtSeatsAvailable.setText(tableModel.getValueAt(row, 7).toString());
    }

    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty() ||
            txtDestination.getText().trim().isEmpty() ||
            txtDuration.getText().trim().isEmpty() ||
            txtPrice.getText().trim().isEmpty() ||
            txtTotalSeats.getText().trim().isEmpty() ||
            txtSeatsAvailable.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields except Description are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        try {
            Integer.parseInt(txtDuration.getText().trim());
            Integer.parseInt(txtTotalSeats.getText().trim());
            Integer.parseInt(txtSeatsAvailable.getText().trim());
            new BigDecimal(txtPrice.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Duration, Price, Total Seats, and Seats Available must be valid numbers.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    private TripPackage buildPackageFromForm(int id) {
        return new TripPackage(
                id,
                txtName.getText().trim(),
                txtDestination.getText().trim(),
                txtDescription.getText().trim(),
                Integer.parseInt(txtDuration.getText().trim()),
                new BigDecimal(txtPrice.getText().trim()),
                Integer.parseInt(txtTotalSeats.getText().trim()),
                Integer.parseInt(txtSeatsAvailable.getText().trim())
        );
    }

    private void addPackage() {
        if (!validateInput()) return;

        TripPackage pkg = buildPackageFromForm(0);
        boolean success = dao.insertPackage(pkg);
        if (success) {
            JOptionPane.showMessageDialog(this, "Trip package added successfully.");
            loadPackages();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add trip package.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePackage() {
        if (selectedPackageId == -1) {
            JOptionPane.showMessageDialog(this, "Select a package from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        TripPackage pkg = buildPackageFromForm(selectedPackageId);
        boolean success = dao.updatePackage(pkg);
        if (success) {
            JOptionPane.showMessageDialog(this, "Trip package updated successfully.");
            loadPackages();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update trip package.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePackage() {
        if (selectedPackageId == -1) {
            JOptionPane.showMessageDialog(this, "Select a package from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this trip package?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = dao.deletePackage(selectedPackageId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Trip package deleted successfully.");
                loadPackages();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete trip package.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedPackageId = -1;
        txtName.setText("");
        txtDestination.setText("");
        txtDescription.setText("");
        txtDuration.setText("");
        txtPrice.setText("");
        txtTotalSeats.setText("");
        txtSeatsAvailable.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TripPackageForm form = new TripPackageForm();
            form.setVisible(true);
        });
    }
}