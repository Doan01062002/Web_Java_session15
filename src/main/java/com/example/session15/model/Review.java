package com.example.session15.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private Long id;
    private String idProduct;
    private String idUser;
    private Integer rating;
    private String comment;
}