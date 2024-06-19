package com.payroll.repository;



import com.payroll.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    
}
import com.payroll.model.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT "
            + "SUM(p.basicPay) AS yearToDateBasicPay, "
            + "SUM(p.houseRentAllowance) AS yearToDateHra, "
            + "SUM(p.specialAllowance) AS yearToDateSpecialAllowance, "
            + "SUM(p.incomeTax) AS yearToDateIncomeTax, "
            + "SUM(p.professionalTax) AS yearToDateProfessionalTax, "
            + "SUM(p.providentFund) AS yearToDateProvidentFund "
            + "FROM Payroll p "
            + "WHERE p.empId = :empId "
            + "AND ((p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = :nextYear AND p.payMonth <= 3))")
    Object[] calculateYearToDateValuesForFinancialYear(
            @Param("empId") Long empId, 
            @Param("year") int year, 
            @Param("nextYear") int nextYear);

    @Query("SELECT "
            + "SUM(p.basicPay + p.houseRentAllowance + p.specialAllowance) AS grossEarnings, "
            + "SUM(p.incomeTax + p.professionalTax + p.providentFund) AS grossDeductions "
            + "FROM Payroll p "
            + "WHERE p.empId = :empId "
            + "AND p.payYear = :year "
            + "AND p.payMonth = :month")
    Object[] calculateGrossSalaryAndDeductions(
            @Param("empId") Long empId, 
            @Param("year") int year, 
            @Param("month") int month);
}


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    public YearToDateValues calculateYearToDateValuesForFinancialYear(Long empId, int year) {
        int nextYear = year + 1;
        Object[] result = payrollRepository.calculateYearToDateValuesForFinancialYear(empId, year, nextYear);
        
        if (result != null && result.length == 6) {
            double yearToDateBasicPay = ((Number) result[0]).doubleValue();
            double yearToDateHra = ((Number) result[1]).doubleValue();
            double yearToDateSpecialAllowance = ((Number) result[2]).doubleValue();
            double yearToDateIncomeTax = ((Number) result[3]).doubleValue();
            double yearToDateProfessionalTax = ((Number) result[4]).doubleValue();
            double yearToDateProvidentFund = ((Number) result[5]).doubleValue();
            
            return new YearToDateValues(yearToDateBasicPay, yearToDateHra, yearToDateSpecialAllowance,
                    yearToDateIncomeTax, yearToDateProfessionalTax, yearToDateProvidentFund);
        }
        
        return null; // Handle null or insufficient data
    }

    public GrossSalaryAndDeductions calculateGrossSalaryAndDeductions(Long empId, int year, int month) {
        Object[] result = payrollRepository.calculateGrossSalaryAndDeductions(empId, year, month);
        
        if (result != null && result.length == 2) {
            double grossEarnings = ((Number) result[0]).doubleValue();
            double grossDeductions = ((Number) result[1]).doubleValue();
            
            return new GrossSalaryAndDeductions(grossEarnings, grossDeductions);
        }
        
        return null; // Handle null or insufficient data
    }
}
//////////////////////////////////////////////////////////
package com.payroll.controller;

import com.payroll.dto.GrossSalaryAndDeductions;
import com.payroll.dto.YearToDateValues;
import com.payroll.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping("/yearToDateValues")
    public ResponseEntity<YearToDateValues> getYearToDateValues(@RequestParam Long empId, @RequestParam int year) {
        YearToDateValues values = payrollService.calculateYearToDateValuesForFinancialYear(empId, year);
        if (values != null) {
            return ResponseEntity.ok(values);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/grossSalaryAndDeductions")
    public ResponseEntity<GrossSalaryAndDeductions> getGrossSalaryAndDeductions(@RequestParam Long empId, @RequestParam int year, @RequestParam int month) {
        GrossSalaryAndDeductions values = payrollService.calculateGrossSalaryAndDeductions(empId, year, month);
        if (values != null) {
            return ResponseEntity.ok(values);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
//////////////////////////////////////////////////////////////////////
package com.payroll.service;

import com.payroll.dto.GrossSalaryAndDeductions;
import com.payroll.dto.YearToDateValues;
import com.payroll.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    public YearToDateValues calculateYearToDateValuesForFinancialYear(Long empId, int year) {
        int nextYear = year + 1;
        Object[] result = payrollRepository.calculateYearToDateValuesForFinancialYear(empId, year, nextYear);

        if (result != null && result.length == 6) {
            double yearToDateBasicPay = ((Number) result[0]).doubleValue();
            double yearToDateHra = ((Number) result[1]).doubleValue();
            double yearToDateSpecialAllowance = ((Number) result[2]).doubleValue();
            double yearToDateIncomeTax = ((Number) result[3]).doubleValue();
            double yearToDateProfessionalTax = ((Number) result[4]).doubleValue();
            double yearToDateProvidentFund = ((Number) result[5]).doubleValue();

            return new YearToDateValues(yearToDateBasicPay, yearToDateHra, yearToDateSpecialAllowance,
                    yearToDateIncomeTax, yearToDateProfessionalTax, yearToDateProvidentFund);
        }

        return null; // Handle null or insufficient data
    }

    public GrossSalaryAndDeductions calculateGrossSalaryAndDeductions(Long empId, int year, int month) {
        Object[] result = payrollRepository.calculateGrossSalaryAndDeductions(empId, year, month);

        if (result != null && result.length == 2) {
            double grossEarnings = ((Number) result[0]).doubleValue();
            double grossDeductions = ((Number) result[1]).doubleValue();

            return new GrossSalaryAndDeductions(grossEarnings, grossDeductions);
        }

        return null; // Handle null or insufficient data
    }
}

