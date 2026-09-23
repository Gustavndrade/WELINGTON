package com.rental.carros.service;

import com.rental.carros.dto.RentalRequest;
import com.rental.carros.model.RentalRecord;
import com.rental.carros.model.Veiculo;
import com.rental.carros.repository.RentalRecordRepository;
import com.rental.carros.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentalService {

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private RentalRecordRepository rentalRecordRepository;

    @Transactional
    public RentalRecord rentCar(RentalRequest request) {
        Veiculo car = veiculoRepository.findById(request.getCarId())
                .orElseThrow(() -> new IllegalArgumentException("Car not found."));

        if (!Boolean.TRUE.equals(car.getDisponivel())) {
            throw new IllegalArgumentException("Car is not available for rent.");
        }

        car.setDisponivel(false);
        veiculoRepository.save(car);

        RentalRecord record = new RentalRecord(
                car,
                request.getRenterName(),
                request.getRenterContact(),
                request.getRentalStartDate(),
                request.getExpectedReturnDate()
        );

        return rentalRecordRepository.save(record);
    }
}
