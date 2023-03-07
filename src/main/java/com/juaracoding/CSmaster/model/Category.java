package com.juaracoding.CSmaster.model;/*
Created By IntelliJ IDEA 2022.2.3 (Community Edition)
Build #IU-222.4345.14, built on October 5, 2022
@Author Moh. Ikhsan a.k.a. Fitriyani
Java Developer
Created on 3/7/2023 11:37 PM
@Last Modified 3/7/2023 11:37 PM
Version 1.0
*/



import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "MstCategory")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CategoryName", nullable = false)
    private String categoryName;
    /*
           start audit trails
        */
    @Column(name ="CreatedDate")
    private Date createdDate = new Date();

    @Column(name = "CreatedBy")
    private Integer createdBy;

    @Column(name = "ModifiedDate")
    private Date modifiedDate;

    @ManyToOne
    @JoinColumn(name = "IDUser")
    private Userz userz;


    @Column(name = "IsDelete")
    private Byte isDelete = 1;
    /*
        end audit trails
     */

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Integer createdBy) {
        this.createdBy = createdBy;
    }

    public Date getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Date modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Userz getUserz() {
        return userz;
    }

    public void setUserz(Userz userz) {
        this.userz = userz;
    }

    public Byte getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}