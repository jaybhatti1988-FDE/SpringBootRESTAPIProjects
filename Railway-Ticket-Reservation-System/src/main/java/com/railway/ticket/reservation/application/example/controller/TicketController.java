package com.railway.ticket.reservation.application.example.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.railway.ticket.reservation.application.example.dao.ticketReservationRepository;
import com.railway.ticket.reservation.application.example.entity.Ticket;

import jakarta.validation.Valid;

@RestController
public class TicketController {

	@Autowired
	private ticketReservationRepository ticketRepository;

	// Book Ticket
	@PostMapping("/tickets")
	public ResponseEntity<?> bookTicket(@Valid @RequestBody Ticket ticket) {
		System.out.println("Book Ticket");

		// ticket number: generate if missing, otherwise it must be unique
		if (ticket.getTicketNumber() == null || ticket.getTicketNumber().isBlank()) {
			ticket.setTicketNumber("TKT" + System.currentTimeMillis());
		} else if (ticketRepository.existsByTicketNumber(ticket.getTicketNumber())) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Ticket number already exists: " + ticket.getTicketNumber());
		}

		// status: default BOOKED, a new ticket cannot start as CANCELLED
		if (ticket.getTicketStatus() == null) {
			ticket.setTicketStatus("BOOKED");
		} else if ("CANCELLED".equals(ticket.getTicketStatus())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A new ticket must be BOOKED or CONFIRMED, not CANCELLED");
		}

		// seat cannot be duplicated for the same train and date
		if (ticketRepository.existsByTrainNumberIgnoreCaseAndJourneyDateAndSeatNumberIgnoreCaseAndTicketStatusNot(
				ticket.getTrainNumber(), ticket.getJourneyDate(), ticket.getSeatNumber(), "CANCELLED")) {
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body("Seat " + ticket.getSeatNumber() + " is already booked on train "
							+ ticket.getTrainNumber() + " for " + ticket.getJourneyDate());
		}

		ticket.setId(null);
		return ResponseEntity.status(HttpStatus.CREATED).body(ticketRepository.save(ticket));
	}

	// Get All Tickets
	@GetMapping("/tickets")
	public List<Ticket> getAllTickets() {
		System.out.println("Get All Tickets");
		return ticketRepository.findAll();
	}

	// Get Ticket by ID
	@GetMapping("/tickets/{id}")
	public ResponseEntity<?> getTicketById(@PathVariable Long id) {
		System.out.println("Get Ticket by Id " + id);
		return ticketRepository.findById(id)
				.<ResponseEntity<?>>map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Ticket not found with id: " + id));
	}

	// Cancel Ticket
	@PatchMapping("/tickets/{id}/cancel")
	public ResponseEntity<?> cancelTicket(@PathVariable Long id) {
		System.out.println("Cancel Ticket " + id);

		return ticketRepository.findById(id)
				.<ResponseEntity<?>>map(ticket -> {
					if ("CANCELLED".equals(ticket.getTicketStatus())) {
						return ResponseEntity.status(HttpStatus.CONFLICT)
								.body("Ticket is already cancelled with id: " + id);
					}
					if (LocalDate.now().isAfter(ticket.getJourneyDate())) {
						return ResponseEntity.status(HttpStatus.BAD_REQUEST)
								.body("Cannot cancel ticket after the journey date: " + ticket.getJourneyDate());
					}
					ticket.setTicketStatus("CANCELLED");
					return ResponseEntity.ok(ticketRepository.save(ticket));
				})
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
						.body("Ticket not found with id: " + id));
	}

	// Delete Ticket
	@DeleteMapping("/tickets/{id}")
	public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
		System.out.println("Delete Ticket " + id);

		if (!ticketRepository.existsById(id)) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Ticket not found with id: " + id);
		}
		ticketRepository.deleteById(id);
		return ResponseEntity.ok("Ticket deleted successfully with id: " + id);
	}

	// Find by trainNumber
	@GetMapping("/tickets/train/{trainNumber}")
	public List<Ticket> findByTrainNumber(@PathVariable String trainNumber) {
		System.out.println("Find Tickets by Train Number " + trainNumber);
		return ticketRepository.findByTrainNumberIgnoreCase(trainNumber);
	}

	// Find by journeyDate
	@GetMapping("/tickets/date/{journeyDate}")
	public List<Ticket> findByJourneyDate(
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate journeyDate) {
		System.out.println("Find Tickets by Journey Date " + journeyDate);
		return ticketRepository.findByJourneyDate(journeyDate);
	}

}
