package com.payroll.service;



import com.payroll.dto.EmployeeDTO;
import com.payroll.dto.SalaryDTO;
import com.payroll.model.Payroll;
import com.payroll.repository.PayrollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.YearMonth;
import java.util.List;
import java.util.Map;

@Service
public class PayrollService {



    @Autowired
    private PayrollRepository payrollRepository;
    
    
    private final String EMPLOYEE_SERVICE_URL = "http://employee-service-url/employees";
    private final String SALARY_SERVICE_URL = "http://salary-service-url/salary/";


    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<String> getEmployeeData() {
        ResponseEntity<String> response = restTemplate.getForEntity(EMPLOYEE_SERVICE_URL, String.class);
        return response;
    }
    
    
    public ResponseEntity<String> getSalaryDetails(int empId) {
        String salaryUrl = SALARY_SERVICE_URL + empId;
        return restTemplate.getForEntity(salaryUrl, String.class);
    }
    
//    private static final String EMPLOYEE_URL ="http://employee-service/employees";
//
//    public EmployeeDTO[] fetchAllEmployees() {
//        return restTemplate.getForObject(EMPLOYEE_URL, EmployeeDTO[].class);
//    }
//    public SalaryDTO fetchSalaryDetails(Long empId) {
//        return restTemplate.getForObject(SALARY_SERVICE_URL + "/" + empId, SalaryDTO.class);
//    }
//
//    public TaxResponseDTO calculateTaxes(TaxRequestDTO taxRequestDTO) {
//        return restTemplate.postForObject(TAX_SERVICE_URL, taxRequestDTO, TaxResponseDTO.class);
//    }


//    private static final String EMPLOYEE_SERVICE_URL = "http://employee-service/employees";
//    private static final String SALARY_SERVICE_URL = "http://salary-service/salary/";
//    private static final String TAX_SERVICE_URL = "http://tax-service/tax";
//
//    public void calculatePayroll() {
//        List<Map<String, Object>> employees = fetchAllEmployees();
//        YearMonth currentMonth = YearMonth.now();
//        int currentMonthValue = currentMonth.getMonthValue();
//        int currentYear = currentMonth.getYear();
//        
//        for (Map<String, Object> employee : employees) {
//            Long empId = ((Number) employee.get("id")).longValue();
//            Payroll payroll = payrollRepository.findByEmpIdAndPayMonthAndPayYear(empId, currentMonthValue, currentYear);
//            if (payroll == null) {
//                payroll = new Payroll();
//                payroll.setEmpId(empId);
//                payroll.setPayMonth(currentMonthValue);
//                payroll.setPayYear(currentYear);
//                payroll.setLop(0.0);  // default value if not available
//            }

//            Map<String, Object> salaryDetails = fetchSalaryDetails(empId);
//            Map<String, Object> taxDetails = fetchTaxDetails(salaryDetails);
//            calculateAndSavePayroll(payroll, salaryDetails, taxDetails);
//        }
//    }
//
//    private List<Map<String, Object>> fetchAllEmployees() {
//        return restTemplate.getForObject(EMPLOYEE_SERVICE_URL, List.class);
//    }
//
//    private Map<String, Object> fetchSalaryDetails(Long empId) {
//        return restTemplate.getForObject(SALARY_SERVICE_URL + empId, Map.class);
//    }
//
//    private Map<String, Object> fetchTaxDetails(Map<String, Object> salaryDetails) {
//        return restTemplate.postForObject(TAX_SERVICE_URL, salaryDetails, Map.class);
//    }
//
//    private void calculateAndSavePayroll(Payroll payroll, Map<String, Object> salaryDetails, Map<String, Object> taxDetails) {
//        YearMonth currentMonth = YearMonth.now();
//        int daysInMonth = currentMonth.lengthOfMonth();
//
//        double basicPay = ((Number) salaryDetails.get("basicPay")).doubleValue();
//        double houseRentAllowance = ((Number) salaryDetails.get("houseRentAllowance")).doubleValue();
//        double specialAllowance = ((Number) salaryDetails.get("specialAllowance")).doubleValue();
//
//        double dailyBasicPay = basicPay / 365;
//        double dailyHouseRentAllowance = houseRentAllowance / 365;
//        double dailySpecialAllowance = specialAllowance / 365;
//
//        double lop = payroll.getLop();
//        double standardDays = daysInMonth;
//        double workDays = standardDays - lop;
//
//        double monthlyBasicPay = dailyBasicPay * workDays;
//        double monthlyHouseRentAllowance = dailyHouseRentAllowance * workDays;
//        double monthlySpecialAllowance = dailySpecialAllowance * workDays;
//
//        double grossEarnings = monthlyBasicPay + monthlyHouseRentAllowance + monthlySpecialAllowance;
//
//        double professionalTax = ((Number) taxDetails.get("professionalTax")).doubleValue();
//        double incomeTax = ((Number) taxDetails.get("incomeTax")).doubleValue();
//
//        double grossDeductions = professionalTax + incomeTax;
//        double netPay = grossEarnings - grossDeductions;
//
//        payroll.setBasicPay(monthlyBasicPay);
//        payroll.setHouseRentAllowance(monthlyHouseRentAllowance);
//        payroll.setSpecialAllowance(monthlySpecialAllowance);
//        payroll.setGrossEarnings(grossEarnings);
//        payroll.setProfessionalTax(professionalTax);
//        payroll.setIncomeTax(incomeTax);
//        payroll.setGrossDeductions(grossDeductions);
//        payroll.setNetPay(netPay);
//
//        payrollRepository.save(payroll);
//    }
}



public void calculateAndSavePayroll() {
    // Fetch employee details
    List<Employee> employees = fetchEmployees();

    // Fetch salary details for each employee
    Map<Long, SalaryDetails> salaryDetailsMap = fetchSalaryDetails(employees);

    // Fetch tax details for each employee
    Map<Long, TaxDetails> taxDetailsMap = fetchTaxDetails(employees);

    // Calculate payroll for each employee
    List<Payroll> payrolls = employees.stream()
            .map(employee -> calculatePayroll(employee, salaryDetailsMap.get(employee.getEmpId()), taxDetailsMap.get(employee.getEmpId())))
            .collect(Collectors.toList());

    // Save the calculated payroll data to the database
    payrollRepository.saveAll(payrolls);
}
private List<Employee> fetchEmployees() {
    return List.of(restTemplate.getForObject(EMPLOYEE_SERVICE_URL, Employee[].class));
}

private Map<Long, SalaryDetails> fetchSalaryDetails(List<Employee> employees) {
    List<Long> empIds = employees.stream().map(Employee::getEmpId).collect(Collectors.toList());
    SalaryDetails[] salaryDetailsArray = restTemplate.postForObject(SALARY_SERVICE_URL, empIds, SalaryDetails[].class);
    return List.of(salaryDetailsArray).stream().collect(Collectors.toMap(SalaryDetails::getEmpId, sd -> sd));
}

private Map<Long, TaxDetails> fetchTaxDetails(List<Employee> employees) {
    List<Long> empIds = employees.stream().map(Employee::getEmpId).collect(Collectors.toList());
    TaxDetails[] taxDetailsArray = restTemplate.postForObject(TAX_SERVICE_URL, empIds, TaxDetails[].class);
    return List.of(taxDetailsArray).stream().collect(Collectors.toMap(TaxDetails::getEmpId, td -> td));
}
private Payroll calculatePayroll(Employee employee, SalaryDetails salaryDetails, TaxDetails taxDetails) {
    Payroll payroll = new Payroll();
    payroll.setEmpId(employee.getEmpId());
    payroll.setPayMonth(6); // Assuming current month
    payroll.setPayYear(2024); // Assuming current year
    payroll.setBasicPay(salaryDetails.getBasicPay());
    payroll.setHouseRentAllowance(salaryDetails.getHouseRentAllowance());
    payroll.setSpecialAllowance(salaryDetails.getSpecialAllowance());
    payroll.setProvidentFund(salaryDetails.getProvidentFund());
    payroll.setIncomeTax(taxDetails.getIncomeTax());
    payroll.setProfessionalTax(taxDetails.getProfessionalTax());
    payroll.setLop(0); // Assuming no loss of pay for simplicity
    return payroll;
}


