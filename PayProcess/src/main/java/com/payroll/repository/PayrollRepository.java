

package com.payroll.repository;

import com.payroll.model.Payroll;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT p FROM Payroll p WHERE p.employeeId = :employeeId AND FUNCTION('YEAR', p.payDate) = :year AND FUNCTION('MONTH', p.payDate) = :month")
    Payroll findPayrollByEmployeeIdAndMonthYear(@Param("employeeId") Long employeeId, @Param("year") int year, @Param("month") int month);
   
    @Query("SELECT SUM(p.basicPay), SUM(p.houseRentAllowance), SUM(p.specialAllowance), " +
            "SUM(p.epf), SUM(p.incomeTax), SUM(p.professionalTax) " +
            "FROM Payroll p " +
            "WHERE p.employeeId = :employeeId AND p.payDate BETWEEN :startDate AND :endDate")
     List<Object[]> findYearToDateValues(@Param("employeeId") Long employeeId,
                                         @Param("startDate") LocalDate startDate,
                                         @Param("endDate") LocalDate endDate);
}
