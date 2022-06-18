package com.cvg.mockito.test.repository;

import com.cvg.mockito.test.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    Optional<Cuenta> findByPersona(String persona);
}
