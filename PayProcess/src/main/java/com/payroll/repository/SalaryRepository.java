package com.payroll.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.payroll.model.Salary;



@Repository
public interface SalaryRepository extends JpaRepository<Salary, Long> {
    
}
