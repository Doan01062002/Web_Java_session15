package com.example.session15.controller;

import com.example.session15.model.Product;
import com.example.session15.model.SearchForm;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class ProductController {

    // Danh sách sản phẩm mẫu
    private final List<Product> products = Arrays.asList(
            new Product("P001", "Laptop Dell XPS", 1500.0),
            new Product("P002", "iPhone 14", 999.0),
            new Product("P003", "Samsung Galaxy S23", 899.0),
            new Product("P004", "MacBook Pro", 2000.0)
    );

    @GetMapping("/search")
    public String showSearchForm(Model model){
        model.addAttribute("searchForm", new SearchForm());
        return "search";
    }

    @PostMapping("/search")
    public String processSearch(@Valid @ModelAttribute("searchForm") SearchForm searchForm, BindingResult result, Model model){
        if (result.hasErrors()){
            return "search";
        }

        String keyword = searchForm.getKeyword().toLowerCase();
        List<Product> filteredProducts = products.stream().filter(p-> p.getName().toLowerCase().contains(keyword)).collect(Collectors.toList());

        model.addAttribute("products", filteredProducts);
        model.addAttribute("keyword", keyword);

        return "search";
    }
}
