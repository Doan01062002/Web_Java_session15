package com.example.session15.repository;

import com.example.session15.model.Resume;
import com.example.session15.util.ConnectionDB;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ResumeRepository {
    private final ConnectionDB connectionDB;

    public ResumeRepository(ConnectionDB connectionDB) {
        this.connectionDB = connectionDB;
    }

    public void save(Resume resume) throws SQLException {
        String sql = "INSERT INTO resumes (full_name, email, phone_number, education, experience, skills) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, resume.getFullName());
            stmt.setString(2, resume.getEmail());
            stmt.setString(3, resume.getPhoneNumber());
            stmt.setString(4, resume.getEducation());
            stmt.setString(5, resume.getExperience());
            stmt.setString(6, resume.getSkills());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                resume.setId(rs.getLong(1));
            }
        }
    }

    public void update(Resume resume) throws SQLException {
        String sql = "UPDATE resumes SET full_name = ?, email = ?, phone_number = ?, education = ?, experience = ?, skills = ? WHERE id = ?";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, resume.getFullName());
            stmt.setString(2, resume.getEmail());
            stmt.setString(3, resume.getPhoneNumber());
            stmt.setString(4, resume.getEducation());
            stmt.setString(5, resume.getExperience());
            stmt.setString(6, resume.getSkills());
            stmt.setLong(7, resume.getId());
            stmt.executeUpdate();
        }
    }

    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM resumes WHERE id = ?";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }

    public Resume findById(Long id) throws SQLException {
        String sql = "SELECT * FROM resumes WHERE id = ?";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Resume resume = new Resume();
                resume.setId(rs.getLong("id"));
                resume.setFullName(rs.getString("full_name"));
                resume.setEmail(rs.getString("email"));
                resume.setPhoneNumber(rs.getString("phone_number"));
                resume.setEducation(rs.getString("education"));
                resume.setExperience(rs.getString("experience"));
                resume.setSkills(rs.getString("skills"));
                return resume;
            }
            return null;
        }
    }

    public List<Resume> findAll() throws SQLException {
        List<Resume> resumes = new ArrayList<>();
        String sql = "SELECT * FROM resumes";
        try (Connection conn = connectionDB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Resume resume = new Resume();
                resume.setId(rs.getLong("id"));
                resume.setFullName(rs.getString("full_name"));
                resume.setEmail(rs.getString("email"));
                resume.setPhoneNumber(rs.getString("phone_number"));
                resume.setEducation(rs.getString("education"));
                resume.setExperience(rs.getString("experience"));
                resume.setSkills(rs.getString("skills"));
                resumes.add(resume);
            }
        }
        return resumes;
    }
}