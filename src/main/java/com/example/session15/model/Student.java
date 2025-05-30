package com.example.session15.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Student {
    private String studentId;
    private String name;
    private int age;
    private String className;
    private String email;
    private String address;
    private String phone;
}
