package com.proyect.CompilAir.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

import jakarta.validation.constraints.*;


@Entity
@Table(name = "Flight")
@Getter
@Setter
public class Flight {

    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Flight number cannot be empty")
    @Size(max = 10, message = "The flight number cannot exceed 10 characters")
    @Column(unique = true, nullable = false)
    private String flightName;

    @NotNull
    @Column(nullable = false)
    private boolean flightStatus = true;

    @Future
    @Column(nullable = false)
    private LocalDateTime departureDate;

    @Future
    @Column(nullable = false)
    private LocalDateTime returnDate;

    @Min(value = 150, message = "The total number of seats must be at least 1")
    @Column(nullable = false)
    private Long totalSeats;


    @Min(value = 0, message = "Reserved seats cannot be negative")
    @Column(nullable = false)
    private Long reservedSeats = 0L;

    @Column(name = "destination")
    private String destination;

    @ManyToOne
    @JoinColumn(name = "route_id", nullable = false)
    @JsonIgnore
    private Route route;

    public Flight() {

    }

    public Flight(Long id, String flightName, boolean flightStatus, LocalDateTime departureDate, LocalDateTime returnDate, Long totalSeats, Long reservedSeats,String destination,Route route) {
        this.id = id;
        this.flightName = flightName;
        this.flightStatus = flightStatus;
        this.departureDate = departureDate;
        this.returnDate = returnDate;
        this.totalSeats = totalSeats;
        this.reservedSeats = reservedSeats;
        this.destination = destination;
        this.route = route;
    }


    public long availableSeats() {
        long available = totalSeats - reservedSeats;
        if (available <= 0) {
            this.flightStatus = false;
        }
        return available;
    }


}

