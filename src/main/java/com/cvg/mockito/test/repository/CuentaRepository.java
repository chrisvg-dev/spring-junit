package com.cvg.mockito.test.repository;

import com.cvg.mockito.test.models.Cuenta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

}
