package com.rental.carros.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RentalRequest {

    @NotNull(message = "Car ID is mandatory")
    private Long carId;

    @NotBlank(message = "Renter name is mandatory")
    private String renterName;

    @NotBlank(message = "Renter contact is mandatory")
    private String renterContact;

    @NotNull(message = "Rental start date is mandatory")
    private LocalDateTime rentalStartDate;

    @NotNull(message = "Expected return date is mandatory")
    private LocalDateTime expectedReturnDate;

    // Getters and Setters
    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
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
