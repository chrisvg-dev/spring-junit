package com.cvg.mockito.test.service;

import com.cvg.mockito.test.models.Banco;
import com.cvg.mockito.test.models.Cuenta;
import com.cvg.mockito.test.repository.BancoRepository;
import com.cvg.mockito.test.repository.CuentaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CuentaServiceImpl implements CuentaService {
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    public Cuenta findById(Long id) {
        return this.cuentaRepository.findById(id).orElseThrow();
    }

    @Override
    public int revisarTotalTransferencia(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    public void transferirSaldo(Long numOrigen, Long numDestino, BigDecimal monto, Long bancoId) {
        Cuenta origen = cuentaRepository.findById(numOrigen).orElseThrow();
        origen.debito( monto );
        cuentaRepository.save( origen );

        Cuenta destino = cuentaRepository.findById(numDestino).orElseThrow();
        destino.credito( monto );
        cuentaRepository.save( destino );

        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        Integer totalTransferencia = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencia);
        bancoRepository.save(banco);
    }
}
