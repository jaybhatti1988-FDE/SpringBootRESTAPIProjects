package com.railway.ticket.reservation.application.example.entity;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
public class Ticket {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(unique = true,nullable = false)
	private String ticketNumber;
	
	@NotBlank
	@Column(nullable = false)
	private String trainNumber;
	
	@NotBlank
	@Column(nullable = false)
	private String trainName;

	@NotBlank
	@Column(nullable = false)
	private String sourceStation;

	@NotBlank
	@Column(nullable = false)
	private String destinationStation;

	@NotNull
	@Column(nullable = false)
	private LocalDate journeyDate;

	@NotBlank
	@Column(nullable = false)
	private String seatNumber;
	
	@Pattern(regexp = "BOOKED|CANCELLED|CONFIRMED",
			message = "ticketStatus must be BOOKED, CANCELLED or CONFIRMED")
	private String ticketStatus;
	
	// source and destination must be different
		@JsonIgnore
		@AssertTrue(message = "sourceStation and destinationStation must be different")
		public boolean isDifferentStations() {
			if (sourceStation == null || destinationStation == null) {
				return true; // the @NotBlank checks report missing values
			}
			return !sourceStation.trim().equalsIgnoreCase(destinationStation.trim());
		}

	public Ticket() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTicketNumber() {
		return ticketNumber;
	}

	public void setTicketNumber(String ticketNumber) {
		this.ticketNumber = ticketNumber;
	}

	public String getTrainNumber() {
		return trainNumber;
	}

	public void setTrainNumber(String trainNumber) {
		this.trainNumber = trainNumber;
	}

	public String getTrainName() {
		return trainName;
	}

	public void setTrainName(String trainName) {
		this.trainName = trainName;
	}

	public String getSourceStation() {
		return sourceStation;
	}

	public void setSourceStation(String sourceStation) {
		this.sourceStation = sourceStation;
	}

	public String getDestinationStation() {
		return destinationStation;
	}

	public void setDestinationStation(String destinationStation) {
		this.destinationStation = destinationStation;
	}

	public LocalDate getJourneyDate() {
		return journeyDate;
	}

	public void setJourneyDate(LocalDate journeyDate) {
		this.journeyDate = journeyDate;
	}

	public String getSeatNumber() {
		return seatNumber;
	}

	public void setSeatNumber(String seatNumber) {
		this.seatNumber = seatNumber;
	}

	public String getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(String ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	@Override
	public String toString() {
		return "Ticket [id=" + id + ", ticketNumber=" + ticketNumber + ", trainNumber=" + trainNumber + ", trainName="
				+ trainName + ", sourceStation=" + sourceStation + ", destinationStation=" + destinationStation
				+ ", journeyDate=" + journeyDate + ", seatNumber=" + seatNumber + ", ticketStatus=" + ticketStatus
				+ "]";
	}
	
	
	
}
