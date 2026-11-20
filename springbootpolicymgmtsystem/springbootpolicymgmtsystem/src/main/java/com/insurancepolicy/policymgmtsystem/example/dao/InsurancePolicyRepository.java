package com.insurancepolicy.policymgmtsystem.example.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.insurancepolicy.policymgmtsystem.example.entity.InsurancePolicy;
import java.util.List;


public interface InsurancePolicyRepository extends JpaRepository<InsurancePolicy, Long>  {
	List<InsurancePolicy> findByPolicyStatus(String policyStatus);
	boolean existsByPolicyNumber(String policyNumber);
	boolean existsByPolicyNumberAndIdNot(String policyNumber, Long id);

}
