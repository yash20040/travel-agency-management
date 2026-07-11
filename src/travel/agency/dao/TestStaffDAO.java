package travel.agency.dao;

import travel.agency.model.Staff;
import java.util.List;

public class TestStaffDAO {
    public static void main(String[] args) {
        StaffDAO dao = new StaffDAO();
        List<Staff> staffList = dao.getAllStaff();

        for (Staff s : staffList) {
            System.out.println(s.getStaffId() + " - " + s.getFullName() + " - " + s.getRole() + " - " + s.getUsername());
        }
    }
}