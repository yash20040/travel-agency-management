package travel.agency.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Payment {
    private int paymentId;
    private int bookingId;
    private BigDecimal amountPaid;
    private LocalDate paymentDate;
    private String paymentMethod;
    private BigDecimal balanceDue;

    public Payment() {}

    public Payment(int paymentId, int bookingId, BigDecimal amountPaid, LocalDate paymentDate,
                    String paymentMethod, BigDecimal balanceDue) {
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
        this.paymentMethod = paymentMethod;
        this.balanceDue = balanceDue;
    }

    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }
    public int getBookingId() { return bookingId; }
    public void setBookingId(int bookingId) { this.bookingId = bookingId; }
    public BigDecimal getAmountPaid() { return amountPaid; }
    public void setAmountPaid(BigDecimal amountPaid) { this.amountPaid = amountPaid; }
    public LocalDate getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDate paymentDate) { this.paymentDate = paymentDate; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public BigDecimal getBalanceDue() { return balanceDue; }
    public void setBalanceDue(BigDecimal balanceDue) { this.balanceDue = balanceDue; }
}