package com.juaracoding.CSmaster.repo;

import com.juaracoding.CSmaster.model.Category;
import com.juaracoding.CSmaster.model.Userz;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface UserRepo extends JpaRepository<Userz,Long> {

    public List<Userz> findByEmail(String value);
    public List<Userz> findByEmailOrNoHPOrUsername(String emails, String noHP, String userName);
//    Page<Userz> findByIsDelete(Pageable page , byte byteIsDelete);
//    Page<Userz> findByIsDeleteAndNamaCategoryContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
//    Page<Userz> findByIsDeleteAndIdCategoryContainsIgnoreCase(Pageable page , byte byteIsDelete, Long values);

}