package com.example.session15.repository;

import com.example.session15.model.OrderDetail;
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
public class OrderDetailRepository {

    private static final Logger LOGGER = Logger.getLogger(OrderDetailRepository.class.getName());
    private final ConnectionDB connectionDB;

    @Autowired
    public OrderDetailRepository(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    public boolean save(OrderDetail orderDetail) {
        String sql = "INSERT INTO order_detail (order_id, product_id, quantity, current_price) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderDetail.getOrderId());
            stmt.setString(2, orderDetail.getProductId());
            stmt.setInt(3, orderDetail.getQuantity());
            stmt.setDouble(4, orderDetail.getCurrentPrice());
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Inserted order detail for order " + orderDetail.getOrderId() + ", product " + orderDetail.getProductId() + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error saving order detail for order " + orderDetail.getOrderId() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<OrderDetail> findByOrderId(String orderId) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT * FROM order_detail WHERE order_id = ?";

        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                OrderDetail orderDetail = new OrderDetail(
                        rs.getLong("id"),
                        rs.getString("order_id"),
                        rs.getString("product_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("current_price")
                );
                orderDetails.add(orderDetail);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding order details for order " + orderId + ": " + e.getMessage());
            e.printStackTrace();
        }
        return orderDetails;
    }
}