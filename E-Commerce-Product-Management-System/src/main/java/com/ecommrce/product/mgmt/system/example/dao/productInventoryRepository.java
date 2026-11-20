package com.ecommrce.product.mgmt.system.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommrce.product.mgmt.system.example.entity.Product;

public interface productInventoryRepository extends JpaRepository<Product, Long>{
	
	boolean existsByProductCode(String productCode);
	
	List<Product> findByCategoryIgnoreCase(String category);
	
	List<Product> findByPriceGreaterThan(double price);

}
