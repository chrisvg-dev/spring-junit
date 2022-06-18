package com.cvg.mockito.test.service;

import com.cvg.mockito.test.models.Cuenta;

import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {
    Cuenta findById(Long id);
    int revisarTotalTransferencia(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferirSaldo(Long origen, Long destino, BigDecimal monto, Long bancoId);
    List<Cuenta> findAll();
    Cuenta save(Cuenta cuenta);
}
