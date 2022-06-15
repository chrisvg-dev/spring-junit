package com.cvg.mockito.test.repository;

import com.cvg.mockito.test.models.Cuenta;

import java.util.List;

public interface CuentaRepository {
    List<Cuenta> findAll();
    Cuenta findById(Long id);
    void update(Cuenta cuenta);
}
