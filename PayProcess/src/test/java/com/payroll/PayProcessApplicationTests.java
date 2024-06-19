package com.payroll;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.payroll.service.PayrollService;

@SpringBootTest
class PayProcessApplicationTests {
	@Autowired
    private PayrollService payrollService;
 
	@Test
	void contextLoads() {
		 assertThat(payrollService).isNotNull();
	}

}
