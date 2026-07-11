package travel.agency.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Booking {
    private int bookingId;
    private int customerId;
    private int packageId;
    private int staffId;
    private LocalDate bookingDate;
    private LocalDate travelDate;
    private int noOfTravelers;
    private BigDecimal totalAmount;
    private String status;

    public Booking() {}

    public Booking(int bookingId, int customerId, int packageId, int staffId, LocalDate bookingDate,
                    LocalDate travelDate, int noOfTravelers, BigDecimal totalAmount, String status) {
        this.bookingId = bookingId;
        this.customerId = customerId;
        this.packageId = packageId;
        this.staffId = staffId;
        this.bookingDate = bookingDate;
        this.travelDate = travelDate;
        this.noOfTravelers = noOfTravelers;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }
    public int getStaffId() { return staffId; }
    public void setStaffId(int staffId) { this.staffId = staffId; }
    public LocalDate getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDate bookingDate) { this.bookingDate = bookingDate; }
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    public int getNoOfTravelers() { return noOfTravelers; }
    public void setNoOfTravelers(int noOfTravelers) { this.noOfTravelers = noOfTravelers; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}