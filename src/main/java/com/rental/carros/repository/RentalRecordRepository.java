package com.rental.carros.repository;

import com.rental.carros.model.RentalRecord;
import com.rental.carros.model.StatusAluguel;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RentalRecordRepository extends JpaRepository<RentalRecord, Long> {

    boolean existsByVeiculoIdAndStatus(Long veiculoId, StatusAluguel status);

    boolean existsByVeiculoIdAndClienteIdAndStatus(
            Long veiculoId, Long clienteId, StatusAluguel status);

    long countByStatus(StatusAluguel status);

    List<RentalRecord> findByClienteIdOrderByDataAluguelDesc(Long clienteId);

    List<RentalRecord> findByVeiculoId(Long veiculoId);

    @Query("select r.veiculo.id from RentalRecord r where r.status = :status")
    List<Long> findVeiculoIdsByStatus(@Param("status") StatusAluguel status);
}
