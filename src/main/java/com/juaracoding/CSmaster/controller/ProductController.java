package com.juaracoding.CSmaster.controller;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/7/2023 11:36 PM
@Last Modified 3/7/2023 11:36 PM
Version 1.0
*/



import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.model.Category;
import com.juaracoding.CSmaster.service.CategoryService;


import com.juaracoding.CSmaster.service.ProductService;
import com.juaracoding.CSmaster.utils.MappingAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.persistence.Id;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/api/prodak")

public class ProductController {

    private ProductService productService;

    private MappingAttribute mappingAttribute = new MappingAttribute();
    private Map<String, Object> objectMapper = new HashMap<String, Object>();

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // handler method to handle list students and return mode and view
    @GetMapping("/v1/pruducts")
    public String listProducts(Model model, WebRequest request) {
        if (OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("products", productService.getAllProducts());
        return "products";
    }
}