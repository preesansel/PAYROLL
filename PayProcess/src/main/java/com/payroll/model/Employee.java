package com.payroll.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Date;


@Entity
@Table(name = "employees")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employeeId;

    @Column(name = "employee_name", nullable = true)
    private String employeeName;

    @Column(name = "designation",nullable = true)
    private String designation;

   
    @Column(name ="doj", nullable = true)
    private Date dateOfJoining;

    @Column(name = "pan_id", nullable = true)
    private String panNumber;

    @Column(name="department",nullable = true)
    private String department;

    @Column(name="location",nullable = true)
    private String location;

    @Column(name = "uan_no", nullable = true)
    private String uanNumber;

    @Column(name = "pf_no", nullable = true)
    private String pfNumber;

    @Column(name = "bank_name", nullable = true)
    private String bankName;

    @Column(name = "account_name", nullable = true)
    private String bankAccountNumber;
}
