package travel.agency.model;

import java.math.BigDecimal;

public class TripPackage {
    private int packageId;
    private String packageName;
    private String destination;
    private String description;
    private int durationDays;
    private BigDecimal price;
    private int totalSeats;
    private int seatsAvailable;

    public TripPackage() {}

    public TripPackage(int packageId, String packageName, String destination, String description,
                        int durationDays, BigDecimal price, int totalSeats, int seatsAvailable) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.destination = destination;
        this.description = description;
        this.durationDays = durationDays;
        this.price = price;
        this.totalSeats = totalSeats;
        this.seatsAvailable = seatsAvailable;
    }

    public int getPackageId() { return packageId; }
    public void setPackageId(int packageId) { this.packageId = packageId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getSeatsAvailable() { return seatsAvailable; }
    public void setSeatsAvailable(int seatsAvailable) { this.seatsAvailable = seatsAvailable; }

    @Override
    public String toString() { return packageName + " - " + destination; }
}