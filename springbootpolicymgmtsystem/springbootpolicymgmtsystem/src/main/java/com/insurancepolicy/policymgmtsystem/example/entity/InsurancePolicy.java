package com.insurancepolicy.policymgmtsystem.example.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;

@Entity
public class InsurancePolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String policyNumber;
	
	@NotBlank
	@Column(nullable = false)
	private String policyHolderName;
	
	  @NotBlank
	    @Column(nullable = false)
	    private String policyType;

	    @NotNull
	    @Positive
	    private Double premiumAmount;

	    @NotNull
	    @Positive
	    private Double coverageAmount;

	    @NotNull
	    @Column(nullable = false)
	    private LocalDate startDate;

	    @NotNull
	    @Column(nullable = false)
	    private LocalDate endDate;

	    @NotBlank
	    @Column(nullable = false)
	    private String nomineeName;

	    @Pattern(regexp = "ACTIVE|INACTIVE", message = "policyStatus must be ACTIVE or INACTIVE")
	    private String policyStatus;
	        
	    
	    public InsurancePolicy(Long id, String policyNumber, @NotBlank String policyHolderName,
				@NotBlank String policyType, @NotNull @Positive Double premiumAmount,
				@NotNull @Positive Double coverageAmount, @NotNull LocalDate startDate, @NotNull LocalDate endDate,
				@NotBlank String nomineeName,
				@Pattern(regexp = "ACTIVE|INACTIVE", message = "policyStatus must be ACTIVE or INACTIVE") String policyStatus) {
			
			this.id = id;
			this.policyNumber = policyNumber;
			this.policyHolderName = policyHolderName;
			this.policyType = policyType;
			this.premiumAmount = premiumAmount;
			this.coverageAmount = coverageAmount;
			this.startDate = startDate;
			this.endDate = endDate;
			this.nomineeName = nomineeName;
			this.policyStatus = policyStatus;
		}

		public InsurancePolicy() {
	    	
	    }

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getPolicyNumber() {
			return policyNumber;
		}

		public void setPolicyNumber(String policyNumber) {
			this.policyNumber = policyNumber;
		}

		public String getPolicyHolderName() {
			return policyHolderName;
		}

		public void setPolicyHolderName(String policyHolderName) {
			this.policyHolderName = policyHolderName;
		}

		public String getPolicyType() {
			return policyType;
		}

		public void setPolicyType(String policyType) {
			this.policyType = policyType;
		}

		public Double getPremiumAmount() {
			return premiumAmount;
		}

		public void setPremiumAmount(Double premiumAmount) {
			this.premiumAmount = premiumAmount;
		}

		public Double getCoverageAmount() {
			return coverageAmount;
		}

		public void setCoverageAmount(Double coverageAmount) {
			this.coverageAmount = coverageAmount;
		}

		public LocalDate getStartDate() {
			return startDate;
		}

		public void setStartDate(LocalDate startDate) {
			this.startDate = startDate;
		}

		public LocalDate getEndDate() {
			return endDate;
		}

		public void setEndDate(LocalDate endDate) {
			this.endDate = endDate;
		}

		public String getNomineeName() {
			return nomineeName;
		}

		public void setNomineeName(String nomineeName) {
			this.nomineeName = nomineeName;
		}

		public String getPolicyStatus() {
			return policyStatus;
		}

		public void setPolicyStatus(String policyStatus) {
			this.policyStatus = policyStatus;
		}
		
		@JsonIgnore
		@AssertTrue(message = "endDate must be after startDate")
		public boolean isEndDateAfterStartDate() {
			if (startDate == null || endDate == null) {
				return true; // the @NotNull checks report missing dates
			}
			return endDate.isAfter(startDate);
		}

		@Override
		public String toString() {
			return "InsurancePolicy [id=" + id + ", policyNumber=" + policyNumber + ", policyHolderName="
					+ policyHolderName + ", policyType=" + policyType + ", premiumAmount=" + premiumAmount
					+ ", coverageAmount=" + coverageAmount + ", startDate=" + startDate + ", endDate=" + endDate
					+ ", nomineeName=" + nomineeName + ", policyStatus=" + policyStatus + "]";
		}
		
		
	
}
