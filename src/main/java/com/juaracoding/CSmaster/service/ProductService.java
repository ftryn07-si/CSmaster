package com.juaracoding.CSmaster.service;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/8/2023 6:13 AM
@Last Modified 3/8/2023 6:13 AM
Version 1.0
*/



import com.juaracoding.CSmaster.model.Product;
import com.juaracoding.CSmaster.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private ProductRepo productRepository;

    public ProductService(ProductRepo productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> getProductById(Long id, String x) {

        return productRepository.findById(id);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).get();

    }
}
