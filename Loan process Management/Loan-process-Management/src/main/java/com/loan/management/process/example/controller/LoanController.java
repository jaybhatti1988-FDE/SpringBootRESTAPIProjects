package com.loan.management.process.example.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.loan.management.process.example.dao.LoanRepository;
import com.loan.management.process.example.dto.LoanStatusUpdateRequest;
import com.loan.management.process.example.entity.Loan;

import jakarta.validation.Valid;

@RestController
public class LoanController {

	private static final double APPROVAL_LIMIT = 1_000_000.0;

	@Autowired
	private LoanRepository loanRepository;

	@PostMapping("/loans")

	public ResponseEntity<?> createLoan(@Valid @RequestBody Loan loan) {
		System.out.println("Create Loan");

		if (loanRepository.existsByLoanNumber(loan.getLoanNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Loan number already exists: " + loan.getLoanNumber());
		}

		if (loan.getLoanStatus() == null) {
			loan.setLoanStatus("PENDING");
		}
		if ("APPROVED".equals(loan.getLoanStatus())) {
			if (loan.getLoanAmount() >= APPROVAL_LIMIT) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST)
						.body("Loan can be APPROVED only if loanAmount is less than 10 Lakhs");
			}
			loan.setApprovalDate(LocalDate.now());
		} else {
			loan.setApprovalDate(null);
		}

		loan.setId(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(loanRepository.save(loan));
	}

	// Get All Loans
	@GetMapping("/loans")
	public List<Loan> getAllLoans() {
		System.out.println("Get All Loans");
		return loanRepository.findAll();
	}

	// Get Loan by ID
	@GetMapping("/loans/{id}")
	public ResponseEntity<?> getLoanById(@PathVariable Long id) {
		System.out.println("Get Loan by Id " + id);
		return loanRepository.findById(id).<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found with id: " + id));
	}

	// Update Loan Status
	@PatchMapping("/loans/{id}/status")
	public ResponseEntity<?> updateLoanStatus(@PathVariable Long id,
			@Valid @RequestBody LoanStatusUpdateRequest request) {
		System.out.println("Update Loan Status for " + id);

		return loanRepository.findById(id).<ResponseEntity<?>>map(loan -> {
			String newStatus = request.getLoanStatus();

			if ("APPROVED".equals(newStatus)) {
				if (loan.getLoanAmount() >= APPROVAL_LIMIT) {
					return ResponseEntity.status(HttpStatus.BAD_REQUEST)
							.body("Loan can be APPROVED only if loanAmount is less than 10 Lakhs");
				}
				loan.setApprovalDate(LocalDate.now());
			} else {
				loan.setApprovalDate(null);
			}

			loan.setLoanStatus(newStatus);
			return ResponseEntity.ok(loanRepository.save(loan));
		}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found with id: " + id));
	}

	// Delete Loan
	@DeleteMapping("/loans/{id}")
	public ResponseEntity<String> deleteLoan(@PathVariable Long id) {
		System.out.println("Delete Loan " + id);

		if (!loanRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan not found with id: " + id);
		}
		loanRepository.deleteById(id);
		return ResponseEntity.ok("Loan deleted successfully with id: " + id);
	}

	// Find by loanType
	@GetMapping("/loans/type/{loanType}")
	public List<Loan> findByLoanType(@PathVariable String loanType) {
		System.out.println("Find Loans by Type " + loanType);
		return loanRepository.findByLoanTypeIgnoreCase(loanType);
	}

	// Find loans with interestRate > 10
	@GetMapping("/loans/high-interest")
	public List<Loan> findHighInterestLoans() {
		System.out.println("Find Loans with Interest Rate > 10");
		return loanRepository.findByInterestRateGreaterThan(10.0);
	}

}
