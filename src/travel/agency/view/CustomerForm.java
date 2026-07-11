package travel.agency.view;

import travel.agency.dao.CustomerDAO;
import travel.agency.model.Customer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerForm extends JFrame {

    private JTextField txtName, txtNic, txtPhone, txtEmail, txtAddress;
    private JTable table;
    private DefaultTableModel tableModel;
    private CustomerDAO dao;
    private int selectedCustomerId = -1;

    public CustomerForm() {
        dao = new CustomerDAO();
        setTitle("Customer Management");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        add(buildFormPanel(), BorderLayout.NORTH);
        add(buildTablePanel(), BorderLayout.CENTER);
        add(buildButtonPanel(), BorderLayout.SOUTH);

        loadCustomers();
    }

    private JPanel buildFormPanel() {
        JPanel panel = new JPanel(new GridLayout(5, 2, 8, 8));
        panel.setBorder(BorderFactory.createTitledBorder("Customer Details"));

        panel.add(new JLabel("Full Name:"));
        txtName = new JTextField();
        panel.add(txtName);

        panel.add(new JLabel("NIC/Passport:"));
        txtNic = new JTextField();
        panel.add(txtNic);

        panel.add(new JLabel("Phone:"));
        txtPhone = new JTextField();
        panel.add(txtPhone);

        panel.add(new JLabel("Email:"));
        txtEmail = new JTextField();
        panel.add(txtEmail);

        panel.add(new JLabel("Address:"));
        txtAddress = new JTextField();
        panel.add(txtAddress);

        return panel;
    }

    private JScrollPane buildTablePanel() {
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Full Name", "NIC/Passport", "Phone", "Email", "Address"}, 0
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

        btnAdd.addActionListener(e -> addCustomer());
        btnUpdate.addActionListener(e -> updateCustomer());
        btnDelete.addActionListener(e -> deleteCustomer());
        btnClear.addActionListener(e -> clearForm());

        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);

        return panel;
    }

    private void loadCustomers() {
        tableModel.setRowCount(0);
        List<Customer> customers = dao.getAllCustomers();
        for (Customer c : customers) {
            tableModel.addRow(new Object[]{
                    c.getCustomerId(), c.getFullName(), c.getNicOrPassport(),
                    c.getPhone(), c.getEmail(), c.getAddress()
            });
        }
    }

    private void onRowSelected() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        selectedCustomerId = (int) tableModel.getValueAt(row, 0);
        txtName.setText(tableModel.getValueAt(row, 1).toString());
        txtNic.setText(tableModel.getValueAt(row, 2).toString());
        txtPhone.setText(tableModel.getValueAt(row, 3).toString());
        txtEmail.setText(tableModel.getValueAt(row, 4).toString());
        txtAddress.setText(tableModel.getValueAt(row, 5).toString());
    }

    private boolean validateInput() {
        if (txtName.getText().trim().isEmpty() ||
            txtNic.getText().trim().isEmpty() ||
            txtPhone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name, NIC/Passport, and Phone are required.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    private void addCustomer() {
        if (!validateInput()) return;

        Customer c = new Customer(0, txtName.getText().trim(), txtNic.getText().trim(),
                txtPhone.getText().trim(), txtEmail.getText().trim(), txtAddress.getText().trim());

        boolean success = dao.insertCustomer(c);
        if (success) {
            JOptionPane.showMessageDialog(this, "Customer added successfully.");
            loadCustomers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to add customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCustomer() {
        if (selectedCustomerId == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validateInput()) return;

        Customer c = new Customer(selectedCustomerId, txtName.getText().trim(), txtNic.getText().trim(),
                txtPhone.getText().trim(), txtEmail.getText().trim(), txtAddress.getText().trim());

        boolean success = dao.updateCustomer(c);
        if (success) {
            JOptionPane.showMessageDialog(this, "Customer updated successfully.");
            loadCustomers();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update customer.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteCustomer() {
        if (selectedCustomerId == -1) {
            JOptionPane.showMessageDialog(this, "Select a customer from the table first.", "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this customer?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = dao.deleteCustomer(selectedCustomerId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Customer deleted successfully.");
                loadCustomers();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete customer.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        selectedCustomerId = -1;
        txtName.setText("");
        txtNic.setText("");
        txtPhone.setText("");
        txtEmail.setText("");
        txtAddress.setText("");
        table.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CustomerForm form = new CustomerForm();
            form.setVisible(true);
        });
    }
}