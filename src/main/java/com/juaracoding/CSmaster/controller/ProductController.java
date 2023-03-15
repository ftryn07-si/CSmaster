package com.juaracoding.CSmaster.controller;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/12/2023 3:28 PM
@Last Modified 3/12/2023 3:28 PM
Version 1.0
*/

import java.util.List;

import com.juaracoding.CSmaster.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.juaracoding.CSmaster.service.ProductService;

@Controller
@RequestMapping("/api/item")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/listProducts.html")
    public String showExampleView(Model model)
    {
        List<Product> products = productService.getAllProduct();
        model.addAttribute("products", products);
        return "/listProducts.html";
    }
    @GetMapping("/")
    public String showAddProduct()
    {

        return "/addProduct";
    }

    @PostMapping("/addP")
    public String saveProduct(@RequestParam("file") MultipartFile file,
                              @RequestParam("pname") String name,
                              @RequestParam("price") int price,
                              @RequestParam("desc") String desc)
    {
        productService.saveProductToDB(file, name, desc, price);
        return "redirect:/listProducts.html";
    }

    @GetMapping("/deleteProd/{id}")
    public String deleteProduct(@PathVariable("id") Long id)
    {

        productService.deleteProductById(id);
        return "redirect:/listProducts.html";
    }

    @PostMapping("/changeName")
    public String changePname(@RequestParam("id") Long id,
                              @RequestParam("newPname") String name)
    {
        productService.chageProductName(id, name);
        return "redirect:/listProducts.html";
    }
    @PostMapping("/changeDescription")
    public String changeDescription(@RequestParam("id") Long id ,
                                    @RequestParam("newDescription") String description)
    {
        productService.changeProductDescription(id, description);
        return "redirect:/listProducts.html";
    }

    @PostMapping("/changePrice")
    public String changePrice(@RequestParam("id") Long id ,
                              @RequestParam("newPrice") int price)
    {
        productService.changeProductPrice(id, price);
        return "redirect:/listProducts.html";
    }
}
