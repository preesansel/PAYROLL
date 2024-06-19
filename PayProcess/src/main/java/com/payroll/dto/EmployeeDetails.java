package com.payroll.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDetails {
    private Long employeeId;
    private String employeeName;
    private String designation;
    private Date doj;
    private String panId;
    private String department;
    private String location;
    private String uanNo;
    private String pfNo;
    private String bankName;
    private String accountNo;
}

