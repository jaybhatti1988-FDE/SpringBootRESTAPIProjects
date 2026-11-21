package com.loan.management.process.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.loan.management.process.example.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{
	
	boolean existsByLoanNumber(String loanNumber);
	List<Loan> findByLoanTypeIgnoreCase(String loanType);
	List<Loan> findByInterestRateGreaterThan(double interestRate);

}
