package com.telecom.subscriber.management.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class PlanUpdateRequest {
	
	@NotBlank
	private String planType;

	@NotNull
	@Positive(message = "planAmount must be positive")
	private Double planAmount;

	@NotNull
	@PositiveOrZero(message = "dataLimit cannot be negative")
	private Double dataLimit;

	@NotNull
	@Min(value = 0, message = "callLimit cannot be negative")
	private Integer callLimit;

	@NotNull
	@Min(value = 0, message = "smsLimit cannot be negative")
	private Integer smsLimit;

	public String getPlanType() { return planType; }
	public void setPlanType(String planType) { this.planType = planType; }

	public Double getPlanAmount() { return planAmount; }
	public void setPlanAmount(Double planAmount) { this.planAmount = planAmount; }

	public Double getDataLimit() { return dataLimit; }
	public void setDataLimit(Double dataLimit) { this.dataLimit = dataLimit; }

	public Integer getCallLimit() { return callLimit; }
	public void setCallLimit(Integer callLimit) { this.callLimit = callLimit; }

	public Integer getSmsLimit() { return smsLimit; }
	public void setSmsLimit(Integer smsLimit) { this.smsLimit = smsLimit; }

}
