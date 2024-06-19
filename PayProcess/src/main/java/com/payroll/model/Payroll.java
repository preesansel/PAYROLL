
package com.payroll.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payroll")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_id")
    private Long payId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "pay_date", nullable = false)
    private LocalDate payDate;

    @Column(name = "basic_pay", nullable = false)
    private Double basicPay;

    @Column(name = "house_rent_allowance", nullable = false)
    private Double houseRentAllowance;

    @Column(name = "special_allowance", nullable = false)
    private Double specialAllowance;

    @Column(name = "epf", nullable = false)
    private Double epf;

    @Column(name = "income_tax", nullable = false)
    private Double incomeTax;

    @Column(name = "professional_tax", nullable = false)
    private Double professionalTax;

    @Column(name = "lop", nullable = false)
    private int lop;

    // Utility method to round to two decimal places
    private Double round(Double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    public void setBasicPay(Double basicPay) {
        this.basicPay = round(basicPay);
    }

    public void setHouseRentAllowance(Double houseRentAllowance) {
        this.houseRentAllowance = round(houseRentAllowance);
    }

    public void setSpecialAllowance(Double specialAllowance) {
        this.specialAllowance = round(specialAllowance);
    }

    public void setEpf(Double epf) {
        this.epf = round(epf);
    }

    public void setIncomeTax(Double incomeTax) {
        this.incomeTax = round(incomeTax);
    }

    public void setProfessionalTax(Double professionalTax) {
        this.professionalTax = round(professionalTax);
    } 


}
