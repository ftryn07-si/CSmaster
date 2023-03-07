package com.juaracoding.CSmaster.controller;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/7/2023 11:35 PM
@Last Modified 3/7/2023 11:35 PM
Version 1.0
*/

import com.juaracoding.CSmaster.configuration.OtherConfig;
import com.juaracoding.CSmaster.model.Category;
import com.juaracoding.CSmaster.service.CategoryService;


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
@RequestMapping("/api/item")

public class CategoryController {

    private CategoryService categoryService;

    private MappingAttribute mappingAttribute = new MappingAttribute();
    private Map<String,Object> objectMapper = new HashMap<String,Object>();

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // handler method to handle list students and return mode and view
    @GetMapping("/v1/categorys")
    public String listCategorys(Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y"))
        {
            mappingAttribute.setAttribute(model,objectMapper,request);//untuk set session
            if(request.getAttribute("USR_ID",1)==null){
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("categorys", categoryService.getAllCategorys());
        return "categorys";
    }

    @GetMapping("/v1/categorys/new")
    public String createCategoryForm(Model model,WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        // create student object to hold student form data
        Category category = new Category();
        model.addAttribute("category", category);
        return "create_category";

    }

    @PostMapping("/v1/categorys")
    public String saveCategory(@ModelAttribute("category") Category category,Model model,WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        categoryService.saveCategory(category);
        return "redirect:/api/item/v1/categorys";
    }

    @GetMapping("/v1/categorys/edit/{id}")
    public String editCategoryForm(@PathVariable("id") Long Id, Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        model.addAttribute("category", categoryService.getCategoryById(Id));
        return "edit_category";
    }

    @PostMapping("/v1/categorys/{id}")
    public String updateCategory(@PathVariable("id") Long id,
                                 @ModelAttribute("category") Category category,
                                 Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        // get student from database by id
        Category existingCategory = categoryService.getCategoryById(id);
        existingCategory.setId(id);

        existingCategory.setModifiedDate(new Date());
        existingCategory.setCategoryName(category.getCategoryName());


        // save updated student object
        categoryService.updateCategory(existingCategory);
        return "redirect:/api/item/v1/categorys";
    }

    // handler method to handle delete student request

    @GetMapping("/v1/categorys/{id}")
    public String deleteCategory(@PathVariable Long id, Model model, WebRequest request) {
        if(OtherConfig.getFlagSessionValidation().equals("y")) {
            mappingAttribute.setAttribute(model, objectMapper, request);//untuk set session
            if (request.getAttribute("USR_ID", 1) == null) {
                return "redirect:/api/check/logout";
            }
        }
        categoryService.deleteCategoryById(id);
        return "redirect:/categorys";
    }

}