package travel.agency.dao;

import travel.agency.model.Customer;
import java.util.List;

public class TestCustomerDAO {
    public static void main(String[] args) {
        CustomerDAO dao = new CustomerDAO();
        List<Customer> customers = dao.getAllCustomers();

        for (Customer c : customers) {
            System.out.println(c.getCustomerId() + " - " + c.getFullName() + " - " + c.getPhone());
        }
    }
}