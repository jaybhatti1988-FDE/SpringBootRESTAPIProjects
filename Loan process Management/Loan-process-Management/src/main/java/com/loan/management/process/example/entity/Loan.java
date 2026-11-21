package com.loan.management.process.example.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
public class Loan {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Column(unique = true, nullable = false)
	private String loanNumber;

	@NotBlank
	@Column(nullable = false)
	private String customerName;

	@NotBlank
	@Column(nullable = false)
	private String loanType;

	@NotNull
	@Positive(message = "loanAmount must be greater than 0")
	private Double loanAmount;

	@NotNull
	@DecimalMin(value = "5.0", message = "interestRate must be between 5 and 20")
	@DecimalMax(value = "20.0", message = "interestRate must be between 5 and 20")
	private Double interestRate;

	@NotNull
	@Positive(message = "tenureMonths must be greater than 0")
	private Integer tenureMonths;

	// calculated automatically, any value sent by the client is overwritten
	private Double emiAmount;

	@Pattern(regexp = "PENDING|APPROVED|REJECTED",
			message = "loanStatus must be PENDING, APPROVED or REJECTED")
	private String loanStatus;

	private LocalDate approvalDate;

	// Business rule: EMI = P * r * (1+r)^n / ((1+r)^n - 1), r = annualRate / 12 / 100
	@PrePersist
	@PreUpdate
	private void calculateEmi() {
		if (loanAmount != null && interestRate != null && tenureMonths != null && tenureMonths > 0) {
			double r = interestRate / 12 / 100;
			double pow = Math.pow(1 + r, tenureMonths);
			double emi = loanAmount * r * pow / (pow - 1);
			this.emiAmount = Math.round(emi * 100.0) / 100.0;
		}
	}
	
	public Loan() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLoanNumber() {
		return loanNumber;
	}

	public void setLoanNumber(String loanNumber) {
		this.loanNumber = loanNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	public Integer getTenureMonths() {
		return tenureMonths;
	}

	public void setTenureMonths(Integer tenureMonths) {
		this.tenureMonths = tenureMonths;
	}

	public Double getEmiAmount() {
		return emiAmount;
	}

	public void setEmiAmount(Double emiAmount) {
		this.emiAmount = emiAmount;
	}

	public String getLoanStatus() {
		return loanStatus;
	}

	public void setLoanStatus(String loanStatus) {
		this.loanStatus = loanStatus;
	}

	public LocalDate getApprovalDate() {
		return approvalDate;
	}

	public void setApprovalDate(LocalDate approvalDate) {
		this.approvalDate = approvalDate;
	}

	@Override
	public String toString() {
		return "Loan [id=" + id + ", loanNumber=" + loanNumber + ", customerName=" + customerName + ", loanType="
				+ loanType + ", loanAmount=" + loanAmount + ", interestRate=" + interestRate + ", tenureMonths="
				+ tenureMonths + ", emiAmount=" + emiAmount + ", loanStatus=" + loanStatus + ", approvalDate="
				+ approvalDate + "]";
	}
	
	

}
