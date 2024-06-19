package com.payroll.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SalaryDTO {
    private Long empId;
    private Double basicSalary;
    private Double houseRentAllowance;
    private Double specialAllowance;
    private Double providentFund;
    private Double bonus;
}
