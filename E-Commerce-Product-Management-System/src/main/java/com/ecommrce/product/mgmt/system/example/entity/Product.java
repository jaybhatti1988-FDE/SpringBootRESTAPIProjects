package com.ecommrce.product.mgmt.system.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Product {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank
	@Column(nullable = false)
	private String productName;
	
	@NotBlank
	@Column(unique = true, nullable = false)
	private String productCode;

	@NotBlank
	@Column(nullable = false)
	private String category;

	@NotNull
	@Positive
	@Column(nullable = false)
	private Double price;

	@NotNull
	@Min(0)
	@Column(nullable = false)
	private Integer stockQuantity;

	@NotBlank
	@Column(nullable = false)
	private String brand;

	@DecimalMin("0.0")
	@DecimalMax("5.0")
	private Double rating;
	
	@Column(length = 1000)
	private String description;

	private String availabilityStatus;
	
	@PrePersist
	@PreUpdate
	private void applyAvailabilityRule() {
		if (stockQuantity != null && stockQuantity == 0) {
			availabilityStatus = "OUT_OF_STOCK";
		} else {
			availabilityStatus = "IN_STOCK";
		}
	}
	
	public Product() {
		
	}

	public Product(Long id, @NotBlank String productName, @NotBlank String productCode, @NotBlank String category,
			@NotNull @Positive Double price, @NotNull @Min(0) Integer stockQuantity, @NotBlank String brand,
			@DecimalMin("0.0") @DecimalMax("5.0") Double rating, String description, String availabilityStatus) {
		this.id = id;
		this.productName = productName;
		this.productCode = productCode;
		this.category = category;
		this.price = price;
		this.stockQuantity = stockQuantity;
		this.brand = brand;
		this.rating = rating;
		this.description = description;
		this.availabilityStatus = availabilityStatus;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}

	public String getAvailabilityStatus() {
		return availabilityStatus;
	}

	public void setAvailabilityStatus(String availabilityStatus) {
		this.availabilityStatus = availabilityStatus;
	}


	@Override
	public String toString() {
		return "Product [id=" + id + ", productName=" + productName + ", productCode=" + productCode + ", category="
				+ category + ", price=" + price + ", stockQuantity=" + stockQuantity + ", brand=" + brand + ", rating="
				+ rating + ", description=" + description + ", availabilityStatus=" + availabilityStatus + "]";
	}


}
