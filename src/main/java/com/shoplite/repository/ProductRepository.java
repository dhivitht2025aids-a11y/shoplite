package com.shoplite.repository;

import com.shoplite.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByStockQuantityLessThan(Integer reorderThreshold);
}