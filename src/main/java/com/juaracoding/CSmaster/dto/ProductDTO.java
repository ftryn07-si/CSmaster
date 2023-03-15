package com.juaracoding.CSmaster.dto;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/12/2023 11:57 AM
@Last Modified 3/12/2023 11:57 AM
Version 1.0
*/


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.juaracoding.CSmaster.model.Category;
import com.juaracoding.CSmaster.utils.ConstantMessage;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class ProductDTO{

    @Id
    @GeneratedValue(generator = "IDProduct")
    @GenericGenerator(name = "MstProduct", strategy = "ppid2")
    @Column(name = "IDProduct", nullable = false, length = 255)
    private String idProduct;
    @NotEmpty
    @NotNull
    @Column(name = "NamaProduct")
    private String namaProduct;

    @ManyToOne
    @JoinColumn(name = "IDCategoryProduct")
    private Category category;

    @Column(name = "DescriptionProduct")
    private String descriptionProduct;

    @Column(name = "MerkProduct")
    private String merkProduct;

    @Column(name = "ImageProduct")
    private String imageProduct;


    @NotNull
    private CategoryDTO namaCategory;

    public String getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(String idProduct) {
        this.idProduct = idProduct;
    }

    public String getNamaProduct() {
        return namaProduct;
    }

    public void setNamaProduct(String namaProduct) {
        this.namaProduct = namaProduct;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescriptionProduct() {
        return descriptionProduct;
    }

    public void setDescriptionProduct(String descriptionProduct) {
        this.descriptionProduct = descriptionProduct;
    }

    public String getMerkProduct() {
        return merkProduct;
    }

    public void setMerkProduct(String merkProduct) {
        this.merkProduct = merkProduct;
    }

    public String getImageProduct() {
        return imageProduct;
    }

    public void setImageProduct(String imageProduct) {
        this.imageProduct = imageProduct;
    }

    public CategoryDTO getNamaCategory() {
        return namaCategory;
    }

    public void setNamaCategory(CategoryDTO namaCategory) {
        this.namaCategory = namaCategory;
    }
}
