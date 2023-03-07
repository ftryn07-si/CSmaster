package com.juaracoding.CSmaster.repo;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/7/2023 11:39 PM
@Last Modified 3/7/2023 11:39 PM
Version 1.0
*/



import com.juaracoding.CSmaster.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long>{

}