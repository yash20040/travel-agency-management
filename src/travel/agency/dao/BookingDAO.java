package travel.agency.dao;

import travel.agency.exception.SeatNotAvailableException;
import travel.agency.model.Booking;
import travel.agency.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    public boolean createBooking(Booking booking) throws SeatNotAvailableException {
        String checkSeatsSql = "SELECT seats_available FROM trip_package WHERE package_id = ?";
        String insertBookingSql = "INSERT INTO booking (customer_id, package_id, staff_id, booking_date, travel_date, no_of_travelers, total_amount, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        String reduceSeatsSql = "UPDATE trip_package SET seats_available = seats_available - ? WHERE package_id = ?";

        try (Connection conn = DBConnection.getConnection()) {

            
            int availableSeats = 0;
            try (PreparedStatement ps = conn.prepareStatement(checkSeatsSql)) {
                ps.setInt(1, booking.getPackageId());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    availableSeats = rs.getInt("seats_available");
                }
            }

            if (booking.getNoOfTravelers() > availableSeats) {
                throw new SeatNotAvailableException(
                    "Only " + availableSeats + " seat(s) available, but " + booking.getNoOfTravelers() + " requested."
                );
            }

             try (PreparedStatement ps = conn.prepareStatement(insertBookingSql)) {
                ps.setInt(1, booking.getCustomerId());
                ps.setInt(2, booking.getPackageId());
                ps.setInt(3, booking.getStaffId());
                ps.setDate(4, Date.valueOf(booking.getBookingDate()));
                ps.setDate(5, Date.valueOf(booking.getTravelDate()));
                ps.setInt(6, booking.getNoOfTravelers());
                ps.setBigDecimal(7, booking.getTotalAmount());
                ps.setString(8, booking.getStatus());

                int rows = ps.executeUpdate();
                if (rows == 0) return false;
            }

            
            try (PreparedStatement ps = conn.prepareStatement(reduceSeatsSql)) {
                ps.setInt(1, booking.getNoOfTravelers());
                ps.setInt(2, booking.getPackageId());
                ps.executeUpdate();
            }

            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT * FROM booking";
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

    public Booking getBookingById(int bookingId) {
        String sql = "SELECT * FROM booking WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, bookingId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateBookingStatus(int bookingId, String status) {
        String sql = "UPDATE booking SET status=? WHERE booking_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, status);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private Booking mapRow(ResultSet rs) throws SQLException {
        return new Booking(
                rs.getInt("booking_id"),
                rs.getInt("customer_id"),
                rs.getInt("package_id"),
                rs.getInt("staff_id"),
                rs.getDate("booking_date").toLocalDate(),
                rs.getDate("travel_date").toLocalDate(),
                rs.getInt("no_of_travelers"),
                rs.getBigDecimal("total_amount"),
                rs.getString("status")
        );
    }
}