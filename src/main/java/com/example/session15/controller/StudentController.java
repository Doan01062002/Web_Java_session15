package com.example.session15.controller;

import com.example.session15.model.Student;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Arrays;
import java.util.List;

@Controller
public class StudentController {

    @GetMapping("/students")
    public String getStudents(Model model) {
        List<Student> students = Arrays.asList(
                new Student("SV001", "Nguyen Van A", 20, "KTPM01", "a@example.com", "Hanoi", "0123456789"),
                new Student("SV002", "Tran Thi B", 21, "KTPM02", "b@example.com", "HCM City", "0987654321"),
                new Student("SV003", "Le Van C", 19, "KTPM01", "c@example.com", "Da Nang", "0912345678")
        );

        model.addAttribute("students", students);

        return "student-list";
    }
}