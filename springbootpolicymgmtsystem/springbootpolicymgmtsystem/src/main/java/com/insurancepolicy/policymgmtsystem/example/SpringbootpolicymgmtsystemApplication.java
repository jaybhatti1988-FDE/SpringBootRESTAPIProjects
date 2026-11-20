package com.insurancepolicy.policymgmtsystem.example;

import com.insurancepolicy.policymgmtsystem.example.dao.InsurancePolicyRepository;
import com.insurancepolicy.policymgmtsystem.example.entity.InsurancePolicy;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringbootpolicymgmtsystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootpolicymgmtsystemApplication.class, args);
	}
		//ConfigurableApplicationContext context=SpringApplication.run(SpringbootpolicymgmtsystemApplication.class, args);
		
		/*
		InsurancePolicyRepository insurancePolicyRepository =context.getBean(InsurancePolicyRepository.class);
				
		List<InsurancePolicy> policies = List.of(
				
				createPolicy("POL1002", "Anita Patel", "Health", 8500.0, 500000.0,
						LocalDate.of(2025, 6, 15), LocalDate.of(2026, 6, 14), "Rajesh Patel", "ACTIVE"),

				createPolicy("POL1003", "Vikram Singh", "Vehicle", 6200.0, 350000.0,
						LocalDate.of(2025, 3, 10), LocalDate.of(2026, 3, 9), "Meera Singh", "INACTIVE"),

				createPolicy("POL1004", "Sneha Desai", "Home", 15000.0, 2500000.0,
						LocalDate.of(2026, 2, 1), LocalDate.of(2031, 1, 31), "Kiran Desai", "ACTIVE"),

				createPolicy("POL1005", "Arjun Mehta", "Travel", 2500.0, 200000.0,
						LocalDate.of(2026, 5, 20), LocalDate.of(2026, 6, 20), "Neha Mehta", "INACTIVE"),

				createPolicy("POL1006", "Kavita Joshi", "Life", 20000.0, 5000000.0,
						LocalDate.of(2024, 9, 1), LocalDate.of(2044, 9, 1), "Amit Joshi", "ACTIVE")
			);

			Iterable<InsurancePolicy> saved = insurancePolicyRepository.saveAll(policies);
			saved.forEach(System.out::println);
		}

		private static InsurancePolicy createPolicy(String number, String holder, String type,
				Double premium, Double coverage, LocalDate start, LocalDate end,
				String nominee, String status) {

			InsurancePolicy policy = new InsurancePolicy();
			policy.setPolicyNumber(number);
			policy.setPolicyHolderName(holder);
			policy.setPolicyType(type);
			policy.setPremiumAmount(premium);
			policy.setCoverageAmount(coverage);
			policy.setStartDate(start);
			policy.setEndDate(end);
			policy.setNomineeName(nominee);
			policy.setPolicyStatus(status);
			return policy;
		}*/

}
