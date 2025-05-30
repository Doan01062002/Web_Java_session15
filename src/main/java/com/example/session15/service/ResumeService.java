package com.example.session15.service;

import com.example.session15.model.Resume;
import com.example.session15.repository.ResumeRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public void save(Resume resume) throws SQLException {
        resumeRepository.save(resume);
    }

    public void update(Resume resume) throws SQLException {
        resumeRepository.update(resume);
    }

    public void delete(Long id) throws SQLException {
        resumeRepository.delete(id);
    }

    public Resume findById(Long id) throws SQLException {
        return resumeRepository.findById(id);
    }

    public List<Resume> findAll() throws SQLException {
        return resumeRepository.findAll();
    }
}