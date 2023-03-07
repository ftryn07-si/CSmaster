package com.juaracoding.CSmaster.repo;


import com.juaracoding.CSmaster.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepo extends JpaRepository<Employee, Long> {

}