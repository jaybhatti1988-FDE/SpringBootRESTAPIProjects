package com.loan.management.process.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class LoanStatusUpdateRequest {

	@NotBlank
	@Pattern(regexp = "PENDING|APPROVED|REJECTED", message = "loanStatus must be PENDING, APPROVED or REJECTED")

	private String loanStatus;

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

}
