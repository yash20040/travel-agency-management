package travel.agency.dao;

import travel.agency.model.TripPackage;
import java.util.List;

public class TestTripPackageDAO {
    public static void main(String[] args) {
        TripPackageDAO dao = new TripPackageDAO();
        List<TripPackage> packages = dao.getAllPackages();

        for (TripPackage p : packages) {
            System.out.println(p.getPackageId() + " - " + p.getPackageName() + " - " + p.getDestination() + " - Rs." + p.getPrice() + " - Seats: " + p.getSeatsAvailable());
        }
    }
}