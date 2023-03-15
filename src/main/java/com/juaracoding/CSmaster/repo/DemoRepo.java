package com.juaracoding.CSmaster.repo;

import com.juaracoding.CSmaster.model.Demo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoRepo extends JpaRepository<Demo,Long> {

    Page<Demo> findByIsDelete(Pageable page , byte byteIsDelete);
    Page<Demo> findByIsDeleteAndNamaDemoContainsIgnoreCase(Pageable page , byte byteIsDelete, String values);
    Page<Demo> findByIsDeleteAndIdDemoContainsIgnoreCase(Pageable page , byte byteIsDelete, Long values);

}
