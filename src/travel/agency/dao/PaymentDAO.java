package travel.agency.dao;

import travel.agency.model.Payment;
import travel.agency.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentDAO {

    public boolean insertPayment(Payment payment) {
        String sql = "INSERT INTO payment (booking_id, amount_paid, payment_date, payment_method, balance_due) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, payment.getBookingId());
            ps.setBigDecimal(2, payment.getAmountPaid());
            ps.setDate(3, Date.valueOf(payment.getPaymentDate()));
            ps.setString(4, payment.getPaymentMethod());
            ps.setBigDecimal(5, payment.getBalanceDue());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Payment> getPaymentsByBookingId(int bookingId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Payment> getAllPayments() {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM payment";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapRow(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Payment mapRow(ResultSet rs) throws SQLException {
        return new Payment(
                rs.getInt("payment_id"),
                rs.getInt("booking_id"),
                rs.getBigDecimal("amount_paid"),
                rs.getDate("payment_date").toLocalDate(),
                rs.getString("payment_method"),
                rs.getBigDecimal("balance_due")
        );
    }
}