
package com.payroll.controller;

import com.payroll.dto.EmployeeDetails;
import com.payroll.dto.PayrollDetails;
import com.payroll.model.Payroll;
import com.payroll.service.PayrollService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/payroll")
public class PayrollController {

    private PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    @GetMapping("/calculate")
    public ResponseEntity<List<Payroll>> calculatePayrolls() {
        List<Payroll> payrolls = payrollService.calculatePayrolls();
        return ResponseEntity.ok(payrolls);
    }

    @GetMapping("/details")
    public ResponseEntity<PayrollDetails> getPayrollDetails(
            @RequestParam Long employeeId,
            @RequestParam int year,
            @RequestParam int month) {
        PayrollDetails payrollDetails = payrollService.getPayrollDetails(employeeId, year, month);
        return ResponseEntity.ok(payrollDetails);
    }

    @GetMapping("/EmployeeDetails/{employeeId}")
    public ResponseEntity<EmployeeDetails> getEmployeeDetails(@PathVariable Long employeeId) {
        EmployeeDetails employeeDTO = payrollService.getEmployeeDetails(employeeId);
        return ResponseEntity.ok(employeeDTO);
    }
}
