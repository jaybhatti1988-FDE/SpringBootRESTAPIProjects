package com.ecommrce.product.mgmt.system.example.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommrce.product.mgmt.system.example.dao.productInventoryRepository;
import com.ecommrce.product.mgmt.system.example.dto.PriceStockUpdateRequest;
import com.ecommrce.product.mgmt.system.example.entity.Product;

import jakarta.validation.Valid;

@RestController
public class ProductController {

	@Autowired
	private productInventoryRepository productRepository;

	// Create New Product

	@PostMapping("/products")
	public ResponseEntity<?> createProduct(@Valid @RequestBody Product product) {
		System.out.println("Product Created");

		if (productRepository.existsByProductCode(product.getProductCode())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(" Product code already exists: " + product.getProductCode());
		}
		product.setId(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(product));
	}

	// Get All Products
	@GetMapping(value = "/products")
	public List<Product> getpolicies() {
		System.out.println("User Get List of All Products");
		return productRepository.findAll();
	}
	
	// Get Products by Id
	@GetMapping("/products/{id}")
	public ResponseEntity<?> getProductById(@PathVariable Long id){
		System.out.println("Get Product By Id " + id);
		return productRepository.findById(id)
				.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
				.body("Product Not Found with Id " + id));
	}
	
	// Update Price & Stocks
	@PatchMapping("/products/{id}")
	public ResponseEntity<?> updatePriceAndStock(@PathVariable Long id,
			@Valid @RequestBody PriceStockUpdateRequest request) {
		System.out.println("Update Price and Stock for Product " + id);
		
		return productRepository.findById(id)
				.<ResponseEntity<?>>map(product ->{
					product.setPrice(request.getPrice());
					product.setStockQuantity(request.getStockQuantity());
					return ResponseEntity.ok(productRepository.save(product));	
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Product not found with id: " + id));
				}
	
	// Delete Product
		@DeleteMapping("/products/{id}")
		public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
			System.out.println("Delete Product " + id);

			if (!productRepository.existsById(id)) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Product not found with id: " + id);
			}
			productRepository.deleteById(id);
			return ResponseEntity.ok("Product deleted successfully with id: " + id);
		}

		// Delete All Products
		@DeleteMapping("/products")
		public ResponseEntity<String> deleteAllProducts() {
			System.out.println("Delete All Products");
			productRepository.deleteAll();
			return ResponseEntity.ok("All products deleted successfully");
		}
		
		// Find by Category
		@GetMapping("/products/category/{category}")
		public List<Product> findByCategory(@PathVariable String category) {
			System.out.println("Find Products by Category " + category);
			return productRepository.findByCategoryIgnoreCase(category);
		}

		// Find by Price Greater Than
		@GetMapping("/products/price-greater-than")
		public List<Product> findByPriceGreaterThan(@RequestParam Double price) {
			System.out.println("Find Products with Price Greater Than " + price);
			return productRepository.findByPriceGreaterThan(price);
		}

}
