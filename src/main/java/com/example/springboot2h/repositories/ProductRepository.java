package com.example.springboot2h.repositories;

import com.example.springboot2h.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductName(String name); //Chỉ cần tạo hàm theo đúng định dạng Spring Boot tự biết
}
