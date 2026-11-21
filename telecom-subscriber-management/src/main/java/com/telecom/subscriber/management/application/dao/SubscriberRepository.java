package com.telecom.subscriber.management.application.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telecom.subscriber.management.application.entity.Subscriber;

public interface SubscriberRepository extends JpaRepository<Subscriber, Long> {

	boolean existsByMobileNumber(String mobileNumber);

	List<Subscriber> findByPlanStatus(String planStatus);

	List<Subscriber> findByPlanTypeIgnoreCase(String planType);

}