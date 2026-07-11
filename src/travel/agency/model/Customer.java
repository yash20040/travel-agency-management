package travel.agency.model;

public class Customer {
    private int customerId;
    private String fullName;
    private String nicOrPassport;
    private String phone;
    private String email;
    private String address;

    public Customer() {}

    public Customer(int customerId, String fullName, String nicOrPassport, String phone, String email, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.nicOrPassport = nicOrPassport;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getNicOrPassport() { return nicOrPassport; }
    public void setNicOrPassport(String nicOrPassport) { this.nicOrPassport = nicOrPassport; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}