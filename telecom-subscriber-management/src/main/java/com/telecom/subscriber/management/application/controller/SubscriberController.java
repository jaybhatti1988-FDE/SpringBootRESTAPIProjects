package com.telecom.subscriber.management.application.controller;

import java.time.LocalDate;
import java.util.List;
import com.telecom.subscriber.management.application.entity.Subscriber;

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

import com.telecom.subscriber.management.application.dao.SubscriberRepository;
import com.telecom.subscriber.management.application.dto.PlanUpdateRequest;

import jakarta.validation.Valid;

@RestController
public class SubscriberController {
	
	@Autowired
	private SubscriberRepository subscriberRepository;

	// Create Subscriber
	@PostMapping("/subscribers")
	public ResponseEntity<?> createSubscriber(@Valid @RequestBody Subscriber subscriber) {
		System.out.println("Create Subscriber");

		if (subscriberRepository.existsByMobileNumber(subscriber.getMobileNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Mobile number already exists: " + subscriber.getMobileNumber());
		}

		if (subscriber.getPlanStatus() == null) {
			subscriber.setPlanStatus("ACTIVE");
		}
		if (subscriber.getActivationDate() == null) {
			subscriber.setActivationDate(LocalDate.now());
		}

		subscriber.setId(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(subscriberRepository.save(subscriber));
	}

	// Get All Subscribers
	@GetMapping("/subscribers")
	public List<Subscriber> getAllSubscribers() {
		System.out.println("Get All Subscribers");
		return subscriberRepository.findAll();
	}

	// Get Active Subscribers
	@GetMapping("/subscribers/active")
	public List<Subscriber> getActiveSubscribers() {
		System.out.println("Get Active Subscribers");
		return subscriberRepository.findByPlanStatus("ACTIVE");
	}

	// Get Subscriber by ID
	@GetMapping("/subscribers/{id}")
	public ResponseEntity<?> getSubscriberById(@PathVariable Long id) {
		System.out.println("Get Subscriber by Id " + id);
		return subscriberRepository.findById(id)
				.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Subscriber not found with id: " + id));
	}

	// Update Plan (upgrade or downgrade): updates planType, planAmount and all limits
	@PatchMapping("/subscribers/{id}/plan")
	public ResponseEntity<?> updatePlan(@PathVariable Long id,
			@Valid @RequestBody PlanUpdateRequest request) {
		System.out.println("Update Plan for Subscriber " + id);

		return subscriberRepository.findById(id)
				.<ResponseEntity<?>>map(subscriber -> {
					subscriber.setPlanType(request.getPlanType());
					subscriber.setPlanAmount(request.getPlanAmount());
					subscriber.setDataLimit(request.getDataLimit());
					subscriber.setCallLimit(request.getCallLimit());
					subscriber.setSmsLimit(request.getSmsLimit());
					return ResponseEntity.ok(subscriberRepository.save(subscriber));
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Subscriber not found with id: " + id));
	}

	// Delete Subscriber
	@DeleteMapping("/subscribers/{id}")
	public ResponseEntity<String> deleteSubscriber(@PathVariable Long id) {
		System.out.println("Delete Subscriber " + id);

		if (!subscriberRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Subscriber not found with id: " + id);
		}
		subscriberRepository.deleteById(id);
		return ResponseEntity.ok("Subscriber deleted successfully with id: " + id);
	}

	// Find by planType
	@GetMapping("/subscribers/plan-type/{planType}")
	public List<Subscriber> findByPlanType(@PathVariable String planType) {
		System.out.println("Find Subscribers by Plan Type " + planType);
		return subscriberRepository.findByPlanTypeIgnoreCase(planType);
	}

}
