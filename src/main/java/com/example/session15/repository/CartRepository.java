package com.example.session15.repository;

import com.example.session15.model.Cart;
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
public class CartRepository {

    private static final Logger LOGGER = Logger.getLogger(CartRepository.class.getName());
    private final ConnectionDB connectionDB;

    @Autowired
    public CartRepository(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    public boolean save(Cart cart) {
        String sql = "INSERT INTO cart (id_user, id_product, quantity) VALUES (?, ?, ?)";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cart.getIdUser());
            stmt.setString(2, cart.getIdProduct());
            stmt.setInt(3, cart.getQuantity());
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Inserted cart for user " + cart.getIdUser() + ", product " + cart.getIdProduct() + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error saving cart for user " + cart.getIdUser() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<Cart> findByIdUser(String idUser) {
        List<Cart> carts = new ArrayList<>();
        String sql = "SELECT * FROM cart WHERE id_user = ?";

        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idUser);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Cart cart = new Cart(
                        rs.getLong("id_cart"),
                        rs.getString("id_user"),
                        rs.getString("id_product"),
                        rs.getInt("quantity")
                );
                carts.add(cart);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding carts for user " + idUser + ": " + e.getMessage());
            e.printStackTrace();
        }
        return carts;
    }

    public boolean delete(Cart cart) {
        String sql = "DELETE FROM cart WHERE id_cart = ?";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, cart.getIdCart());
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Deleted cart with id " + cart.getIdCart() + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error deleting cart with id " + cart.getIdCart() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}