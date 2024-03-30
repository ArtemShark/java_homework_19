package org.example.clientsDAO;

import org.example.model.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class ClientsDaoImpl implements ClientsDao {
    private final Connection connection;

    public ClientsDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public Optional<Client> findById(int id) {
        String sql = "SELECT * FROM Clients WHERE ClientID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
                return Optional.of(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public void save(Client client) {
        String sql = "INSERT INTO Clients (FullName, BirthDate, Phone, Email, Discount) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getFullName());
            pstmt.setDate(2, Date.valueOf(client.getBirthDate()));
            pstmt.setString(3, client.getPhone());
            pstmt.setString(4, client.getEmail());
            pstmt.setInt(5, client.getDiscount());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Client client) {
        String sql = "UPDATE Clients SET FullName = ?, BirthDate = ?, Phone = ?, Email = ?, Discount = ? WHERE ClientID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, client.getFullName());
            pstmt.setDate(2, Date.valueOf(client.getBirthDate()));
            pstmt.setString(3, client.getPhone());
            pstmt.setString(4, client.getEmail());
            pstmt.setInt(5, client.getDiscount());
            pstmt.setInt(6, client.getClientId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM Clients WHERE ClientID = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @Override
    public int findMinDiscount() {
        String sql = "SELECT MIN(Discount) FROM Clients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int findMaxDiscount() {
        String sql = "SELECT MAX(Discount) FROM Clients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public List<Client> findClientsWithMinDiscount(int minDiscount) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients WHERE Discount = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, minDiscount);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public List<Client> findClientsWithMaxDiscount(int maxDiscount) {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients WHERE Discount = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, maxDiscount);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public double findAverageDiscount() {
        String sql = "SELECT AVG(Discount) FROM Clients";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public Client findYoungestClient() {
        String sql = "SELECT * FROM Clients ORDER BY BirthDate ASC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Client findOldestClient() {
        String sql = "SELECT * FROM Clients ORDER BY BirthDate DESC LIMIT 1";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> findClientsWithBirthdayToday() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients WHERE MONTH(BirthDate) = MONTH(CURDATE()) AND DAY(BirthDate) = DAY(CURDATE())";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

    @Override
    public List<Client> findClientsWithEmailMissing() {
        List<Client> clients = new ArrayList<>();
        String sql = "SELECT * FROM Clients WHERE Email IS NULL OR Email = ''";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Client client = new Client(
                        rs.getInt("ClientID"),
                        rs.getString("FullName"),
                        rs.getDate("BirthDate").toLocalDate(),
                        rs.getString("Phone"),
                        rs.getString("Email"),
                        rs.getInt("Discount"));
                clients.add(client);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return clients;
    }

}
