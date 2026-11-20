package com.insurancepolicy.policymgmtsystem.example.controller;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.insurancepolicy.policymgmtsystem.example.dao.InsurancePolicyRepository;
import com.insurancepolicy.policymgmtsystem.example.entity.InsurancePolicy;

import jakarta.validation.Valid;


@RestController
public class InsurancePolicyController {
	
	@Autowired
	private InsurancePolicyRepository policyRepository;
	
	//All Policy Holder User information
	
	@GetMapping(value="/policies")
	public List<InsurancePolicy> getpolicies() {
		System.out.println("User Get All Policies");
		return policyRepository.findAll();
	}
	
	//Policy Holder User Information By Id
	
	@GetMapping(value="/policies/{id}")
	public ResponseEntity<InsurancePolicy> getPolicyById(@PathVariable Long id) {
		System.out.println("User Get Policy by Id " + id);
		return policyRepository.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
	}
	
	//Create new Policy Holder User 
	
	@PostMapping("/policies")
	public ResponseEntity<?> createPolicy(@Valid @RequestBody InsurancePolicy policy) {
		System.out.println("New Policy Created Successfully");

		if (policy.getPolicyNumber() != null
				&& policyRepository.existsByPolicyNumber(policy.getPolicyNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Policy number already exists: " + policy.getPolicyNumber());
		}

		policy.setId(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(policyRepository.save(policy));
	}
	
	// Modify or Update new Policy Holder User
	@PutMapping(value="/policies/{id}")
	public ResponseEntity<?> updatePolicy(@PathVariable Long id, 
			@Valid @RequestBody InsurancePolicy updated ){
		
		System.out.println("User Updated Policy: " + id);
		
		if (updated.getPolicyNumber() != null
				&& policyRepository.existsByPolicyNumberAndIdNot(updated.getPolicyNumber(), id)) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Policy number already exists: " + updated.getPolicyNumber());
		}
		
		return policyRepository.findById(id).
				<ResponseEntity<?>>map(existing ->{
					existing.setPolicyNumber(updated.getPolicyNumber());
					existing.setPolicyHolderName(updated.getPolicyHolderName());
					existing.setPolicyType(updated.getPolicyType());
					existing.setPremiumAmount(updated.getPremiumAmount());
					existing.setCoverageAmount(updated.getCoverageAmount());
					existing.setStartDate(updated.getStartDate());
					existing.setEndDate(updated.getEndDate());
					existing.setNomineeName(updated.getNomineeName());
					existing.setPolicyStatus(updated.getPolicyStatus());
					return ResponseEntity.ok(policyRepository.save(existing));
				}).orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Policy not found with id: "+id));
	}
	
	@DeleteMapping(value="/policies/{id}")
	public ResponseEntity<String> deletePolicyById(@PathVariable Long id){
		System.out.println("User Delete Successfully with id :" +id);
		return policyRepository.findById(id)
				.map(policy -> {
					if ("ACTIVE".equalsIgnoreCase(policy.getPolicyStatus())) {
						return ResponseEntity.status(HttpStatus.CONFLICT)
								.body("Cannot delete ACTIVE policy with id: " + id);
					}
					policyRepository.delete(policy);
					return ResponseEntity.ok("Policy deleted successfully with id: " + id);
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Policy not found with id: " + id));
	}
	
	@DeleteMapping(value="/policies")
	public ResponseEntity<String> deleteAllPolicies(){
		System.out.println("All Users Deleted Successfully...!");
		
		List<InsurancePolicy> inactive=policyRepository.findByPolicyStatus("INACTIVE");
		long activeCount = policyRepository.count()-inactive.size();
		
		policyRepository.deleteAll(inactive);
		
		return ResponseEntity.ok("Deleted "+ inactive.size() + " Inactive Policies. "+ activeCount + " ACTIVE policies were not deleted");
	}
	
}
