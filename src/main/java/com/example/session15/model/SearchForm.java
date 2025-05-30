package com.example.session15.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SearchForm {
    @NotBlank(message = "Trường tìm kiếm không được để trống")
    private String keyword;
}