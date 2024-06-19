import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    @Query("SELECT " +
            "SUM(CASE WHEN (p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = (:year + 1) AND p.payMonth <= 3) THEN p.basicPay ELSE 0 END) AS ytdBasic, " +
            "SUM(CASE WHEN (p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = (:year + 1) AND p.payMonth <= 3) THEN p.houseRentAllowance ELSE 0 END) AS ytdHra, " +
            "SUM(CASE WHEN (p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = (:year + 1) AND p.payMonth <= 3) THEN p.specialAllowance ELSE 0 END) AS ytdSpecial, " +
            "SUM(CASE WHEN (p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = (:year + 1) AND p.payMonth <= 3) THEN p.incomeTax ELSE 0 END) AS ytdIncomeTax, " +
            "SUM(CASE WHEN (p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = (:year + 1) AND p.payMonth <= 3) THEN p.professionalTax ELSE 0 END) AS ytdProfessionalTax, " +
            "SUM(CASE WHEN (p.payYear = :year AND p.payMonth >= 4) OR (p.payYear = (:year + 1) AND p.payMonth <= 3) THEN p.providentFund ELSE 0 END) AS ytdPf, " +
            "SUM(CASE WHEN p.payYear = :year AND p.payMonth = :month THEN p.basicPay + p.houseRentAllowance + p.specialAllowance ELSE 0 END) AS grossEarnings, " +
            "SUM(CASE WHEN p.payYear = :year AND p.payMonth = :month THEN p.incomeTax + p.professionalTax + p.providentFund ELSE 0 END) AS grossDeductions, " +
            "SUM(CASE WHEN p.payYear = :year AND p.payMonth = :month THEN p.lop ELSE 0 END) AS lop " +
            "FROM Payroll p " +
            "WHERE p.empId = :empId " +
            "AND ((p.payYear = :year AND p.payMonth >= 4 AND p.payMonth <= :month) OR (p.payYear = (:year + 1) AND p.payMonth <= 3))")
    Object[] findPayrollDetails(@Param("empId") Long empId, 
                                @Param("year") int year, 
                                @Param("month") int month);
}



**************************************************************************
import com.payroll.dto.PayrollDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayrollService {

    @Autowired
    private PayrollRepository payrollRepository;

    public PayrollDetails getPayrollDetails(Long empId, int year, int month) {
        Object[] payrollData = payrollRepository.findPayrollDetails(empId, year, month);

        if (payrollData != null) {
            double ytdBasic = ((Number) payrollData[0]).doubleValue();
            double ytdHra = ((Number) payrollData[1]).doubleValue();
            double ytdSpecial = ((Number) payrollData[2]).doubleValue();
            double ytdIncomeTax = ((Number) payrollData[3]).doubleValue();
            double ytdProfessionalTax = ((Number) payrollData[4]).doubleValue();
            double ytdPf = ((Number) payrollData[5]).doubleValue();
            double grossEarnings = ((Number) payrollData[6]).doubleValue();
            double grossDeductions = ((Number) payrollData[7]).doubleValue();
            double netPay = grossEarnings - grossDeductions;
            double lop = ((Number) payrollData[8]).doubleValue(); // LOP value

            return new PayrollDetails(ytdBasic, ytdHra, ytdSpecial, ytdIncomeTax, ytdProfessionalTax, ytdPf, grossEarnings, grossDeductions, netPay, lop);
        } else {
            return null;
        }
    }
}


************************************************************************************
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayrollDetails {
    private double ytdBasic;
    private double ytdHra;
    private double ytdSpecial;
    private double ytdIncomeTax;
    private double ytdProfessionalTax;
    private double ytdPf;
    private double grossEarnings;
    private double grossDeductions;
    private double netPay;
    private double lop; // New field for LOP
}


*********************************************************************
import com.payroll.dto.PayrollDetails;
import com.payroll.service.PayrollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping("/details")
    public ResponseEntity<PayrollDetails> getPayrollDetails(@RequestParam Long empId, @RequestParam int year, @RequestParam int month) {
        PayrollDetails payrollDetails = payrollService.getPayrollDetails(empId, year, month);
        if (payrollDetails != null) {
            return ResponseEntity.ok(payrollDetails);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}


*********************************************************************************