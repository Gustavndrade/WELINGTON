package com.rental.carros.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "rental_record")
public class RentalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private Veiculo car;

    @NotBlank(message = "Renter name is mandatory")
    private String renterName;

    @NotBlank(message = "Renter contact is mandatory")
    private String renterContact;

    @NotNull(message = "Rental start date is mandatory")
    private LocalDateTime rentalStartDate;

    @NotNull(message = "Expected return date is mandatory")
    private LocalDateTime expectedReturnDate;

    public RentalRecord() {}

    public RentalRecord(Veiculo car, String renterName, String renterContact, LocalDateTime rentalStartDate, LocalDateTime expectedReturnDate) {
        this.car = car;
        this.renterName = renterName;
        this.renterContact = renterContact;
        this.rentalStartDate = rentalStartDate;
        this.expectedReturnDate = expectedReturnDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Veiculo getCar() {
        return car;
    }

    public void setCar(Veiculo car) {
        this.car = car;
    }

    public String getRenterName() {
        return renterName;
    }

    public void setRenterName(String renterName) {
        this.renterName = renterName;
    }

    public String getRenterContact() {
        return renterContact;
    }

    public void setRenterContact(String renterContact) {
        this.renterContact = renterContact;
    }

    public LocalDateTime getRentalStartDate() {
        return rentalStartDate;
    }

    public void setRentalStartDate(LocalDateTime rentalStartDate) {
        this.rentalStartDate = rentalStartDate;
    }

    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }
}
