package com.railway.ticket.reservation.application.example.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.railway.ticket.reservation.application.example.entity.Ticket;
import java.time.LocalDate;


public interface ticketReservationRepository extends JpaRepository<Ticket, Long>{
	
	boolean existsByTicketNumber(String ticketNumber);
	
	boolean existsByTrainNumberIgnoreCaseAndJourneyDateAndSeatNumberIgnoreCaseAndTicketStatusNot(
			String trainNumber, LocalDate journeyDate, String seatNumber, String ticketStatus);
	List<Ticket> findByTrainNumberIgnoreCase(String ticketNumber);
	
	List<Ticket> findByJourneyDate(LocalDate journeyDate);

}
