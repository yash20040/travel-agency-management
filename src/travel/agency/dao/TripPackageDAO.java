package travel.agency.dao;

import travel.agency.model.TripPackage;
import travel.agency.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripPackageDAO {

    public boolean insertPackage(TripPackage pkg) {
        String sql = "INSERT INTO trip_package (package_name, destination, description, duration_days, price, total_seats, seats_available) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pkg.getPackageName());
            ps.setString(2, pkg.getDestination());
            ps.setString(3, pkg.getDescription());
            ps.setInt(4, pkg.getDurationDays());
            ps.setBigDecimal(5, pkg.getPrice());
            ps.setInt(6, pkg.getTotalSeats());
            ps.setInt(7, pkg.getSeatsAvailable());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePackage(TripPackage pkg) {
        String sql = "UPDATE trip_package SET package_name=?, destination=?, description=?, duration_days=?, price=?, total_seats=?, seats_available=? WHERE package_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, pkg.getPackageName());
            ps.setString(2, pkg.getDestination());
            ps.setString(3, pkg.getDescription());
            ps.setInt(4, pkg.getDurationDays());
            ps.setBigDecimal(5, pkg.getPrice());
            ps.setInt(6, pkg.getTotalSeats());
            ps.setInt(7, pkg.getSeatsAvailable());
            ps.setInt(8, pkg.getPackageId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePackage(int packageId) {
        String sql = "DELETE FROM trip_package WHERE package_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, packageId);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<TripPackage> getAllPackages() {
        List<TripPackage> list = new ArrayList<>();
        String sql = "SELECT * FROM trip_package";
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

    public TripPackage getPackageById(int packageId) {
        String sql = "SELECT * FROM trip_package WHERE package_id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, packageId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return mapRow(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public boolean reduceSeats(int packageId, int seatsToReduce) {
        String sql = "UPDATE trip_package SET seats_available = seats_available - ? WHERE package_id=? AND seats_available >= ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, seatsToReduce);
            ps.setInt(2, packageId);
            ps.setInt(3, seatsToReduce);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private TripPackage mapRow(ResultSet rs) throws SQLException {
        return new TripPackage(
                rs.getInt("package_id"),
                rs.getString("package_name"),
                rs.getString("destination"),
                rs.getString("description"),
                rs.getInt("duration_days"),
                rs.getBigDecimal("price"),
                rs.getInt("total_seats"),
                rs.getInt("seats_available")
        );
    }
}