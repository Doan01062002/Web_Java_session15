package com.example.session15.repository;

import com.example.session15.model.Review;
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
public class ReviewRepository {

    private static final Logger LOGGER = Logger.getLogger(ReviewRepository.class.getName());
    private final ConnectionDB connectionDB;

    @Autowired
    public ReviewRepository(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    public List<Review> findByIdProduct(String idProduct) {
        List<Review> reviews = new ArrayList<>();
        String sql = "SELECT * FROM review WHERE id_product = ?";

        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, idProduct);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Review review = new Review(
                        rs.getLong("id"),
                        rs.getString("id_product"),
                        rs.getString("id_user"),
                        rs.getInt("rating"),
                        rs.getString("comment")
                );
                reviews.add(review);
            }
        } catch (SQLException e) {
            LOGGER.severe("Error finding reviews for product " + idProduct + ": " + e.getMessage());
            e.printStackTrace();
        }
        return reviews;
    }

    public boolean save(Review review) {
        String sql = "INSERT INTO review (id_product, id_user, rating, comment) VALUES (?, ?, ?, ?)";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, review.getIdProduct());
            stmt.setString(2, review.getIdUser() != null ? review.getIdUser() : "");
            stmt.setObject(3, review.getRating()); // Permet null
            stmt.setString(4, review.getComment() != null ? review.getComment() : "");
            int rowsAffected = stmt.executeUpdate();
            LOGGER.info("Inserted review for product " + review.getIdProduct() + ", rows affected: " + rowsAffected);
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.severe("Error saving review for product " + review.getIdProduct() + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}