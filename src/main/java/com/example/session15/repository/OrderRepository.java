package com.example.session15.repository;

import com.example.session15.model.Order;
import com.example.session15.util.ConnectionDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Repository
public class OrderRepository {

    private static final Logger LOGGER = Logger.getLogger(OrderRepository.class.getName());
    private final ConnectionDB connectionDB;

    @Autowired
    public OrderRepository(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    public boolean save(Order order) {
        String sql = "INSERT INTO `order` (order_id, id_user, recipient_name, address, phonenumber) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, order.getOrderId());
            stmt.setString(2, order.getIdUser());
            stmt.setString(3, order.getRecipientName());
            stmt.setString(4, order.getAddress());
            stmt.setString(5, order.getPhonenumber());
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Inserted order " + order.getOrderId() + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error saving order " + order.getOrderId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM `order`";

        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Order order = new Order(
                        rs.getString("order_id"),
                        rs.getString("id_user"),
                        rs.getString("recipient_name"),
                        rs.getString("address"),
                        rs.getString("phonenumber")
                );
                orders.add(order);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding all orders: " + e.getMessage());
            e.printStackTrace();
        }
        return orders;
    }

    public Order findById(String orderId) {
        String sql = "SELECT * FROM `order` WHERE order_id = ?";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Order(
                        rs.getString("order_id"),
                        rs.getString("id_user"),
                        rs.getString("recipient_name"),
                        rs.getString("address"),
                        rs.getString("phonenumber")
                );
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}