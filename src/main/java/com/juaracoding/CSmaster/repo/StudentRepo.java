package com.juaracoding.CSmaster.repo;

import com.juaracoding.CSmaster.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepo extends JpaRepository<Student, Long>{

}
