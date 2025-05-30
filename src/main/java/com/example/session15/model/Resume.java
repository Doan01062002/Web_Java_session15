package com.example.session15.model;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Resume {
    private Long id;

    @NotNull(message = "Tên không được để trống")
    @Size(min = 2, max = 100, message = "Tên phải có độ dài từ 2 đến 100 ký tự")
    private String fullName;

    @NotNull(message = "email không được để trống")
    @Email(message = "email không hợp lệ")
    private String email;

    @NotNull(message = "số điện thoại không được để trống")
    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "số điện thoại không hợp lệ")
    private String phoneNumber;

    private String education;

    private String experience;

    private String skills;
}