package com.telecom.subscriber.management.application.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
public class Subscriber {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank
	@Pattern(regexp = "\\d{10}", message = "mobileNumber must be exactly 10 digits")
	@Column(unique = true, nullable = false, length = 10)
	private String mobileNumber;

	@NotBlank
	@Column(nullable = false)
	private String customerName;

	@NotBlank
	@Column(nullable = false)
	private String planType;

	@NotNull
	@Positive(message = "planAmount must be positive")
	private Double planAmount;

	// data limit in GB
	@NotNull
	@PositiveOrZero(message = "dataLimit cannot be negative")
	private Double dataLimit;

	// call limit in minutes
	@NotNull
	@Min(value = 0, message = "callLimit cannot be negative")
	private Integer callLimit;

	@NotNull
	@Min(value = 0, message = "smsLimit cannot be negative")
	private Integer smsLimit;

	// defaults to today if the client does not send one
	private LocalDate activationDate;

	// defaults to ACTIVE if the client does not send one
	@Pattern(regexp = "ACTIVE|INACTIVE", message = "planStatus must be ACTIVE or INACTIVE")
	private String planStatus;

	public Subscriber() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getPlanType() {
		return planType;
	}

	public void setPlanType(String planType) {
		this.planType = planType;
	}

	public Double getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(Double planAmount) {
		this.planAmount = planAmount;
	}

	public Double getDataLimit() {
		return dataLimit;
	}

	public void setDataLimit(Double dataLimit) {
		this.dataLimit = dataLimit;
	}

	public Integer getCallLimit() {
		return callLimit;
	}

	public void setCallLimit(Integer callLimit) {
		this.callLimit = callLimit;
	}

	public Integer getSmsLimit() {
		return smsLimit;
	}

	public void setSmsLimit(Integer smsLimit) {
		this.smsLimit = smsLimit;
	}

	public LocalDate getActivationDate() {
		return activationDate;
	}

	public void setActivationDate(LocalDate activationDate) {
		this.activationDate = activationDate;
	}

	public String getPlanStatus() {
		return planStatus;
	}

	public void setPlanStatus(String planStatus) {
		this.planStatus = planStatus;
	}

	@Override
	public String toString() {
		return "Subscriber [id=" + id + ", mobileNumber=" + mobileNumber + ", customerName=" + customerName
				+ ", planType=" + planType + ", planAmount=" + planAmount + ", dataLimit=" + dataLimit + ", callLimit="
				+ callLimit + ", smsLimit=" + smsLimit + ", activationDate=" + activationDate + ", planStatus="
				+ planStatus + "]";
	}

}
