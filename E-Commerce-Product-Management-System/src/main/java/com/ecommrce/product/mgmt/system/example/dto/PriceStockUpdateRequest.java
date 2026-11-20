package com.ecommrce.product.mgmt.system.example.dto;

import io.micrometer.common.lang.NonNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;

public class PriceStockUpdateRequest {
	
	@NonNull
	@Positive(message = "Price Must be Positive")
	private Double price;
	
	@NonNull
	@Min(value = 0,message = "stockQuantity can't be Negative")
	private Integer stockQuantity;
	
	public Double getPrice() {
		return price;
	}
	
	public void setPrice(Double price) {
		this.price=price;
	}
	
	public Integer getStockQuantity() {
		return stockQuantity;
	}
	
	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

}
