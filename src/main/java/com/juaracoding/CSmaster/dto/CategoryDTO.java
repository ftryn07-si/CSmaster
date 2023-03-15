package com.juaracoding.CSmaster.dto;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/11/2023 1:47 AM
@Last Modified 3/11/2023 1:47 AM
Version 1.0
*/

import com.juaracoding.CSmaster.utils.ConstantMessage;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CategoryDTO {

    private Long idCategory;
    @NotNull
    @NotEmpty
    @Length(message = ConstantMessage.WARNING_MENU_NAME_LENGTH,max = 25)
    private String namaCategory;

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public String getNamaCategory() {
        return namaCategory;
    }

    public void setNamaCategory(String namaCategory) {
        this.namaCategory = namaCategory;
    }
}
