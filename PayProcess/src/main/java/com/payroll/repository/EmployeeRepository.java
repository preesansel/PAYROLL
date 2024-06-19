package com.payroll.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.payroll.dto.EmployeeDetails;
import com.payroll.dto.EmployeeSalaryDTO;
import com.payroll.model.Employee;


@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query("SELECT new com.payroll.dto.EmployeeSalaryDTO(e.employeeId, e.dateOfJoining, s.basicPay, s.houseRentAllowance, s.specialAllowance, s.epf) " +
	           "FROM Employee e JOIN Salary s ON e.employeeId = s.employeeId")
	    List<EmployeeSalaryDTO> findEmployeeSalaryDetails();
	
	
	 @Query("SELECT new com.payroll.dto.EmployeeDetails(e.employeeId, e.employeeName, e.designation, e.dateOfJoining, e.panNumber, e.department, e.location, e.uanNumber, e.pfNumber, e.bankName, e.bankAccountNumber) FROM Employee e WHERE e.employeeId = :employeeId")
	    EmployeeDetails findEmployeeDetailsById(@Param("employeeId") Long employeeId);
	
	
}