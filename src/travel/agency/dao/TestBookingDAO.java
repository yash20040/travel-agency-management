package travel.agency.dao;

import travel.agency.exception.SeatNotAvailableException;
import travel.agency.model.Booking;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class TestBookingDAO {
    public static void main(String[] args) {
        BookingDAO dao = new BookingDAO();

        Booking booking = new Booking(
                0, 1, 1, 1,
                LocalDate.now(),
                LocalDate.now().plusMonths(1),
                2,
                new BigDecimal("50000.00"),
                "Confirmed"
        );

        try {
            boolean success = dao.createBooking(booking);
            System.out.println("Booking created " + success);
        } catch (SeatNotAvailableException e) {
            System.out.println("Booking failed: " + e.getMessage());
        }

        System.out.println(" All Bookings");
        List<Booking> bookings = dao.getAllBookings();
        for (Booking b : bookings) {
            System.out.println(b.getBookingId() + " - Customer:" + b.getCustomerId() + " - Package:" + b.getPackageId() + " - Travelers:" + b.getNoOfTravelers() + " - Status:" + b.getStatus());
        }
    }
}