package com.juaracoding.CSmaster.dto;

import com.juaracoding.CSmaster.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositry extends JpaRepository<Product, Long> {

}
